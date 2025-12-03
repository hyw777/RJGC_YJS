<template>
  <div class="reviews-container">
    <div class="header-section">
      <h2 class="title">Reviews</h2>
      <div class="reviews-count">
        {{ userInfo.reviewsList?.length || 0 }} reviews
      </div>
    </div>

    <div 
      v-if="!userInfo.reviewsList || userInfo.reviewsList.length === 0" 
      class="empty-state"
    >
      <div class="empty-icon">📝</div>
      <p>No reviews found</p>
    </div>

    <div v-else class="reviews-list">
      <div 
        v-for="(reviews, index) in userInfo.reviewsList" 
        :key="index" 
        class="review-card"
      >
        <div class="review-header">
          <h3 class="business-name">{{ reviews.businessName }}</h3>
          <div class="review-actions">
            <div class="review-date">{{ reviews.date }}</div>
            <el-icon size="20" class="delete" color="#E00707" @click="dialogVisible = true">
              <Delete />
            </el-icon>
          </div>
        </div>
        
        <div class="review-rating">
          <el-rate 
            v-model="reviews.stars" 
            disabled 
            :colors="['#F56C6C', '#E6A23C', '#67C23A']"
            void-color="#DCDFE6"
          />
          <span class="stars-text">{{ reviews.stars }} stars</span>
        </div>
        
        <div class="review-content">
          <p class="review-text">{{ reviews.text }}</p>
        </div>
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="Warning!"
      width="500"
    >
      <span>Are you sure to delete this reviews?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="dialogVisible = false; deleteReviews(currentReviewId)">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, toRefs } from "vue";
import UseReviewsDelete from "@/hooks/UseReviewsDelete";
import UseUserInfo from "@/hooks/UseUserInfo";
import { Delete } from '@element-plus/icons-vue';

// 使用用户信息hook
let { userInfo, getUserInfo } = toRefs(UseUserInfo());

// 使用删除评论hooks
let reviewsDelete = UseReviewsDelete()
let { deleteReviews } = reviewsDelete

// 对话框展示
let dialogVisible = ref(false)
let currentReviewId = ref('')

// 打开删除对话框时记录当前评论ID
const openDeleteDialog = (reviewId: string) => {
  currentReviewId.value = reviewId
  dialogVisible.value = true
}

onMounted(() => {
  getUserInfo.value()
})
</script>

<style scoped>
.reviews-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  max-height: 600px;
  overflow-y: auto;
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
  width: 95%; /* 使用父容器的全宽 */
  min-width: 250px; /* 设置最小宽度 */
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

.review-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.review-date {
  color: #6E7072;
  font-size: 13px;
  white-space: nowrap;
}

.delete {
  cursor: pointer;
  transition: transform 0.2s ease;
}

.delete:hover {
  transform: scale(1.1);
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

.dialog-footer {
  text-align: right;
}

@media (max-width: 768px) {
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
  
  .review-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .business-name {
    font-size: 16px;
  }
  
  .review-date {
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
</style>