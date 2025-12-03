<template>
  <div class="friends-container">
    <div class="header-section">
      <h2 class="title">Friends</h2>
      <div class="friends-count">
        {{ friendInfo.findfriendVOS?.length || 0 }} friends
      </div>
    </div>

    <div
      v-if="!friendInfo.findfriendVOS || friendInfo.findfriendVOS.length === 0"
      class="empty-state"
    >
      <div class="empty-icon">👥</div>
      <p>No friends found</p>
    </div>

    <div v-else class="friends-grid">
      <div
        v-for="(friend, index) in friendInfo.findfriendVOS"
        :key="index"
        class="friend-card"
      >
        <div class="friend-avatar">
          <!-- 替换图片为文字头像 -->
          <div class="avatar-text">{{ getInitials(friend.name) }}</div>
        </div>
        <div class="friend-info">
          <h3 class="friend-name">{{ friend.name }}</h3>
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
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import UseFriendInfo from "@/hooks/UseFriendInfo";
import { User, Star } from '@element-plus/icons-vue';

// 获取用户id
const props = defineProps<{
  userId?: string
}>()

const userId = computed(() => props.userId)

// 使用朋友信息hook
const { friendInfo, getFriendInfo } = UseFriendInfo()

// 获取姓名首字母
const getInitials = (name: string) => {
  if (!name) return '?';
  const firstChar = name.trim().charAt(0).toUpperCase();
  return firstChar || '?';
}

onMounted(() => {
  if (userId.value) {
    getFriendInfo(userId.value)
  }
})
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
  width: 600px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #eee;
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
  background: linear-gradient(135deg, #6ee7b7, #3b82f6);
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
}

@media (min-width: 1400px) {
  .friends-grid {
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  }
}
</style>
