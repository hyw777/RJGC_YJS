<template>
  <div class="merchant-detail">
    <!-- 商户图片展示区域 - 轮播图 -->
    <div class="image-gallery">
      <div class="carousel-container">
        <!-- 主图轮播区域 -->
        <div class="main-carousel">
          <div
            class="carousel-wrapper"
            :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
          >
            <div
              v-for="(image, index) in result.imageList"
              :key="index"
              class="carousel-slide"
            >
              <div class="main-image-container">
                <img
                  class="main-image"
                  :src="getImagePath(image)"
                  :alt="`${result.name} - 图片${index + 1}`"
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
          v-if="result.imageList && result.imageList.length > 1"
        >
          <el-icon size="24"><ArrowLeft /></el-icon>
        </button>
        <button
          class="carousel-btn next-btn"
          @click="nextSlide"
          v-if="result.imageList && result.imageList.length > 1"
        >
          <el-icon size="24"><ArrowRight /></el-icon>
        </button>

        <!-- 指示器 -->
        <div
          class="carousel-indicators"
          v-if="result.imageList && result.imageList.length > 1"
        >
          <span
            v-for="(image, index) in result.imageList"
            :key="index"
            :class="['indicator-dot', { active: currentSlide === index }]"
            @click="goToSlide(index)"
          ></span>
        </div>
      </div>

      <el-button
        @click="jump"
        class="view-all-btn"
        type="primary"
        plain
        v-if="result.imageList && result.imageList.length > 0"
      >
        查看全部 {{ result.imageList.length }} 张图片
      </el-button>
    </div>

    <!-- 商户信息区域 -->
    <div class="info-section">
      <div class="info-header">
        <div class="header-content">
          <div class="business-header">
            <h1 class="business-name">{{ result.name }}</h1>
            <!-- 添加多行弹幕式小贴士 -->
            <div
              class="tips-barrage"
              v-if="result.tipList && result.tipList.length > 0"
            >
              <div
                v-for="(tip, index) in result.tipList"
                :key="index"
                class="barrage-item"
                :style="{
                  animationDelay: `${index * 2}s`,
                  top: `${(index % 3) * 30}px`,
                  zIndex: `${index + 1}`,
                  opacity: '0.8',
                }"
              >
                <el-icon color="#409eff" size="16"><Lightning /></el-icon>
                <span class="tip-text">{{ tip.text }}</span>
              </div>
            </div>
          </div>
          <div class="business-address">
            <el-icon><Location /></el-icon>
            <span
              >{{ result.address }}, {{ result.city }}, {{ result.state }}
              {{ result.postalCode }}</span
            >
          </div>
        </div>
        <el-button
          @click="toggleCollect"
          :type="isCollected ? 'danger' : 'default'"
          :icon="isCollected ? StarFilled : Star"
          circle
          size="large"
          class="collect-btn"
        >
        </el-button>
      </div>

      <!-- 评分信息 -->
      <div class="rating-section">
        <div class="rating-item">
          <span class="rating-label">用户评分</span>
          <el-rate v-model="result.stars" disabled size="small" />
          <span class="rating-value">{{ result.stars }}</span>
          <span class="review-count">({{ result.reviewCount }} 条评论)</span>
        </div>

        <div class="rating-item ai-rating">
          <span class="rating-label ai-label">AI评分</span>
          <el-rate v-model="result.stars" disabled size="small" />
          <span class="rating-value">{{ result.stars }}</span>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="basic-info">
        <div class="info-grid">
          <div class="info-card">
            <el-icon><OfficeBuilding /></el-icon>
            <div class="info-content">
              <span class="label">营业状态</span>
              <span class="value" :class="{ open: result.isOpen === 1 }">
                {{ result.isOpen === 1 ? "营业中" : "已打烊" }}
              </span>
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
              v-for="(time, day) in formatBusinessHours(result.hours)"
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
              <span class="value category-tags">
                <el-tag
                  v-for="(category, index) in parseCategories(
                    result.categories
                  )"
                  :key="index"
                  size="small"
                  type="info"
                  class="category-tag"
                >
                  {{ category }}
                </el-tag>
              </span>
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
          <h2 class="section-title">用户评价 ({{ result.reviewCount }})</h2>
          <el-button @click="switchStatus" type="primary" :icon="EditPen" plain>
            写评价
          </el-button>
        </div>

        <!-- 评价统计 -->
        <div class="review-stats">
          <div class="stats-item">
            <span class="stats-label">总体评分</span>
            <div class="stats-content">
              <span class="stats-score">{{ result.stars }}</span>
              <el-rate v-model="result.stars" disabled size="small" />
            </div>
          </div>
          <div class="stats-item">
            <span class="stats-label">评论总数</span>
            <span class="stats-value">{{ result.reviewCount }}</span>
          </div>
        </div>

        <!-- 评价输入区域 -->
        <div class="review-form" v-if="isDisplayed">
          <el-rate v-model="request.stars" size="small" class="form-rate" />
          <el-input
            v-model="request.text"
            type="textarea"
            placeholder="分享您的体验..."
            :rows="4"
            class="form-textarea"
          />
          <div class="form-actions">
            <el-button
              @click="writeReview(result.businessId, result.bid)"
              type="primary"
            >
              提交评价
            </el-button>
            <el-button @click="switchStatus">取消</el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="reviews-list">
          <div
            class="review-item"
            v-for="(review, index) in result.reviewVOList"
            :key="index"
          >
            <div class="review-header">
              <div class="user-info">
                <el-avatar :size="32">{{
                  review.userName?.charAt(0) || "U"
                }}</el-avatar>
                <div class="user-details">
                  <span class="username">{{ review.userName }}</span>
                  <span class="review-date">{{ review.date }}</span>
                </div>
              </div>
              <el-rate
                v-model="review.stars"
                disabled
                size="small"
                class="review-rating"
              />
            </div>

            <div class="review-content">
              {{ review.text }}
            </div>

            <div class="review-actions">
              <el-button
                type="primary"
                link
                @click="
                  useful(
                    review.reviewId,
                    review.cool,
                    review.useful,
                    review.funny,
                    index
                  )
                "
              >
                <el-icon><Pointer /></el-icon>
                <span>有用 {{ review.useful }}</span>
              </el-button>

              <el-button
                type="warning"
                link
                @click="
                  funny(
                    review.reviewId,
                    review.cool,
                    review.useful,
                    review.funny,
                    index
                  )
                "
              >
                <el-icon><Sunny /></el-icon>
                <span>有趣 {{ review.funny }}</span>
              </el-button>

              <el-button
                type="info"
                link
                @click="
                  cool(
                    review.reviewId,
                    review.cool,
                    review.useful,
                    review.funny,
                    index
                  )
                "
              >
                <el-icon><Headset /></el-icon>
                <span>酷 {{ review.cool }}</span>
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, toRefs, watch } from "vue";
import { useBusiness } from "@/hooks/UseBusiness";
import { useRoute } from "vue-router";
import {
  StarFilled,
  Star,
  Location,
  Clock,
  Collection,
  Picture,
  QuestionFilled,
  CircleCheck,
  CircleClose,
  EditPen,
  Pointer,
  Sunny,
  Headset,
  ArrowLeft,
  ArrowRight,
  OfficeBuilding,
  Lightning,
} from "@element-plus/icons-vue";
import { UseCollectStore } from "@/stores/UseCollectStore";
import axios from "axios";
import { router } from "@/router";
import { UseImageListStore } from "@/stores/UseImageListStore";
import { useWriteReviews } from "@/hooks/UseWriteReviews";
import { ElMessage } from "element-plus";

