<template>
  <div class="friends-container">
    <div class="header-section">
      <h2 class="title">Friends</h2>
      <div class="friends-count">
        {{ friends.length || 0 }} friends
      </div>
    </div>

    <div
      v-if="!friends || friends.length === 0"
      class="empty-state"
    >
      <div class="empty-icon">👥</div>
      <p>No friends found</p>
    </div>

    <div v-else class="friends-grid">
      <div
        v-for="(friend, index) in friends"
        :key="index"
        class="friend-card"
      >
        <div class="friend-avatar">
          <div class="avatar-text">{{ getInitials(friend.name) }}</div>
        </div>
        <div class="friend-info">
          <h3 class="friend-name">
            <router-link :to="{path:'/friendInfo',query:{userId:friend.userId}}" class="name-link">
              {{ friend.name }}
            </router-link>
          </h3>
          <p class="friend-city">{{ friend.city || "Unknown City" }}</p>
          <div class="friend-stats">
            <div class="stat-item">
              <el-icon color="#f37325"><User /></el-icon>
              <span class="stat-value">{{ friend.friendNumbers || 0 }}</span>
              <span class="stat-label">Friends</span>
            </div>
            <div class="stat-item">
              <el-icon color="#f37325"><Star /></el-icon>
              <span class="stat-value">{{ friend.reviewCount || 0 }}</span>
              <span class="stat-label">Reviews</span>
            </div>
          </div>
        </div>
        <div class="friend-actions">
          <div class="delete-box" @click="showDeleteDialog(index)">
            <el-icon><Remove /></el-icon>
            <span>Delete</span>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="Warning!"
      width="500"
    >
      <span>Are you sure to delete this friend?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="confirmDelete()">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import UseFriends from "@/hooks/UseFriends";
import { onMounted, ref, toRefs } from "vue";
import { User, Star, Remove } from '@element-plus/icons-vue';

// 使用朋友hook
let { friends, getFriends, deleteFriend } = toRefs(UseFriends());

// 对话框展示
let dialogVisible = ref(false);
let currentDeleteIndex = ref<number | null>(null);

onMounted(() => {
  getFriends.value();
});

// 显示删除对话框
const showDeleteDialog = (index: number) => {
  currentDeleteIndex.value = index;
  dialogVisible.value = true;
};

// 确认删除
const confirmDelete = () => {
  if (currentDeleteIndex.value !== null && friends.value[currentDeleteIndex.value]) {
    const uid = friends.value[currentDeleteIndex.value].uid;
    deleteFriend.value(uid);
    dialogVisible.value = false;
    currentDeleteIndex.value = null;
  }
};

// 获取姓名首字母
const getInitials = (name: string) => {
  if (!name) return '?';
  const firstChar = name.trim().charAt(0).toUpperCase();
  return firstChar || '?';
};
</script>

<style scoped>
.friends-container {
  padding: 20px;
  margin: 0 auto;
  max-height: 600px;
  overflow-y: auto;
}

.friends-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebebeb;
}

.title {
  color: #2d2e2f;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
}

.friends-count {
  color: #666666;
  font-size: 16px;
  background-color: #f5f5f5;
  padding: 5px 15px;
  border-radius: 20px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.friend-card {
  display: flex;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  width: 600px;
  transition: all 0.3s ease;
  border: 1px solid #eee;
  position: relative;
}

.friend-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.friend-avatar {
  flex: 0 0 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.friend-info {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.friend-name {
  margin: 0 0 5px 0;
  color: #0073bb;
  font-size: 18px;
  font-weight: 600;
}

.name-link {
  color: inherit;
  text-decoration: none;
}

.name-link:hover {
  text-decoration: underline;
}

.friend-city {
  margin: 0 0 15px 0;
  color: #666666;
  font-size: 14px;
}

.friend-stats {
  display: flex;
  gap: 15px;
  margin-top: auto;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.stat-value {
  font-weight: 600;
  color: #333;
}

.stat-label {
  color: #999;
  font-size: 12px;
}

.avatar-text {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  font-weight: bold;
  border: 3px solid white;
}

.friend-actions {
  position: absolute;
  top: 15px;
  right: 15px;
}

.delete-box {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #E00707;
  font-size: 14px;
  background: rgba(224, 7, 7, 0.1);
  padding: 5px 10px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.delete-box:hover {
  background: rgba(224, 7, 7, 0.2);
}

.delete-box span {
  margin-left: 5px;
}

@media (max-width: 768px) {
  .friends-grid {
    grid-template-columns: 1fr;
  }
  
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .friend-card {
    flex-direction: row;
  }
  
  .friend-avatar {
    flex: 0 0 80px;
  }
  
  .avatar-text {
    width: 60px;
    height: 60px;
    font-size: 24px;
  }
  
  .friends-container {
    max-height: 500px;
  }
  
  .friend-actions {
    position: static;
    margin-top: 10px;
    align-self: flex-end;
  }
}

@media (min-width: 1400px) {
  .friends-grid {
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  }
}
</style>