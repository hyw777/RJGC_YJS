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
      <!-- 商户图片展示区域 - 轮播图 -->
      <div class="image-gallery">
        <!-- 当有图片时显示轮播图 -->
        <div v-if="baseInfo?.imageList && baseInfo.imageList.length > 0">
          <div class="carousel-container">
            <!-- 主图轮播区域 -->
            <div class="main-carousel">
              <div
                class="carousel-wrapper"
                :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
              >
                <div
                  v-for="(image, index) in baseInfo.imageList"
                  :key="index"
                  class="carousel-slide"
                >
                  <div class="main-image-container">
                    <img
                      class="main-image"
                      :src="getImagePath(image)"
                      :alt="`${baseInfo?.name} - 图片${index + 1}`"
                      v-if="image"
                    />
                    <div class="image-placeholder" v-else>
                      <el-icon size="48"><Picture /></el-icon>
                      <span>暂无图片</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 左右切换按钮 -->
            <button
              class="carousel-btn prev-btn"
              @click="prevSlide"
              v-if="baseInfo.imageList && baseInfo.imageList.length > 1"
            >
              <el-icon size="24"><ArrowLeft /></el-icon>
            </button>
            <button
              class="carousel-btn next-btn"
              @click="nextSlide"
              v-if="baseInfo.imageList && baseInfo.imageList.length > 1"
            >
              <el-icon size="24"><ArrowRight /></el-icon>
            </button>

            <!-- 指示器 -->
            <div
              class="carousel-indicators"
              v-if="baseInfo.imageList && baseInfo.imageList.length > 1"
            >
              <span
                v-for="(image, index) in baseInfo.imageList"
                :key="index"
                :class="['indicator-dot', { active: currentSlide === index }]"
                @click="goToSlide(index)"
              ></span>
            </div>

            <el-button
              @click="jump"
              class="view-all-btn"
              type="primary"
              plain
              v-if="baseInfo.imageList && baseInfo.imageList.length > 0"
            >
              查看全部 {{ baseInfo.imageList.length }} 张图片
            </el-button>
          </div>
        </div>

        <!-- 当无图片时显示提示 -->
        <div v-else class="no-images-tip">
          <el-icon size="48" color="#909399"><Picture /></el-icon>
          <p>抱歉，该商户暂时未上传图片</p>
        </div>
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
                <span
                  class="value clickable"
                  @click="showBusinessHours = !showBusinessHours"
                >
                  {{ showBusinessHours ? "收起详细时间" : "点击查看详细时间" }}
                </span>
              </div>
            </div>

            <!-- 在 info-grid 后添加详细营业时间展示 -->
            <div v-if="showBusinessHours" class="business-hours-detail">
              <div
                v-for="(time, day) in formatBusinessHours(baseInfo?.hours)"
                :key="day"
                class="hours-item"
              >
                <span class="day">{{ formatDay(day) }}:</span>
                <span class="time" :class="{ closed: time === '0:0-0:0' }">
                  {{ time === "0:0-0:0" ? "全天" : formatTimeRange(time) }}
                </span>
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
          <div
            class="qa-item"
            v-for="(attr, index) in getImportantAttributes()"
            :key="index"
          >
            <div class="question">
              <el-icon color="#409eff"><QuestionFilled /></el-icon>
              <span>{{ formatAttributeName(attr.key) }}</span>
            </div>
            <div class="answer">
              <el-icon :color="getIconColor(attr.value)" size="16">
                <CircleCheck v-if="attr.value === 'true'" />
                <CircleClose v-else-if="attr.value === 'false'" />
                <InfoFilled v-else />
              </el-icon>
              <span class="answer-text">{{
                formatAttributeValue(attr.value)
              }}</span>
            </div>
          </div>
        </div>

        <!-- 分割线 -->
        <div class="divider-vertical"></div>

        <!-- 评论部分 -->
        <div class="reviews-section">
          <div class="reviews-header">
            <h2 class="section-title">用户评价 ({{ (baseInfo?.reviewVOList || []).length }})</h2>
          </div>

          <!-- 评论列表 -->
          <div class="reviews-list" v-if="(baseInfo?.reviewVOList || []).length > 0">
            <div
              class="review-item"
              v-for="(review, index) in (baseInfo?.reviewVOList || [])"
              :key="index"
            >
              <div class="review-header">
                <div class="user-info">
                  <el-avatar :size="32">{{
                    (review?.userName || '').charAt(0) || "U"
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
import { onMounted, ref, watch } from "vue";
import { useBaseInfo } from "@/hooks/UseBaseInfo";
import { UseButtonStore } from "@/stores/UseButtonStore";
import { router } from "@/router";
import { UseImageListStore } from "@/stores/UseImageListStore";
import { useRoute } from 'vue-router';
import {
  Location,
  Clock,
  Collection,
  QuestionFilled,
  CircleCheck,
  CircleClose,
  OfficeBuilding,
  Loading,
  Picture,
  ArrowLeft,
  ArrowRight,
  InfoFilled
} from "@element-plus/icons-vue";

// 定义 props
const props = defineProps<{
  businessId?: string
}>()

// 使用按钮store
let buttonStore = UseButtonStore();

// 使用baseInfohook
let { baseInfo, getBaseInfo } = useBaseInfo(); // 修复：不再使用 toRefs 包装自定义 hook 返回的对象

let imageListStore = UseImageListStore();

// 添加 loading 状态
const loading = ref(true);

// 轮播图当前页码
const currentSlide = ref(0);

// 添加响应式变量
const showBusinessHours = ref(false);

// 添加格式化营业时间的函数
// 定义星期顺序映射
const dayOrder = {
  Monday: 0,
  Tuesday: 1,
  Wednesday: 2,
  Thursday: 3,
  Friday: 4,
  Saturday: 5,
  Sunday: 6,
};

const formatBusinessHours = (hoursString) => {
  if (!hoursString) return {};

  try {
    const hoursObj = JSON.parse(hoursString);

    // 将对象转换为数组并按星期顺序排序
    const sortedHours = Object.entries(hoursObj)
      .sort(([dayA], [dayB]) => dayOrder[dayA] - dayOrder[dayB])
      .reduce((obj, [key, value]) => {
        obj[key] = value;
        return obj;
      }, {});

    return sortedHours;
  } catch (e) {
    return {};
  }
};

// 格式化星期显示
const formatDay = (day) => {
  const dayMap = {
    Monday: "周一",
    Tuesday: "周二",
    Wednesday: "周三",
    Thursday: "周四",
    Friday: "周五",
    Saturday: "周六",
    Sunday: "周日",
  };
  return dayMap[day] || day;
};

// 格式化时间范围显示
const formatTimeRange = (timeRange) => {
  if (!timeRange) return "";
  return timeRange.replace(/(\d+):0/g, "$1:00").replace("-", " - ");
};

// 定义重要属性列表
const importantAttributes = [
  "WiFi",
  "Alcohol",
  "OutdoorSeating",
  "RestaurantsDelivery",
  "RestaurantsTakeOut",
  "BusinessAcceptsCreditCards",
  "DogsAllowed",
  "GoodForKids",
];

// 获取重要属性的函数
const getImportantAttributes = () => {
  if (!baseInfo.value?.attributes) return [];

  try {
    const attrs = JSON.parse(baseInfo.value.attributes);
    const resultAttrs = [];

    // 遍历重要属性列表
    importantAttributes.forEach((key) => {
      if (attrs.hasOwnProperty(key)) {
        resultAttrs.push({
          key,
          value: attrs[key],
        });
      } else {
        // 如果属性不存在，添加一个默认值为未知的项
        resultAttrs.push({
          key,
          value: "unknown",
        });
      }
    });

    return resultAttrs;
  } catch (e) {
    return [];
  }
};

// 添加颜色映射函数
const getIconColor = (value) => {
  if (value === "true") return "#67c23a";
  if (value === "false") return "#f56c6c";
  return "#409eff";
};

const formatAttributeName = (name) => {
  const nameMap = {
    WiFi: "是否有WiFi",
    Alcohol: "是否提供酒精饮品",
    OutdoorSeating: "是否有户外座位",
    RestaurantsDelivery: "是否提供外卖",
    RestaurantsTakeOut: "是否提供外带",
    BusinessAcceptsCreditCards: "是否接受信用卡",
    GoodForKids: "是否适合儿童",
    HasTV: "是否有电视",
    NoiseLevel: "噪音等级",
    RestaurantsReservations: "是否需要预约",
    WheelchairAccessible: "是否无障碍通道",
    DogsAllowed: "是否允许宠物入内",
    HappyHour: "是否有欢乐时光",
    CoatCheck: "是否提供衣帽间",
    Smoking: "是否允许吸烟",
  };
  return nameMap[name] || name;
};

// 更新格式化函数以处理未知值
const formatAttributeValue = (value) => {
  if (value === "true") return "是";
  if (value === "false") return "否";
  if (value === "none") return "无";
  if (value === "unknown") return "未知";

  if (value === "average") return "一般";
  if (value === "quiet") return "安静";
  if (value === "loud") return "吵闹";
  if (value === "very_loud") return "非常吵闹";

  // 处理数组类型值
  if (typeof value === "object" && value !== null) {
    const obj = JSON.parse(value);
    const keys = Object.keys(obj);
    const values = Object.values(obj);

    // 过滤出为 true 的项
    const trueKeys = keys.filter((key, index) => values[index] === true);

    if (trueKeys.length > 0) {
      return trueKeys.join("、");
    }

    return "无";
  }

  // 处理价格范围
  if (value.startsWith("PriceRange")) {
    const rangeMap = {
      "1": "¥",
      "2": "¥¥",
      "3": "¥¥¥",
      "4": "¥¥¥¥",
    };
    return rangeMap[value] || value;
  }

  return value;
};

// 监听图片列表变化，重置轮播图
watch(
  () => baseInfo.value?.imageList,
  () => {
    currentSlide.value = 0;
  }
);

// 上一张图片
const prevSlide = () => {
  if (baseInfo.value?.imageList && baseInfo.value.imageList.length > 0) {
    currentSlide.value =
      (currentSlide.value - 1 + baseInfo.value.imageList.length) %
      baseInfo.value.imageList.length;
  }
};

// 下一张图片
const nextSlide = () => {
  if (baseInfo.value?.imageList && baseInfo.value.imageList.length > 0) {
    currentSlide.value =
      (currentSlide.value + 1) % baseInfo.value.imageList.length;
  }
};

// 跳转到指定图片
const goToSlide = (index: number) => {
  currentSlide.value = index;
};

// 获取路由参数
const route = useRoute();

// 抽取加载数据的函数，方便复用
const loadData = async (businessId: string) => {
  loading.value = true;
  try {
    // 使用businessId获取商户信息
    await getBaseInfo(businessId); // 修复：直接调用函数而不是 .value()
    
    // 数据加载成功后的处理
    console.log("商户信息加载成功：", baseInfo.value);
    
    // 确保必要的字段存在
    if (baseInfo.value) {
      if (!baseInfo.value.reviewVOList || !Array.isArray(baseInfo.value.reviewVOList)) {
        baseInfo.value.reviewVOList = [];
      }
      if (!baseInfo.value.imageList || !Array.isArray(baseInfo.value.imageList)) {
        baseInfo.value.imageList = [];
      }
    }
  } catch (error) {
    // 数据加载失败的处理
    console.error("商户信息加载失败：", error);
  } finally {
    // 无论成功还是失败都要隐藏加载状态
    setTimeout(() => {
      loading.value = false;
    }, 100); // 延迟100毫秒确保DOM更新
  }
};

// 监听路由变化，在切换标签时重新加载数据
watch(
  () => props.businessId || route.query.businessId,
  (newBusinessId) => {
    if (newBusinessId) {
      loadData(newBusinessId as string);
    }
  },
  { immediate: true }
);

onMounted(() => {
  buttonStore.setBossButton(0);
  
  // 从props或路由参数中获取businessId
  const businessId = props.businessId || route.query.businessId as string;
  
  // 使用businessId获取商户信息
  if (businessId) {
    loadData(businessId);
  }
});

// 图片路径处理函数
const getImagePath = (file: string) => {
  if (!file) {
    console.log("图片文件名为空");
    return "";
  }

  const isHttpUrl = file.includes("http");

  if (isHttpUrl) {
    return file;
  } else {
    const fullPath = `http://localhost:3000/images/${file}.jpg`;
    return fullPath;
  }
};

// 跳转到图片展示页面
function jump() {
  imageListStore.setImageList(baseInfo.value.imageList);
  router.push("/imageDisplay");
}
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

/* 图片展示区域 - 轮播图样式 */
.image-gallery {
  background: white;
  border-radius: 12px;
  padding: 16px; /* 减小内边距 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px; /* 减小外边距 */
  position: relative;
}

.carousel-container {
  position: relative;
  margin-bottom: 16px; /* 减小外边距 */
}

.main-carousel {
  width: 100%;
  overflow: hidden;
  border-radius: 8px;
  position: relative;
}

.carousel-wrapper {
  display: flex;
  transition: transform 0.3s ease;
  width: 100%;
}

.carousel-slide {
  min-width: 100%;
  transition: transform 0.3s ease;
}

.main-image-container {
  width: 70%; /* 或者使用固定宽度如 600px */
  height: 600px; /* 建议设置为具体数值而非百分比 */
  background-color: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto; /* 添加这行使容器本身在父元素中水平居中 */
}

.main-image {
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  object-fit: cover;
}

.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #909399;
  gap: 8px;
}

/* 轮播图控制按钮 */
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.5);
  border: none;
  border-radius: 50%;
  width: 32px; /* 减小按钮尺寸 */
  height: 32px; /* 减小按钮尺寸 */
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
  z-index: 10;
}