const collectStore = UseCollectStore();
const { result, getResult } = toRefs(useBusiness());
const route = useRoute();
const { isDisplayed, switchStatus, writeReview, request } = useWriteReviews();
// 添加响应式变量
const showBusinessHours = ref(false);
// 初始化请求参数
request.value.businessId = result.value.businessId;
request.value.bid = result.value.bid;

const imageListStore = UseImageListStore();

// 检查是否已收藏
const isCollected = computed(() => {
  return collectStore.collect.includes(result.value.businessId);
});

// 轮播图当前页码
const currentSlide = ref(0);

// 监听图片列表变化，重置轮播图
watch(
  () => result.value?.imageList,
  () => {
    currentSlide.value = 0;
  }
);

// 上一张图片
const prevSlide = () => {
  if (result.value?.imageList && result.value.imageList.length > 0) {
    currentSlide.value =
      (currentSlide.value - 1 + result.value.imageList.length) %
      result.value.imageList.length;
  }
};

// 下一张图片
const nextSlide = () => {
  if (result.value?.imageList && result.value.imageList.length > 0) {
    currentSlide.value =
      (currentSlide.value + 1) % result.value.imageList.length;
  }
};

// 跳转到指定图片
const goToSlide = (index: number) => {
  currentSlide.value = index;
};

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
  imageListStore.setImageList(result.value.imageList);
  router.push("/imageDisplay");
}

// 切换收藏状态
async function toggleCollect() {
  if (isCollected.value) {
    await cancelCollect(result.value.businessId);
  } else {
    await collect(result.value.businessId);
  }
}

