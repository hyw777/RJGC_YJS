<template>
  <div class="user-management">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-wrapper">
        <el-input
          v-model="input"
          placeholder="Search with email or name..."
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>
    </div>

    <!-- 用户统计 (只保留总数) -->
    <div class="stats-section" v-if="pageResult.total > 0">
      <el-card class="stat-card">
        <div class="stat-item">
          <el-icon size="24" color="#409EFF"><User /></el-icon>
          <div>
            <div class="stat-number">{{ pageResult.total }}</div>
            <div class="stat-label">Total Users</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 加载指示器 -->
    <div class="loading-container" v-if="loading">
      <el-skeleton animated>
        <template #template>
          <div class="skeleton-grid">
            <div v-for="i in 6" :key="i" class="skeleton-item">
              <el-skeleton-item variant="circle" class="skeleton-avatar" />
              <div class="skeleton-content">
                <el-skeleton-item variant="text" class="skeleton-line short" />
                <el-skeleton-item variant="text" class="skeleton-line" />
                <div class="skeleton-stats">
                  <el-skeleton-item variant="text" class="skeleton-stat" />
                  <el-skeleton-item variant="text" class="skeleton-stat" />
                </div>
              </div>
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 用户列表 -->
    <div v-else class="users-section">
      <el-empty description="No users found" v-if="pageResult.total === 0" />

      <div v-else>
        <el-scrollbar
          ref="scrollbarRef"
          class="users-scrollbar"
          @scroll="handleScroll"
        >
          <div class="users-grid">
            <div
              v-for="(user, index) in visibleUsers"
              :key="user.userId"
              class="user-card"
              :class="{ blocked: user.status === 1 }"
            >
              <div class="user-header">
                <div
                  class="avatar-placeholder"
                  :style="getAvatarStyle(user.name)"
                >
                  <span class="avatar-text">{{ getInitials(user.name) }}</span>
                </div>
                <div
                  class="user-status-badge"
                  :class="getStatusClass(user.status)"
                >
                  {{ getStatusText(user.status) }}
                </div>
              </div>

              <div class="user-info">
                <h3 class="user-name">{{ user.name }}</h3>
                <p class="user-email">{{ user.email }}</p>
                <p class="user-city" v-if="user.city">
                  <el-icon><Location /></el-icon>
                  {{ user.city }}
                </p>
              </div>

              <div class="user-stats">
                <div class="stat-item">
                  <el-icon color="#409eff"><User /></el-icon>
                  <div>
                    <div class="stat-value">{{ user.friendNumbers || 0 }}</div>
                    <div class="stat-label">Friends</div>
                  </div>
                </div>
                <div class="stat-item">
                  <el-icon color="#f37325"><Star /></el-icon>
                  <div>
                    <div class="stat-value">{{ user.reviewNumbers || 0 }}</div>
                    <div class="stat-label">Reviews</div>
                  </div>
                </div>
              </div>

              <div class="user-actions">
                <el-button
                  size="small"
                  @click="seeStatus(user.userId)"
                  :icon="Tools"
                >
                  Manage
                </el-button>
              </div>
            </div>
          </div>
        </el-scrollbar>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            background
            :current-page="findFriendDTO.pageNum"
            layout="prev, pager, next, jumper, ->, total"
            :total="pageResult.total"
            :page-size="findFriendDTO.pageSize"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <!-- 状态管理对话框 -->
    <el-dialog
      v-model="display"
      title="User Status Management"
      width="400px"
      center
    >
      <div class="status-dialog-content">
        <div v-if="status === 0" class="status-normal">
          <el-icon size="48" color="#67C23A"><CircleCheck /></el-icon>
          <h3>User is currently active</h3>
          <p>This user can access all system features</p>
        </div>
        <div v-else-if="status === 1" class="status-blocked">
          <el-icon size="48" color="#F56C6C"><CircleClose /></el-icon>
          <h3>User is currently blocked</h3>
          <p>This user cannot access system features</p>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="display = false">Cancel</el-button>
          <el-button
            v-if="status === 0"
            type="danger"
            @click="changeUserStatus(userId, 1)"
          >
            Block User
          </el-button>
          <el-button v-else type="success" @click="changeUserStatus(userId, 0)">
            Unblock User
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, nextTick } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import {
  Search,
  User,
  Star,
  Tools,
  CircleCheck,
  CircleClose,
  Location,
  Loading,
  Back,
  Lock,
  Unlock,
} from "@element-plus/icons-vue";

