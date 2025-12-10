<template>
  <div class="container">
    <div class="header">
      <h2>评价管理</h2>
      <div class="search-box">
        <div class="date-selector">
          <el-date-picker
            v-model="value2"
            type="daterange"
            size="large"
            value-format="YYYY-MM-DD"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :shortcuts="shortcuts"
            :size="size"
          />
          <el-button type="primary" @click="search" style="margin-left: 20px">
            <el-icon><Search /></el-icon>搜索
          </el-button>
        </div>
      </div>
    </div>

    <div class="review-list">
      <div 
        class="review-card"  
        v-for="(review,index) in data.records" 
        :key="index" 
        v-show="display"
      >
        <div class="review-header">
          <div class="user-info">
            <el-avatar :icon="User" size="small" style="background-color: #58B4FF;" />
            <span class="user-name">{{ review.userName }}</span>
          </div>
          
          <div class="business-info">
            <el-icon style="color: #e19898;"><Shop /></el-icon>
            <span class="business-name">{{ review.businessName }}</span>
          </div>
          
          <div class="review-meta">
            <el-rate v-model="review.stars" disabled />
            <span class="time">{{ formatDate(review.date) }}</span>
          </div>
        </div>
        
        <div class="review-content">
          <p class="review-text">{{ review.text }}</p>
        </div>
        
        <div class="review-actions">
          <el-button 
            v-if="!isReviewHidden(review.reviewId)" 
            type="warning" 
            plain 
            @click="warning(review.reviewId, review)"
            size="small"
          >
            <el-icon><Warning /></el-icon>警告
          </el-button>
          <el-tag v-else type="info">已警告</el-tag>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty v-if="data.records && data.records.length === 0" description="暂无评价数据" />
    </div>
    
    <div class="pagination-container">
      <el-pagination 
        background 
        v-model:current-page="findReviewDTO.pageNum" 
        layout="total, prev, pager, next, jumper" 
        :total="data.total"
        :page-size="findReviewDTO.pageSize"
        @current-change="search"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import axios from "axios";
import { User, Shop, Warning, Search } from '@element-plus/icons-vue';

const size = ref<'default' | 'large' | 'small'>('default')
const display = ref(true);
const value2 = ref('')
const findReviewDTO = ref({
  pageSize: 5,
  pageNum: 1,
  begin: '',
  end: ''
})
let data = ref({
  records: [],
  total: 0
});

const search = () => {
  findReviewDTO.value.begin = value2.value[0];
  findReviewDTO.value.end = value2.value[1];
  axios.post('/api/reviewManage/getAll', findReviewDTO.value)
    .then((response) => {
      data.value = response.data.data;
    })
    .catch((error) => {
      console.log(error);
    })
};

onMounted(() => {
  search();
  if (storedHiddenIds) {
    hiddenReviewIds.value = new Set(JSON.parse(storedHiddenIds));
  }
});

// 使用 ref 创建一个响应式集合来存储已隐藏的 reviewId
// 从 localStorage 获取已隐藏的 reviewId
const storedHiddenIds = localStorage.getItem('hiddenReviewIds');
const hiddenReviewIds = ref(storedHiddenIds ? new Set(JSON.parse(storedHiddenIds)) : new Set());

// 检查 reviewId 是否在 hiddenReviewIds 中
function isReviewHidden(reviewId: number) {
  return hiddenReviewIds.value.has(reviewId);
}

// 隐藏警告并将 reviewId 添加到 hiddenReviewIds 中，并且发出警告
function warning(reviewId: number, reviewVO) {
  axios.post('/api/reviewManage/warning', reviewVO).
    then((response) => {
      hiddenReviewIds.value.add(reviewId);
      // 更新 localStorage
      localStorage.setItem('hiddenReviewIds', JSON.stringify(Array.from(hiddenReviewIds.value)));
    }).catch((error) => {
      console.log(error);
    })
}

// 格式化日期的函数
function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]
</script>

<style scoped>
.container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100%;
}

.header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.header h2 {
  margin: 0 0 20px 0;
  color: #303133;
}

.search-box {
  display: flex;
  justify-content: center;
}

.date-selector {
  display: flex;
  align-items: center;
}

.review-list {
  margin-bottom: 20px;
}

.review-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.review-card:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.user-info, .business-info, .review-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-name {
  font-weight: bold;
  color: #303133;
}

.business-name {
  color: #606266;
}

.time {
  color: #909399;
  font-size: 12px;
  margin-left: 10px;
}

.review-content {
  margin-bottom: 15px;
}

.review-text {
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

.review-actions {
  text-align: right;
}

.pagination-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .review-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .review-meta {
    width: 100%;
    justify-content: space-between;
  }
  
  .search-box {
    flex-direction: column;
    gap: 10px;
  }
  
  .date-selector {
    flex-direction: column;
    width: 100%;
  }
}
</style>