// 收藏商户
async function collect(businessId: string) {
  try {
    const res = await axios.post(`/api/collect/save?businessId=${businessId}`);
    ElMessage.success(res.data.data);
    collectStore.pushCollect(businessId);
  } catch (error) {
    console.error(error);
    ElMessage.error("收藏失败");
  }
}

// 取消收藏
async function cancelCollect(businessId: string) {
  try {
    const res = await axios.delete(
      `/api/collect/cancel?businessId=${businessId}`
    );
    ElMessage.success(res.data.data);
    collectStore.removeCollect(businessId);
  } catch (error) {
    console.error(error);
    ElMessage.error("取消收藏失败");
  }
}

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

// 解析分类字符串
const parseCategories = (categories: string) => {
  if (!categories) return [];
  return categories.split(",").map((cat) => cat.trim());
};

// 定义重要属性列表
const importantAttributes = [
  'WiFi',
  'Alcohol',
  'OutdoorSeating',
  'RestaurantsDelivery',
  'RestaurantsTakeOut',
  'BusinessAcceptsCreditCards',
  'DogsAllowed',
  'GoodForKids'
];

// 获取重要属性的函数
const getImportantAttributes = () => {
  if (!result.value?.attributes) return [];
  
  try {
    const attrs = JSON.parse(result.value.attributes);
    const resultAttrs = [];
    
    // 遍历重要属性列表
    importantAttributes.forEach(key => {
      if (attrs.hasOwnProperty(key)) {
        resultAttrs.push({
          key,
          value: attrs[key]
        });
      } else {
        // 如果属性不存在，添加一个默认值为未知的项
        resultAttrs.push({
          key,
          value: 'unknown'
        });
      }
    });
    
    return resultAttrs;
  } catch (e) {
    return [];
  }
};

// 添加颜色映射函数
const getIconColor = (value: string) => {
  if (value === 'true') return '#67c23a';
  if (value === 'false') return '#f56c6c';
  return '#409eff';
};

const formatAttributeName = (name: string) => {
  const nameMap = {
    'WiFi': '是否有WiFi',
    'Alcohol': '是否提供酒精饮品',
    'OutdoorSeating': '是否有户外座位',
    'RestaurantsDelivery': '是否提供外卖',
    'RestaurantsTakeOut': '是否提供外带',
    'BusinessAcceptsCreditCards': '是否接受信用卡',
    'GoodForKids': '是否适合儿童',
    'HasTV': '是否有电视',
    'NoiseLevel': '噪音等级',
    'RestaurantsReservations': '是否需要预约',
    'WheelchairAccessible': '是否无障碍通道',
    'DogsAllowed': '是否允许宠物入内',
    'HappyHour': '是否有欢乐时光',
    'CoatCheck': '是否提供衣帽间',
    'Smoking': '是否允许吸烟'
  };
  return nameMap[name] || name;
};

// 更新格式化函数以处理未知值
const formatAttributeValue = (value: string) => {
  if (value === 'true') return '是';
  if (value === 'false') return '否';
  if (value === 'none') return '无';
  if (value === 'unknown') return '未知';
  
  if (value === 'average') return '一般';
  if (value === 'quiet') return '安静';
  if (value === 'loud') return '吵闹';
  if (value === 'very_loud') return '非常吵闹';
  
  // 处理数组类型值
  if (typeof value === 'object' && value !== null) {
    const obj = JSON.parse(value);
    const keys = Object.keys(obj);
    const values = Object.values(obj);
    
    // 过滤出为 true 的项
    const trueKeys = keys.filter((key, index) => values[index] === true);
    
    if (trueKeys.length > 0) {
      return trueKeys.join('、');
    }
    
    return '无';
  }
  
  // 处理价格范围
  if (value.startsWith('PriceRange')) {
    const rangeMap = {
      '1': '¥',
      '2': '¥¥',
      '3': '¥¥¥',
      '4': '¥¥¥¥'
    };
    return rangeMap[value] || value;
  }
  
  return value;
};

// 评价互动功能
function cool(
  reviewId: number,
  cool: number,
  useful: number,
  funny: number,
  index: number
) {
  // 实现点赞逻辑
  console.log("Cool clicked for review:", reviewId);
}

function useful(
  reviewId: number,
  cool: number,
  useful: number,
  funny: number,
  index: number
) {
  // 实现有用逻辑
  console.log("Useful clicked for review:", reviewId);
}

