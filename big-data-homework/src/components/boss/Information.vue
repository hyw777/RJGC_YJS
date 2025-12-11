<template>
  <div class="merchant-detail">
    <!-- 加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <!-- 使用 el-icon 中的 Loading 组件 -->
        <el-icon class="is-loading" size="48"><Loading /></el-icon>
        <p>加载中...</p>
      </div>
    </div>

    <div v-else>
      <!-- 商户图片展示区域 -->
      <div class="image-gallery">
        <div class="image-container">
          <div class="main-images">
            <img
              v-for="(image, index) in baseInfo?.imageList?.slice(0, 3)"
              :key="index"
              class="main-image"
              :src="getImagePath(image)"
              :alt="`${baseInfo?.name} - 图片${index + 1}`"
              v-if="image !== undefined && image !== null && image !== '' && baseInfo?.imageList"
            />
          </div>
        </div>

        <el-button
          @click="jump"
          class="view-all-btn"
          type="primary"
          plain
          v-if="baseInfo?.imageList && baseInfo?.imageList.length > 0"
        >
          查看全部 {{ baseInfo?.imageList.length }} 张图片
        </el-button>
      </div>

      <!-- 商户信息区域 -->
      <div class="info-section">
        <div class="info-header">
          <div class="header-content">
            <div class="business-header">
              <h1 class="business-name">{{ baseInfo?.name || '未知商户' }}</h1>
            </div>
            <div class="business-address">
              <el-icon><Location /></el-icon>
              <span>{{ baseInfo?.city || '' }}, {{ baseInfo?.state || '' }}</span>
            </div>
          </div>
        </div>

        <!-- 评分信息 -->
        <div class="rating-section" v-if="baseInfo?.stars">
          <div class="rating-item">
            <span class="rating-label">用户评分</span>
            <el-rate v-model="baseInfo.stars" disabled size="small" />
            <span class="rating-value">{{ baseInfo.stars }}</span>
            <span class="review-count" v-if="baseInfo?.reviewCount">({{ baseInfo.reviewCount }} 条评论)</span>
          </div>
        </div>

        <!-- 基本信息 -->
        <div class="basic-info">
          <div class="info-grid">
            <div class="info-card">
              <el-icon><OfficeBuilding /></el-icon>
              <div class="info-content">
                <span class="label">营业状态</span>
                <span class="value">-</span>
              </div>
            </div>

            <div class="info-card">
              <el-icon><Clock /></el-icon>
              <div class="info-content">
                <span class="label">营业时间</span>
                <span class="value">{{ baseInfo?.hours || '未知' }}</span>
              </div>
            </div>

            <div class="info-card">
              <el-icon><Collection /></el-icon>
              <div class="info-content">
                <span class="label">分类</span>
                <span class="value">{{ baseInfo?.categories || '未分类' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Q&A 和评论区域 -->
      <div class="content-section">
        <!-- Q&A 部分 -->
        <div class="qa-section">
          <h2 class="section-title">Q&A</h2>
          <div class="qa-item">
            <div class="question">
              <el-icon color="#409eff"><QuestionFilled /></el-icon>
              <span>是否适合儿童</span>
            </div>
            <div class="answer">
              <el-icon :color="baseInfo?.goodForKids === 'true' ? '#67c23a' : '#f56c6c'" size="16">
                <CircleCheck v-if="baseInfo?.goodForKids === 'true'" />
                <CircleClose v-else />
              </el-icon>
              <span class="answer-text">{{ baseInfo?.goodForKids === 'true' ? '是' : '否' }}</span>
            </div>
          </div>
          
          <div class="qa-item">
            <div class="question">
              <el-icon color="#409eff"><QuestionFilled /></el-icon>
              <span>是否接受信用卡</span>
            </div>
            <div class="answer">
              <el-icon :color="baseInfo?.businessAcceptsCreditcards === 'true' ? '#67c23a' : '#f56c6c'" size="16">
                <CircleCheck v-if="baseInfo?.businessAcceptsCreditcards === 'true'" />
                <CircleClose v-else />
              </el-icon>
              <span class="answer-text">{{ baseInfo?.businessAcceptsCreditcards === 'true' ? '是' : '否' }}</span>
            </div>
          </div>
        </div>

        <!-- 分割线 -->
        <div class="divider-vertical"></div>

        <!-- 评论部分 -->
        <div class="reviews-section">
          <div class="reviews-header">
            <h2 class="section-title">用户评价 ({{ baseInfo?.reviewVOList?.length || 0 }})</h2>
          </div>

          <!-- 评论列表 -->
          <div class="reviews-list" v-if="baseInfo?.reviewVOList && baseInfo?.reviewVOList.length > 0">
            <div
              class="review-item"
              v-for="(review, index) in baseInfo?.reviewVOList"
              :key="index"
            >
              <div class="review-header">
                <div class="user-info">
                  <el-avatar :size="32">{{
                    review?.userName?.charAt(0) || "U"
                  }}</el-avatar>
                  <div class="user-details">
                    <span class="username">{{ review?.userName || '匿名用户' }}</span>
                    <span class="review-date">{{ review?.date || '未知日期' }}</span>
                  </div>
                </div>
                <el-rate
                  :model-value="review?.stars"
                  disabled
                  size="small"
                  class="review-rating"
                  v-if="review?.stars"
                />
              </div>

              <div class="review-content">
                {{ review?.text || '无评论内容' }}
              </div>
            </div>
          </div>
          <div v-else class="no-reviews">
            暂无用户评价
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, toRefs } from "vue";
import { useBaseInfo } from "@/hooks/UseBaseInfo";
import { UseButtonStore } from "@/stores/UseButtonStore";
import { router } from "@/router";
import { UseImageListStore } from "@/stores/UseImageListStore";
import {
  Location,
  Clock,
  Collection,
  QuestionFilled,
  CircleCheck,
  CircleClose,
  OfficeBuilding,
  Loading
} from "@element-plus/icons-vue";

// 使用按钮store
let buttonStore = UseButtonStore();

// 使用baseInfohook
let { baseInfo, getBaseInfo } = toRefs(useBaseInfo());

let imageListStore = UseImageListStore();

// 添加 loading 状态
const loading = ref(true);

function jump() {
  // 确保imageList存在再执行跳转
  if (baseInfo.value?.imageList) {
    imageListStore.setImageList(baseInfo.value.imageList);
    router.push("/imageDisplay");
  }
}

const getImagePath = (file: string) => {
  // 添加检查确保file存在且不为空
  if (!file || file === '') {
    console.log("图片文件名为空");
    return "";
  }
 
  const isHttpUrl = file.includes("http");

  if (isHttpUrl) {
    return file;
  } else {
    const fullPath = `http://localhost:3000/images/${file}.jpg`;
    console.log("图片文件名:", fullPath);
    return fullPath;
  }
};

onMounted(() => {
  buttonStore.setBossButton(0);
  getBaseInfo.value()
    .then(() => {
      // 数据加载成功后的处理
      console.log("商户信息加载成功：", baseInfo.value);
    })
    .catch((error) => {
      // 数据加载失败的处理
      console.error("商户信息加载失败：", error);
    })
    .finally(() => {
      // 无论成功还是失败都要隐藏加载状态
      setTimeout(() => {
        loading.value = false;
      }, 100); // 延迟100毫秒确保DOM更新
    });
});
</script>

<style scoped>
/* 添加加载样式 */

/* 添加旋转动画样式 */
.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  text-align: center;
}

