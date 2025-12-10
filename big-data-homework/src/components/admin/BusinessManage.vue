<template>
  <div class="business-management">
    <!-- 搜索区域 -->
    <!-- <div class="search-section">
      <div class="search-wrapper">
        <el-input
          v-model="input"
          placeholder="Search with name or city..."
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>
    </div> -->

    <!-- 商户统计 -->
    <div class="stats-section" v-if="pageResult.total > 0">
      <el-card class="stat-card">
        <div class="stat-item">
          <el-icon size="24" color="#409EFF"><Shop /></el-icon>
          <div>
            <div class="stat-number">{{ pageResult.total }}</div>
            <div class="stat-label">Total Businesses</div>
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

    <!-- 商户列表 -->
    <div v-else class="businesses-section">
      <el-empty
        description="No businesses found"
        v-if="pageResult.total === 0"
      />

      <div v-else>
        <el-scrollbar
          ref="scrollbarRef"
          class="businesses-scrollbar"
          @scroll="handleScroll"
        >
          <div class="businesses-grid">
            <div
              v-for="(business, index) in visibleBusinesses"
              :key="business.businessId"
              class="business-card"
              :class="{ blocked: business.isOpen === -2 }"
            >
              <div class="business-header">
                <div
                  class="avatar-placeholder"
                  :style="getAvatarStyle(business.name)"
                >
                  <span class="avatar-text">{{
                    getInitials(business.name)
                  }}</span>
                </div>
                <div
                  class="business-status-badge"
                  :class="getStatusClass(business.isOpen)"
                >
                  {{ getStatusText(business.isOpen) }}
                </div>
              </div>

              <div class="business-info">
                <h3 class="business-name">{{ business.name }}</h3>
                <p class="business-categories">{{ business.categories }}</p>
                <p class="business-city" v-if="business.city">
                  <el-icon><Location /></el-icon>
                  {{ business.city }}, {{ business.state }}
                </p>
              </div>

              <div class="business-stats">
                <div class="stat-item">
                  <el-icon color="#409eff"><Star /></el-icon>
                  <div>
                    <div class="stat-value">{{ business.stars || 0 }}</div>
                    <div class="stat-label">Stars</div>
                  </div>
                </div>
                <div class="stat-item">
                  <el-icon color="#f37325"><ChatLineSquare /></el-icon>
                  <div>
                    <div class="stat-value">
                      {{ business.reviewCount || 0 }}
                    </div>
                    <div class="stat-label">Reviews</div>
                  </div>
                </div>
              </div>

              <div class="business-actions">
                <el-button
                  size="small"
                  @click="seeStatus(business.businessId)"
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
            :current-page="businessQueryDTO.pageNum"
            layout="prev, pager, next, jumper, ->, total"
            :total="pageResult.total"
            :page-size="businessQueryDTO.pageSize"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <!-- 状态管理对话框 -->
    <el-dialog
      v-model="display"
      title="Business Status Management"
      width="400px"
      center
    >
      <div class="status-dialog-content">
        <div v-if="status !== -2" class="status-normal">
          <el-icon size="48" color="#67C23A"><CircleCheck /></el-icon>
          <h3>Business is currently active</h3>
          <p>This business can operate normally</p>
        </div>
        <div v-else class="status-blocked">
          <el-icon size="48" color="#F56C6C"><CircleClose /></el-icon>
          <h3>Business is currently blocked</h3>
          <p>This business cannot operate</p>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="display = false">Cancel</el-button>
          <el-button
            v-if="status !== -2"
            type="danger"
            @click="changeBusinessStatus(currentBusinessId, 'block')"
          >
            Block Business
          </el-button>
          <el-button
            v-else
            type="success"
            @click="changeBusinessStatus(currentBusinessId, 'unblock')"
          >
            Unblock Business
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
  Shop,
  Star,
  ChatLineSquare,
  Tools,
  CircleCheck,
  CircleClose,
  Location,
  Lock,
  Check,
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
const businessQueryDTO = ref({
  pageSize: 10,
  pageNum: 1,
  name: "",
  city: "",
});

const status = ref(-1);
const currentBusinessId = ref(0);
const display = ref(false);

// 虚拟滚动相关
const visibleBusinesses = ref([]);
const loadedCount = ref(0);
const batchSize = 10;
const hasMore = ref(true);
const scrollbarRef = ref();

// // 计算属性
// const activeBusinessesCount = computed(() => {
//   return pageResult.value.records.filter((business) => business.isOpen === 1)
//     .length;
// });

// const blockedBusinessesCount = computed(() => {
//   return pageResult.value.records.filter((business) => business.isOpen === -2)
//     .length;
// });
// 获取商户首字母
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

// 获取商户状态文本
const getStatusText = (status) => {
  switch (status) {
    case -1:
      return "Pending";
    case 0:
      return "Closed";
    case 1:
      return "Open";
    case -2:
      return "Blocked";
    default:
      return "Unknown";
  }
};

// 获取商户状态样式类
const getStatusClass = (status) => {
  switch (status) {
    case -1:
      return "pending";
    case 0:
      return "closed";
    case 1:
      return "active";
    case -2:
      return "blocked";
    default:
      return "";
  }
};

// 搜索处理
const handleSearch = () => {
  if (!input.value) {
    businessQueryDTO.value.name = "";
    businessQueryDTO.value.city = "";
  } else if (input.value.includes(",")) {
    // 假设输入格式为 "name, city"
    const parts = input.value.split(",");
    businessQueryDTO.value.name = parts[0]?.trim() || "";
    businessQueryDTO.value.city = parts[1]?.trim() || "";
  } else {
    businessQueryDTO.value.name = input.value;
    businessQueryDTO.value.city = "";
  }

  businessQueryDTO.value.pageNum = 1;
  fetchBusinesses();
};

