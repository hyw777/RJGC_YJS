<template>
  <div class="reviews-container">
    <div class="header-section">
      <h2 class="title">Reviews</h2>
      <div class="reviews-count">
        {{ friendInfo.reviewsList?.length || 0 }} reviews
      </div>
    </div>

    <div 
      v-if="!friendInfo.reviewsList || friendInfo.reviewsList.length === 0" 
      class="empty-state"
    >
      <div class="empty-icon">📝</div>
      <p>No reviews found</p>
    </div>

    <div v-else class="reviews-list">
      <div 
        v-for="(review, index) in friendInfo.reviewsList" 
        :key="index" 
        class="review-card"
      >
        <div class="review-header">
          <h3 class="business-name">{{ review.businessName }}</h3>
          <div class="review-date">{{ review.date }}</div>
        </div>
        
        <div class="review-rating">
          <el-rate 
            v-model="review.stars" 
            disabled 
            :colors="['#F56C6C', '#E6A23C', '#67C23A']"
            void-color="#DCDFE6"
          />
          <span class="stars-text">{{ review.stars }} stars</span>
        </div>
        
        <div class="review-content">
          <p class="review-text">{{ review.text }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import UseFriendInfo from "@/hooks/UseFriendInfo";
import { Star } from '@element-plus/icons-vue';

// 获取用户id
const props = defineProps<{
  userId?: string
}>()

const userId = computed(() => props.userId)

// 使用朋友信息hook - 修复响应式数据使用
const { friendInfo, getFriendInfo } = UseFriendInfo()

onMounted(() => {
  if (userId.value) {
    getFriendInfo(userId.value)
  }
})
</script>

<style scoped>
.reviews-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
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
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
}

.reviews-count {
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

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  padding: 20px;
  border: 1px solid #eee;
  transition: all 0.3s ease;
}

.review-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.business-name {
  color: #2D2E2F;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  flex: 1;
}

.review-date {
  color: #6E7072;
  font-size: 13px;
  margin-left: 15px;
  white-space: nowrap;
}

.review-rating {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  gap: 10px;
}

.stars-text {
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.review-content {
  margin-top: 10px;
}

.review-text {
  color: #2D2E2F;
  font-size: 15px;
  line-height: 1.6;
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

:deep(.el-rate__icon) {
  font-size: 16px !important;
  margin-right: 2px;
}

@media (max-width: 768px) {
  .reviews-container {
    padding: 15px;
    max-height: 500px; /* 移动端调整最大高度 */
  }
  .reviews-container {
    padding: 15px;
  }
  
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    margin-bottom: 20px;
  }
  
  .review-header {
    flex-direction: column;
    gap: 8px;
  }
  
  .business-name {
    font-size: 16px;
  }
  
  .review-date {
    margin-left: 0;
    font-size: 12px;
  }
  
  .review-rating {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .review-card {
    padding: 15px;
  }
}

.reviews-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  max-height: 600px; /* 设置最大高度 */
  overflow-y: auto;   /* 启用垂直滚动 */
}



</style>