function funny(
  reviewId: number,
  cool: number,
  useful: number,
  funny: number,
  index: number
) {
  // 实现有趣逻辑
  console.log("Funny clicked for review:", reviewId);
}

onMounted(() => {
  console.log("商户信息：", result.value);
  getResult.value(route.query.id);
});
</script>

<style scoped>
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

.collect-btn {
  box-shadow: 0 2px 8px rgba(224, 7, 7, 0.3);
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.header-content {
  flex: 1;
}

.business-name {
  margin: 0 0 8px 0;
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

.info-content .value.open {
  color: #67c23a;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.category-tag {
  margin: 0;
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

.ai-rating .ai-label {
  color: #409eff;
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
.basic-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.info-row .el-icon {
  font-size: 18px;
  color: #409eff;
}

.location-text,
.hours,
.categories {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.divider {
  color: #c0c4cc;
}

.label {
  font-weight: 500;
}

.value {
  color: #303133;
}

.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.review-stats {
  display: flex;
  gap: 32px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 24px;
}

.stats-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stats-score {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stats-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
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
  padding-left: 44px;
}

.review-actions {
  display: flex;
  gap: 20px;
  padding-left: 44px;
}

.review-actions .el-button {
  padding: 0;
}

.review-actions .el-button span {
  margin-left: 6px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .review-stats {
    flex-direction: column;
    gap: 16px;
  }

  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .review-content,
  .review-actions {
    padding-left: 0;
  }
}

/* 添加营业时间样式 */
.info-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.hours {
  display: flex;
  flex-direction: column;
  color: #606266;
}

.hours .label {
  font-weight: 500;
  margin-bottom: 8px;
}

/* 调整营业时间内容的间距 */
.hours-content {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 4px 12px; /* 减小行间距，保持列间距 */
}

.hours-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.day {
  font-weight: 500;
  color: #303133;
  min-width: 40px;
}

.time {
  color: #67c23a;
  font-weight: 500;
}

.time.closed {
  color: #f56c6c;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .hours-content {
    grid-template-columns: 1fr;
  }

  .hours-item {
    justify-content: space-between;
  }
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

/* 弹幕式小贴士样式 */
.tips-barrage {
  height: 90px; /* 增加高度以容纳多行 */
  overflow: hidden;
  position: relative;
  background-color: #ecf5ff;
  border-radius: 12px;
  margin-top: 12px;
}

.barrage-item {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
  color: #409eff;
  font-size: 14px;
  padding: 4px 12px;
  animation: barrage 20s linear infinite; /* 减慢速度 */
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.tip-text {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
}

@keyframes barrage {
  0% {
    transform: translateX(100vw);
  }
  100% {
    transform: translateX(-100%);
  }
}

/* 确保每个弹幕项有适当的间距 */
.barrage-item:nth-child(1) {
  top: 0;
}

.barrage-item:nth-child(2) {
  top: 30px;
}

.barrage-item:nth-child(3) {
  top: 60px;
}

/* 设置初始位置，确保弹幕从右侧开始 */
.barrage-item {
  transform: translateX(100vw);
  transition: transform 0.3s ease;
}

/* 分割线 */
.divider-vertical {
  width: 1px;
  background-color: #e4e7ed;
}

/* Q&A区域 */
.qa-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

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
  padding-left: 24px; /* 增加左侧缩进 */
  position: relative;
}

.answer::before {
  content: '';
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
@media (max-width: 768px) {
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

/* 评价表单 */
.review-form {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
}

.form-rate {
  margin-bottom: 16px;
}

.form-textarea {
  margin-bottom: 16px;
}

.form-actions {
  display: flex;
  gap: 12px;
}

/* 评论列表 */
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
  align-items: center;
  margin-bottom: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
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
  margin-bottom: 12px;
}

.review-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 16px;
}

.review-actions {
  display: flex;
  gap: 20px;
}

.review-actions .el-button {
  padding: 0;
}

.review-actions .el-button span {
  margin-left: 6px;
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
    font-size: 24px; /* 调整字体大小 */
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
    padding: 12px; /* 减小移动端内边距 */
  }

  .thumbnail-container {
    flex-wrap: wrap;
  }

  .main-image-container {
    height: 200px; /* 进一步减小移动端高度 */
  }

  .business-name {
    font-size: 20px; /* 调整移动端字体大小 */
  }

  .review-actions {
    flex-wrap: wrap;
  }

  .carousel-btn {
    width: 28px; /* 减小移动端按钮尺寸 */
    height: 28px; /* 减小移动端按钮尺寸 */
  }

  .prev-btn {
    left: 8px;
  }

  .next-btn {
    right: 8px;
  }
}
</style>