onMounted(() => {
  handleSearch();
});

// 数据状态
const loading = ref(false);
const input = ref("");
const pageResult = ref({
  records: [],
  total: 0,
});
// 修改分页参数
const findFriendDTO = ref({
  pageSize: 10, // 设置为10，与显示数量一致
  pageNum: 1,
  email: "",
  userName: "",
});

const status = ref(-1);
const userId = ref(0);
const display = ref(false);

// 虚拟滚动相关
const visibleUsers = ref([]);
const loadedCount = ref(0);
const batchSize = 10;
const hasMore = ref(true);
const scrollbarRef = ref();

// 计算属性
const normalUsersCount = computed(() => {
  return pageResult.value.records.filter((user) => user.status === 0).length;
});

const blockedUsersCount = computed(() => {
  return pageResult.value.records.filter((user) => user.status === 1).length;
});

// 获取用户首字母
const getInitials = (name) => {
  if (!name) return "?";
  return name.charAt(0).toUpperCase();
};

// 获取头像背景色
const getAvatarStyle = (name) => {
  if (!name) return { backgroundColor: "#409eff" };

  const colors = [
    "#409eff",
    "#67c23a",
    "#e6a23c",
    "#f56c6c",
    "#909399",
    "#00c1d4",
    "#722ed1",
    "#52c41a",
    "#fa8c16",
    "#eb2f96",
  ];

  const charCode = name.charCodeAt(0);
  const colorIndex = charCode % colors.length;

  return {
    backgroundColor: colors[colorIndex],
  };
};

// 获取用户状态文本
const getStatusText = (status) => {
  return status === 1 ? "Blocked" : "Active";
};

// 获取用户状态样式类
const getStatusClass = (status) => {
  return status === 1 ? "blocked" : "active";
};

// 搜索处理
const handleSearch = () => {
  if (input.value.includes("@")) {
    findFriendDTO.value.email = input.value;
    findFriendDTO.value.userName = "";
  } else {
    findFriendDTO.value.userName = input.value;
    findFriendDTO.value.email = "";
  }

  findFriendDTO.value.pageNum = 1;
  fetchUsers();
};

// 分页处理
const handlePageChange = (pageNum) => {
  console.log("Page changed to:", pageNum); // 添加调试日志
  findFriendDTO.value.pageNum = pageNum;
  fetchUsers();
};

// 获取用户数据
// 在 fetchUsers 方法中添加缓存机制
const userCache = new Map(); // 添加缓存

// 修改 fetchUsers 函数，移除缓存逻辑
const fetchUsers = async () => {
  loading.value = true;
  
  try {
    const response = await axios.post(
      "/api/userManage/getInfo",
      findFriendDTO.value
    );
    pageResult.value = response.data.data;

    // 重置虚拟滚动状态
    visibleUsers.value = [];
    loadedCount.value = 0;
    hasMore.value = true;

    // 加载第一批数据
    loadBatch();

    nextTick(() => {
      if (scrollbarRef.value) {
        scrollbarRef.value.setScrollTop(0);
      }
    });
  } catch (error) {
    console.error("Failed to fetch users:", error);
  } finally {
    loading.value = false;
  }
};

// 批量加载用户
// 优化批量加载逻辑
const loadBatch = () => {
  const start = loadedCount.value;
  const end = Math.min(start + batchSize, pageResult.value.records.length);

  if (start < end) {
    // 使用数组展开而非多次 push
    visibleUsers.value = [
      ...visibleUsers.value,
      ...pageResult.value.records.slice(start, end),
    ];
    loadedCount.value = end;
    hasMore.value = end < pageResult.value.records.length;
  } else {
    hasMore.value = false;
  }
};

// 查看用户状态
const seeStatus = async (id) => {
  try {
    const response = await axios.get(`/api/userManage/getStatus/${id}`);
    status.value = response.data.data;
    userId.value = id;
    display.value = true;
  } catch (error) {
    console.error("Failed to get user status:", error);
  }
};