.carousel-btn:hover {
  background: rgba(0, 0, 0, 0.7);
}

.prev-btn {
  left: 12px; /* 调整位置 */
}

.next-btn {
  right: 12px; /* 调整位置 */
}

/* 指示器 */
.carousel-indicators {
  display: flex;
  justify-content: center;
  gap: 6px; /* 减小间隙 */
  margin-top: 12px; /* 减小外边距 */
}

.indicator-dot {
  width: 8px; /* 减小指示器尺寸 */
  height: 8px; /* 减小指示器尺寸 */
  border-radius: 50%;
  background: #dcdfe6;
  cursor: pointer;
  transition: background 0.3s;
}

.indicator-dot.active {
  background: #409eff;
}

.view-all-btn {
  width: 100%;
  height: 36px; /* 减小按钮高度 */
  font-size: 14px; /* 调整字体大小 */
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

/* 添加样式 */
.clickable {
  cursor: pointer;
  color: #409eff;
}

.business-hours-detail {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.hours-item {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
}

.day {
  font-weight: 500;
  color: #303133;
}

.time {
  color: #67c23a;
  font-weight: 500;
}

.time.closed {
  color: #f56c6c;
}

/* 针对不同类型的值添加特殊样式 */
.answer-text.is-yes {
  color: #67c23a;
}

.answer-text.is-no {
  color: #f56c6c;
}

.answer-text.is-price {
  font-weight: 500;
  color: #409eff;
}

.answer-text.is-unknown {
  color: #909399;
  font-style: italic;
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

  .qa-item {
    margin-bottom: 12px;
  }

  .question {
    font-size: 14px;
  }

  .answer-text {
    font-size: 13px;
  }
}

.no-images-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  color: #909399;
}

.no-images-tip p {
  margin-top: 16px;
  font-size: 16px;
}
</style>