.loading-spinner p {
  margin-top: 16px;
  color: #606266;
  font-size: 16px;
}

/* 添加无评论样式 */
.no-reviews {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  font-size: 14px;
}

.merchant-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 图片展示区域样式 */
.image-gallery {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  position: relative;
}

.image-container {
  width: 100%;
  margin-bottom: 16px;
}

.main-images {
  display: flex;
  gap: 10px;
}

.main-image {
  flex: 1;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.view-all-btn {
  width: 100%;
  height: 36px;
  font-size: 14px;
}

/* 信息区域 */
.info-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.business-name {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  color: #303133;
}

.business-address {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
  font-size: 16px;
}

/* 评分区域 */
.rating-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e4e7ed;
}

.rating-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.rating-item:last-child {
  margin-bottom: 0;
}

.rating-label {
  font-weight: 500;
  color: #606266;
  min-width: 80px;
}

.rating-value {
  font-weight: 600;
  color: #303133;
}

.review-count {
  color: #909399;
  font-size: 14px;
}

/* 基本信息 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.info-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.info-card .el-icon {
  font-size: 24px;
  color: #409eff;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-content .label {
  font-size: 14px;
  color: #909399;
}

.info-content .value {
  font-weight: 500;
  color: #303133;
}

/* 内容区域 */
.content-section {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.qa-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

/* Q&A区域 */
.qa-item {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.qa-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.question {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.answer {
  display: flex;
  align-items: center;
  gap: 8px;
  padding-left: 24px;
  position: relative;
}

.answer::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 1px;
  height: 16px;
  background-color: #e4e7ed;
}

.answer-text {
  color: #606266;
  font-size: 14px;
}

/* 分割线 */
.divider-vertical {
  width: 1px;
  background-color: #e4e7ed;
}

/* 评论区域 */
.reviews-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  max-height: 800px;
  overflow-y: auto;
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid #e4e7ed;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 500;
  color: #303133;
}

.review-date {
  color: #909399;
  font-size: 14px;
}

.review-rating {
  margin-top: 8px;
}

.review-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 16px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .content-section {
    flex-direction: column;
  }

  .divider-vertical {
    width: 100%;
    height: 1px;
  }

  .info-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .business-name {
    font-size: 24px;
  }
}

@media (max-width: 768px) {
  .merchant-detail {
    padding: 12px;
  }

  .image-gallery,
  .info-section,
  .qa-section,
  .reviews-section {
    padding: 12px;
  }

  .main-images {
    flex-direction: column;
  }

  .main-image {
    height: 150px;
  }

  .business-name {
    font-size: 20px;
  }
}
</style>