// 分页处理
const handlePageChange = (pageNum) => {
  businessQueryDTO.value.pageNum = pageNum;
  fetchBusinesses();
};

// 获取商户数据
const fetchBusinesses = async () => {
  loading.value = true;

  try {
    // 根据后端接口调整请求方式和参数
    const params = {
      page: businessQueryDTO.value.pageNum,
      pageSize: businessQueryDTO.value.pageSize,
    };

    // 如果有搜索条件，则添加到查询参数中
    if (businessQueryDTO.value.name) {
      params.name = businessQueryDTO.value.name;
    }
    if (businessQueryDTO.value.city) {
      params.city = businessQueryDTO.value.city;
    }

    const response = await axios.get("/api/businessManage/list", { params });
    pageResult.value = response.data.data;

    // 重置虚拟滚动状态
    visibleBusinesses.value = [];
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
    console.error("Failed to fetch businesses:", error);
    ElMessage.error("Failed to fetch businesses");
  } finally {
    loading.value = false;
  }
};

// 批量加载商户
const loadBatch = () => {
  const start = loadedCount.value;
  const end = Math.min(start + batchSize, pageResult.value.records.length);

  if (start < end) {
    visibleBusinesses.value = [
      ...visibleBusinesses.value,
      ...pageResult.value.records.slice(start, end),
    ];
    loadedCount.value = end;
    hasMore.value = end < pageResult.value.records.length;
  } else {
    hasMore.value = false;
  }
};

// 查看商户状态
const seeStatus = async (id) => {
  // 直接从可见商户列表中查找对应商户的状态
  const business = visibleBusinesses.value.find((b) => b.businessId === id);
  if (business) {
    status.value = business.isOpen;
    currentBusinessId.value = id;
    display.value = true;
    console.log("Found business in visible list:", business);
  } else {
    // 如果在可见列表中找不到，可以从完整列表中查找
    const allBusiness = pageResult.value.records.find(
      (b) => b.businessId === id
    );
    if (allBusiness) {
      status.value = allBusiness.isOpen;
      currentBusinessId.value = id;
      display.value = true;
    } else {
      ElMessage.error("未找到商户信息");
    }
  }
};

// 更改商户状态
const changeBusinessStatus = async (targetBusinessId, action) => {
  try {
    let newStatus;
    let successMessage;

    // 根据操作类型设置新状态
    if (action === "block") {
      newStatus = -2; // 封禁
      successMessage = "Business has been blocked successfully";
    } else if (action === "unblock") {
      newStatus = 0; // 解封（设置为未营业）
      successMessage = "Business has been unblocked successfully";
    } else {
      ElMessage.error("Invalid action");
      return;
    }
    console.log("Changing business status:", targetBusinessId, "to", newStatus);
    // 调用后端接口更新状态
    await axios.put(`/api/businessManage/changeBusinessStatus`, {
      businessId: targetBusinessId,
      isOpen: newStatus,
    });

    // 更新本地状态
    status.value = newStatus;

    // 更新商户列表中的状态
    const businessIndex = pageResult.value.records.findIndex(
      (b) => b.businessId === targetBusinessId
    );
    if (businessIndex !== -1) {
      const updatedRecords = [...pageResult.value.records];
      updatedRecords[businessIndex] = {
        ...updatedRecords[businessIndex],
        isOpen: newStatus,
      };
      pageResult.value.records = updatedRecords;
    }

    // 同样更新 visibleBusinesses 中的状态
    const visibleBusinessIndex = visibleBusinesses.value.findIndex(
      (b) => b.businessId === targetBusinessId
    );
    if (visibleBusinessIndex !== -1) {
      visibleBusinesses.value[visibleBusinessIndex].isOpen = newStatus;
    }

    display.value = false;
    ElMessage.success(successMessage);
  } catch (error) {
    console.error("Failed to change business status:", error);
    ElMessage.error("Failed to update business status");
  }
};
</script>

<style scoped>
.business-status-badge.pending {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.business-status-badge.closed {
  background-color: #f4f4f5;
  color: #909399;
}

.business-status-badge.active {
  background-color: #f0f9eb;
  color: #67c23a;
}

.business-status-badge.blocked {
  background-color: #fef0f0;
  color: #f56c6c;
}
.business-management {
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

/* 商户列表区域 */
.businesses-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.businesses-scrollbar {
  height: calc(100vh - 300px);
}

.businesses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 15px;
  padding: 10px;
}

.business-card {
  border: 1px solid #ebeef5;
  border-radius: 10px;
  padding: 15px;
  transition: all 0.3s ease;
  position: relative;
  background: white;
  max-height: 220px;
}

.business-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.business-card.blocked {
  border-color: #f56c6c;
  background-color: #fdf6f6;
}

.business-header {
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

.business-status-badge {
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: bold;
}

.business-status-badge.active {
  background-color: #f0f9eb;
  color: #67c23a;
}

.business-status-badge.blocked {
  background-color: #fef0f0;
  color: #f56c6c;
}

.business-info {
  margin-bottom: 15px;
}

.business-name {
  margin: 0 0 6px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.business-categories {
  margin: 0 0 6px 0;
  font-size: 13px;
  color: #909399;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.business-city {
  margin: 0;
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
  gap: 4px;
}

.business-stats {
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

.business-actions {
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
  .businesses-grid {
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