// 更改用户状态
const changeUserStatus = async (targetUserId, newStatus) => {
  try {
    await axios.put(`/api/userManage/changeStatus`, {
      userId: targetUserId,
      status: newStatus,
    });

    // 更新本地状态
    status.value = newStatus;

    // 更新用户列表中的状态
    const userIndex = pageResult.value.records.findIndex(
      (u) => u.userId === targetUserId
    );
    if (userIndex !== -1) {
      // 创建新的记录数组以触发响应式更新
      const updatedRecords = [...pageResult.value.records];
      updatedRecords[userIndex] = {
        ...updatedRecords[userIndex],
        status: newStatus,
      };
      pageResult.value.records = updatedRecords;
    }

    // 同样更新 visibleUsers 中的状态
    const visibleUserIndex = visibleUsers.value.findIndex(
      (u) => u.userId === targetUserId
    );
    if (visibleUserIndex !== -1) {
      visibleUsers.value[visibleUserIndex].status = newStatus;
    }

    display.value = false;

    ElMessage.success(
      newStatus === 0
        ? "User has been unblocked successfully"
        : "User has been blocked successfully"
    );
  } catch (error) {
    console.error("Failed to change user status:", error);
    ElMessage.error("Failed to update user status");
  }
};
</script>

<style scoped>
.user-management {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 搜索区域 */
.search-section {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.search-wrapper {
  width: 100%;
  max-width: 500px;
}

.search-input :deep(.el-input-group__append) {
  background-color: #409eff;
  border-color: #409eff;
  color: white;
}

.search-input :deep(.el-button) {
  color: white;
}

/* 统计区域 */
.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 加载容器 */
.loading-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.skeleton-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.skeleton-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}

.skeleton-content {
  flex: 1;
}

.skeleton-line {
  width: 100%;
  height: 16px;
  margin-bottom: 10px;
}

.skeleton-line.short {
  width: 60%;
}

.skeleton-stats {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.skeleton-stat {
  width: 80px;
  height: 14px;
}

/* 用户列表区域 */
.users-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.users-scrollbar {
  height: calc(100vh - 300px);
}

.users-grid {
  display: grid;
  /* 修改网格列数，使卡片更小 */
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 15px;
  padding: 10px;
}

.user-card {
  border: 1px solid #ebeef5;
  border-radius: 10px;
  padding: 15px;
  transition: all 0.3s ease;
  position: relative;
  background: white;
  /* 限制卡片最大高度 */
  max-height: 220px;
}

.user-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.user-card.blocked {
  border-color: #f56c6c;
  background-color: #fdf6f6;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.avatar-placeholder {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 18px;
}

.user-status-badge {
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: bold;
}
.user-status-badge.active {
  background-color: #f0f9eb;
  color: #67c23a;
}

.user-status-badge.blocked {
  background-color: #fef0f0;
  color: #f56c6c;
}

.user-info {
  margin-bottom: 15px;
}

.user-name {
  margin: 0 0 6px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  /* 限制名称长度 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-email {
  margin: 0 0 6px 0;
  font-size: 13px;
  color: #909399;
  /* 限制邮箱长度 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-city {
  margin: 0;
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.stat-value {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.stat-label {
  color: #909399;
  font-size: 11px;
}

.user-actions {
  display: flex;
  justify-content: flex-end;
}

/* 骨架屏调整 */
.skeleton-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
}

.skeleton-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
}

.skeleton-line {
  width: 100%;
  height: 14px;
  margin-bottom: 8px;
}

.skeleton-line.short {
  width: 60%;
}

.skeleton-stats {
  display: flex;
  gap: 15px;
  margin-top: 8px;
}

.skeleton-stat {
  width: 70px;
  height: 12px;
}

/* 加载更多提示 */
.loading-more,
.no-more {
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

.loading-icon {
  animation: rotating 2s linear infinite;
  margin-right: 8px;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 分页区域 */
.pagination-wrapper {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 状态对话框 */
.status-dialog-content {
  text-align: center;
  padding: 20px 0;
}

.status-dialog-content h3 {
  margin: 15px 0 10px;
  color: #303133;
}

.status-dialog-content p {
  color: #909399;
  margin: 0;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .users-grid {
    grid-template-columns: 1fr;
  }

  .stats-section {
    grid-template-columns: 1fr;
  }

  .search-wrapper {
    max-width: 100%;
  }
}
</style>
