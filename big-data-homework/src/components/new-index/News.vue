<template>
  <div class="container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>正在加载新闻...</p>
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="!news || news.length === 0" class="empty-state">
      <p>暂无新闻数据</p>
    </div>
    
    <!-- 新闻列表 -->
    <div v-else>
      <div class="message" v-for="(n, index) in news" :key="index">
        <div class="time">{{ n.time }}</div>
        <div class="content">{{ n.content }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { UseButtonStore } from "@/stores/UseButtonStore";
import { useNews } from "@/hooks/UseNews";

const { news, getNews } = useNews();
const buttonStore = UseButtonStore();
const loading = ref(true);

onMounted(async () => {
  buttonStore.setIndexButton(2);
  await getNews();
  loading.value = false;
});
</script>

<style scoped>

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
}

.spinner {
  width: 30px;
  height: 30px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
  font-size: 16px;
}

/* 移除 body 样式，因为这是组件样式 */
.container {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: stretch; /* 修改为stretch以充分利用宽度 */
  height: 400px;
  width: 100%;
  overflow-y: auto;
  border: 1px solid #ebebeb;
  border-radius: 8px; /* 添加圆角 */
  padding: 20px;
  background-color: #fafafa; /* 背景色调整 */
}

.message {
  background-color: #ffffff;
  padding: 20px; /* 增加内边距 */
  border-radius: 8px; /* 统一圆角 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 增强阴影效果 */
  margin-bottom: 15px; /* 减小间距 */
  transition: transform 0.2s ease, box-shadow 0.2s ease; /* 添加过渡动画 */
}

.message:hover {
  transform: translateY(-2px); /* 悬停效果 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.time {
  font-size: 13px;
  color: #666666;
  margin-bottom: 8px;
  font-weight: 500; /* 稍微加粗 */
}

.content {
  font-size: 15px;
  color: #333333;
  line-height: 1.6; /* 增加行高提高可读性 */
}
</style>