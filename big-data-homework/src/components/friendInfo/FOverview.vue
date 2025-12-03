<template>
  <div class="overview-container">
    <div class="profile-card">
      <div class="card-header">
        <div class="avatar-wrapper">
          <div class="avatar">{{ initials }}</div>
        </div>
        <div class="header-info">
          <div class="title">{{ friendInfo?.userVO?.name || 'Profile overview' }}</div>
          <div class="subtitle">{{ friendInfo?.userVO?.city || '' }}</div>
        </div>
      </div>

      <div class="card-body">
        <div class="profile-section">
          <h2 class="section-title">基本信息</h2>
          <div class="basic-info">
            <div class="info-item">
              <span class="label">姓名</span>
              <span class="value">{{ friendInfo?.userVO?.name || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">年龄</span>
              <span class="value">{{ friendInfo?.userVO?.age ?? '-' }} 岁</span>
            </div>
            <div class="info-item">
              <span class="label">性别</span>
              <span class="value">{{ friendInfo?.userVO?.gender || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">城市</span>
              <span class="value">{{ friendInfo?.userVO?.city || '-' }}</span>
            </div>
          </div>
        </div>
        
        <div class="profile-section">
          <h2 class="section-title">联系信息</h2>
          <div class="contact-info">
            <div class="info-item">
              <span class="label">邮箱</span>
              <span class="value">{{ friendInfo?.userVO?.email || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">注册时间</span>
              <span class="value">{{ friendInfo?.userVO?.yelpingSince || '-' }}</span>
            </div>
          </div>
        </div>
        
        <div class="profile-section">
          <h2 class="section-title">统计数据</h2>
          <div class="stats-info">
            <div class="stat-card">
              <div class="stat-value">{{ friendInfo?.userVO?.reviewCount ?? 0 }}</div>
              <div class="stat-label">评论数</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ friendInfo?.userVO?.useful ?? 0 }}</div>
              <div class="stat-label">有用数</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ friendInfo?.userVO?.funny ?? 0 }}</div>
              <div class="stat-label">有趣数</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ friendInfo?.userVO?.cool ?? 0 }}</div>
              <div class="stat-label">酷炫数</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted} from 'vue';
import UseFriendInfo from "@/hooks/UseFriendInfo";
import { useRoute } from 'vue-router'

// 正确获取路由参数
const route = useRoute()
const userId = computed(() => route.query.userId)

// 使用朋友信息hook
const { friendInfo, getFriendInfo } = UseFriendInfo()

onMounted(()=>{
  if (userId.value) {
    getFriendInfo(userId.value)
  }
})

const initials = computed(() => {
  const name = friendInfo.value?.userVO?.name || ''
  if (!name) return 'U'
  
  // 处理多字名，提取首字母
  const cleanName = name.trim()
  if (cleanName.includes(' ')) {
    // 如果是复合姓名，取第一个和最后一个单词的首字母
    const words = cleanName.split(' ')
    return (words[0][0] + (words.length > 1 ? words[words.length - 1][0] : '')).toUpperCase()
  }
  return cleanName[0]?.toUpperCase() || 'U'
})
</script>

<style scoped>
.overview-container {
  display: flex;
  justify-content: center;
  padding: 10px;
  background: #f5f7fa;
  min-height: 60vh;
  overflow: hidden; /* 防止整个页面出现双重滚动条 */
}

.profile-card {
  width: 100%;
  max-width: 1200px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: all 0.3s ease;
  margin: 32px auto;
  max-height: calc(100vh - 350px); /* 设置最大高度 */
  overflow-y: auto; /* 启用垂直滚动 */
}

.card-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #eee;
}

.avatar-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6EE7B7, #3B82F6);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  backdrop-filter: blur(5px);
}

.header-info .title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 8px;
}

.header-info .subtitle {
  font-size: 16px;
  color: #6B7280;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 32px;
  margin-top: 32px;
}

.profile-section {
  background: #fafafa;
  border-radius: 12px;
  padding: 24px;
  transition: all 0.3s ease;
}

.profile-section:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.basic-info,
.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.label {
  font-size: 14px;
  color: #6B7280;
  margin-bottom: 4px;
}

.value {
  font-size: 16px;
  font-weight: 500;
  color: #111827;
}

.stats-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
}

.stat-card {
  background: linear-gradient(135deg, #6EE7B7, #3B82F6);
  color: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

@media (max-width: 768px) {
  .profile-card {
    padding: 20px;
    margin: 20px;
  }
  
  .card-header {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
  
  .basic-info,
  .contact-info {
    grid-template-columns: 1fr;
  }
  
  .stats-info {
    grid-template-columns: 1fr 1fr;
  }
  
  .stat-card {
    padding: 16px;
  }
  
  .stat-value {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .stats-info {
    grid-template-columns: 1fr;
  }
}
</style>