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
            <!-- 地址区域添加点击事件 -->
            <div class="business-address" @click="showMapDialog = true">
              <el-icon><Location /></el-icon>
              <span>
                {{ result.address }}, {{ result.city }}, {{ result.state }}
                {{ result.postalCode }}
              </span>
            </div>
            <!-- 地图弹窗 -->
            <el-dialog
              v-model="showMapDialog"
              title="商户位置与路线"
              width="800px"
              :before-close="handleClose"
            >
              <div class="map-container">
                <div id="map" class="map"></div>
                <div class="map-info">
                  <p>商户地址: {{ fullAddress }}</p>
                  <p v-if="userLocation">
                    您的位置: {{ userLocation.address }}
                  </p>
                  <p v-if="distance">距离: {{ distance.toFixed(2) }} 公里</p>
                  <el-button
                    @click="calculateRoute"
                    type="primary"
                    v-if="userLocation && merchantLocation"
                    class="route-btn"
                  >
                    规划路线
                  </el-button>
                  <div id="route-panel" class="route-panel"></div>
                </div>
              </div>
            </el-dialog>
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
            <el-rate v-model="aiScore" disabled size="small" />
            <span class="rating-value">{{ aiScore || 0 }}</span>
            <el-button
              @click="toggleAIReason"
              type="primary"
              link
              size="small"
              class="toggle-reason-btn"
            >
              {{ showAIReason ? "收起" : "点击生成AI评分" }}
            </el-button>
          </div>

          <!-- AI评分理由展示区域 -->
          <div v-if="showAIReason" class="ai-reason-section">
            <div class="reason-content">
              <div v-if="aiLoading" class="loading-state">
                <el-icon class="is-loading"><Loading /></el-icon>
                <span>AI正在分析图片美观度...</span>
              </div>
              <div v-else-if="aiReason" class="reason-text">
                <pre>{{ aiReason }}</pre>
              </div>
              <div v-else class="no-reason">
                <span>暂无评分理由</span>
              </div>
            </div>
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
            <el-button
              @click="switchStatus"
              type="primary"
              :icon="EditPen"
              plain
            >
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
            <el-rate
              v-model="request.stars"
              size="small"
              class="form-rate"
              allow-half
            />
            <el-input
              v-model="request.text"
              type="textarea"
              placeholder="分享您的体验..."
              :rows="4"
              class="form-textarea"
              maxlength="500"
              show-word-limit
            />
            <div class="form-actions">
              <el-button
                @click="writeReview(result.businessId, result.bid)"
                type="primary"
                :disabled="!canSubmit"
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
                  :disabled="review.userClicked?.useful"
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
                  :disabled="review.userClicked?.funny"
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
                  :disabled="review.userClicked?.cool"
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
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, toRefs, watch, onUnmounted } from "vue";
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
  Loading,
} from "@element-plus/icons-vue";
import { UseCollectStore } from "@/stores/UseCollectStore";
import axios from "axios";
import { router } from "@/router";
import { UseImageListStore } from "@/stores/UseImageListStore";
import { useWriteReviews } from "@/hooks/UseWriteReviews";
import { ElMessage } from "element-plus";
// 评价互动功能
import { changeReviewSatisfaction } from "@/apis/reviews.js";
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
  if (!result.value?.attributes) return [];

  try {
    const attrs = JSON.parse(result.value.attributes);
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
const getIconColor = (value: string) => {
  if (value === "true") return "#67c23a";
  if (value === "false") return "#f56c6c";
  return "#409eff";
};

const formatAttributeName = (name: string) => {
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
const formatAttributeValue = (value: string) => {
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

// 写评论
// 添加计算属性
const canSubmit = computed(() => {
  return (
    request.value.stars > 0 &&
    request.value.text &&
    request.value.text.trim().length > 0
  );
});
// 评论点赞三个函数实现
async function cool(reviewId, cool, useful, funny, index) {
  try {
    const rsDTO = {
      reviewId,
      cool: 1,
      useful: 0,
      funny: 0,
    };

    await changeReviewSatisfaction(rsDTO);

    // 更新本地数据
    if (result.value.reviewVOList && result.value.reviewVOList[index]) {
      result.value.reviewVOList[index].cool += 1;
      // 初始化 userClicked 对象（如果不存在）
      if (!result.value.reviewVOList[index].userClicked) {
        result.value.reviewVOList[index].userClicked = {
          cool: false,
          useful: false,
          funny: false,
        };
      }
      // 设置对应的状态为已点击
      result.value.reviewVOList[index].userClicked.cool = true;
    }

    ElMessage.success("操作成功");
  } catch (error) {
    console.error("Failed to update cool count:", error);
    ElMessage.error("操作失败");
  }
}

async function useful(reviewId, cool, useful, funny, index) {
  try {
    const rsDTO = {
      reviewId,
      cool: 0,
      useful: 1,
      funny: 0,
    };

    await changeReviewSatisfaction(rsDTO);

    // 更新本地数据
    if (result.value.reviewVOList && result.value.reviewVOList[index]) {
      result.value.reviewVOList[index].useful += 1;
      // 初始化 userClicked 对象（如果不存在）
      if (!result.value.reviewVOList[index].userClicked) {
        result.value.reviewVOList[index].userClicked = {
          cool: false,
          useful: false,
          funny: false,
        };
      }
      // 设置对应的状态为已点击
      result.value.reviewVOList[index].userClicked.useful = true;
    }

    ElMessage.success("操作成功");
  } catch (error) {
    console.error("Failed to update useful count:", error);
    ElMessage.error("操作失败");
  }
}

async function funny(reviewId, cool, useful, funny, index) {
  try {
    const rsDTO = {
      reviewId,
      cool: 0,
      useful: 0,
      funny: 1,
    };

    await changeReviewSatisfaction(rsDTO);

    // 更新本地数据
    if (result.value.reviewVOList && result.value.reviewVOList[index]) {
      result.value.reviewVOList[index].funny += 1;
      // 初始化 userClicked 对象（如果不存在）
      if (!result.value.reviewVOList[index].userClicked) {
        result.value.reviewVOList[index].userClicked = {
          cool: false,
          useful: false,
          funny: false,
        };
      }
      // 设置对应的状态为已点击
      result.value.reviewVOList[index].userClicked.funny = true;
    }

    ElMessage.success("操作成功");
  } catch (error) {
    console.error("Failed to update funny count:", error);
    ElMessage.error("操作失败");
  }
}

// 添加 loading 状态
const loading = ref(true);

onMounted(() => {
  console.log("商户信息：", result.value);

  // 显示加载状态
  loading.value = true;

  getResult.value(route.query.id).finally(() => {
    // 数据加载完成后隐藏加载状态
    loading.value = false;
  });
  // 添加评论提交事件监听
  window.addEventListener("reviewSubmitted", handleReviewSubmitted);
});

onUnmounted(() => {
  window.removeEventListener("reviewSubmitted", handleReviewSubmitted);
});

// 监听 reviewSubmitted 事件时也显示加载状态
const handleReviewSubmitted = () => {
  loading.value = true;
  setTimeout(() => {
    getResult.value(route.query.id).finally(() => {
      loading.value = false;
    });
  }, 1000);
};

// 添加AI评分相关的响应式变量
const showAIReason = ref(false);
const aiReason = ref("");
const aiLoading = ref(false);
const aiScore = ref(0); // 新增：用于存储提取的AI评分

// 切换AI评分理由显示/隐藏
const toggleAIReason = () => {
  showAIReason.value = !showAIReason.value;

  // 如果是第一次展开且还没有获取过评分理由，则获取评分
  if (showAIReason.value && !aiReason.value && !aiLoading.value) {
    getAIScore();
  }
};

// 从AI返回的文本中提取评分
const extractAIScore = (text) => {
  if (!text) return 0;

  // 使用正则表达式匹配"最终评分：数字"格式
  const scoreMatch = text.match(/最终评分[：:]\s*(\d+)/);
  if (scoreMatch && scoreMatch[1]) {
    return parseInt(scoreMatch[1]);
  }

  // 如果没有找到评分，返回默认值0
  return 0;
};

/**
 * 将图片URL转换为base64编码
 * @param {string} url - 图片URL
 * @returns {Promise<string>} base64编码的图片数据
 */
function imageUrlToBase64(url) {
  return new Promise((resolve, reject) => {
    // 创建图片对象
    const img = new Image();
    img.crossOrigin = "Anonymous"; // 处理跨域问题

    img.onload = () => {
      // 创建canvas元素
      const canvas = document.createElement("canvas");
      const ctx = canvas.getContext("2d");

      // 设置canvas尺寸与图片一致
      canvas.width = img.width;
      canvas.height = img.height;

      // 将图片绘制到canvas上
      ctx.drawImage(img, 0, 0);

      // 将canvas转换为base64编码
      const dataURL = canvas.toDataURL("image/jpeg");
      resolve(dataURL);
    };

    img.onerror = (error) => {
      reject(new Error("图片加载失败: " + error));
    };

    // 开始加载图片
    img.src = url;
  });
}

/**
 * 商户实景图片美观打分 - 多模态请求（Base64传图）
 * @param {string} apiKey - Silicon Flow API Key
 * @param {string} imageBase64 - 商户图片Base64编码数据
 * @returns {Promise<string>} 打分结果+优化建议
 */
async function scoreShopAesthetics(apiKey, imageBase64) {
  // 1. 配置请求参数
  const requestOptions = {
    method: "POST",
    headers: {
      Authorization: `Bearer ${apiKey}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      model: "Qwen/Qwen3-VL-32B-Instruct", // 中文优先选该模型，也可换 GLM-4.1V
      messages: [
        {
          role: "user",
          content: [
            // 文本指令：明确打分维度+规则
            {
              type: "text",
              text: `请对该商户实景图片进行美观度打分，要求：
1. 评分维度：构图（1-5分）、色彩搭配（1-5分）、环境整洁度（1-5分）、品牌呈现（1-5分）；
2. 输出综合分（四维度平均分，向下取整），并给出简短理由；
3. 结果格式：最终评分： 理由：。`,
            },
            // 图片数据（Base64编码）
            {
              type: "image_url",
              image_url: {
                url: imageBase64,
              },
            },
          ],
        },
      ],
      max_tokens: 1024, // 输出长度限制
      temperature: 0.1, // 低随机性，保证打分稳定
    }),
  };

  try {
    // 2. 发送请求
    const response = await fetch(
      "https://api.siliconflow.cn/v1/chat/completions",
      requestOptions
    );
    if (!response.ok) {
      const errorText = await response.text(); // 获取详细错误信息
      console.error("API错误详情:", errorText);
      throw new Error(`HTTP 错误：${response.status} - ${errorText}`);
    }

    // 3. 解析结果
    const result = await response.json();
    const scoreResult = result.choices[0].message.content;
    console.log("✅ 商户美观打分结果：\n", scoreResult);
    return scoreResult;
  } catch (error) {
    console.error("❌ 请求失败：", error.message);
    throw error;
  }
}

// 获取AI评分
async function getAIScore() {
  // 检查是否有图片
  if (!result.value?.imageList || result.value.imageList.length === 0) {
    aiReason.value = "该商户暂无图片，无法进行AI评分";
    return;
  }

  // 获取第一张图片作为评分依据
  const firstImage = result.value.imageList[0];
  const imageUrl = getImagePath(firstImage);

  // 检查是否是有效的图片URL
  if (!imageUrl) {
    aiReason.value = "图片路径无效，无法进行AI评分";
    return;
  }

  // 开始加载状态
  aiLoading.value = true;

  try {
    // 将图片URL转换为base64编码
    const imageBase64 = await imageUrlToBase64(imageUrl);

    // 替换为实际的API Key (在生产环境中应从安全的地方获取)
    const apiKey = "sk-jsppmnzualuadnsjwnneaqsupkcpjfoungipzaahqygoqhqw"; // 示例API Key

    // 调用AI评分函数，传递base64编码的图片
    const scoreResult = await scoreShopAesthetics(apiKey, imageBase64);
    aiReason.value = scoreResult;

    // 提取AI评分并保存
    aiScore.value = extractAIScore(scoreResult);
  } catch (error) {
    console.error("AI评分失败:", error);
    aiReason.value = `AI评分失败: ${error.message}`;
  } finally {
    aiLoading.value = false;
  }
}

// *******************地图
// 新增地图相关变量
const showMapDialog = ref(false);
const userLocation = ref<any>(null); // 用户位置（经纬度+地址）
const merchantLocation = ref<any>(null); // 商户位置（直接用后端经纬度）
const distance = ref<number>(0); // 距离（公里）
const map = ref<any>(null); // 地图实例
const driving = ref<any>(null); // 路线规划实例

// 计算完整地址（用于显示）
const fullAddress = computed(() => {
  return `${result.value.address}, ${result.value.city}, ${result.value.state} ${result.value.postalCode}`;
});

// 初始化地图
const initMap = () => {

  // 设置安全密钥
  (window as any)._AMapSecurityConfig = {
    securityJsCode: "fece524185e1f50a1f12de4faa7a15cc",
  };

  if (window.AMap) {
    createMap();
  } else {
    // 动态加载高德地图API（替换为你的密钥）
    const script = document.createElement("script");
    script.type = "text/javascript";
    script.src =
      "/wpi/maps?v=2.0&key=038019d08912b27aa684b47177e4113e&callback=initAMap";
    script.onerror = () => ElMessage.error("地图加载失败");
    document.head.appendChild(script);
    window.initAMap = createMap; // 全局回调函数
  }
};

// 创建地图实例（核心修改：直接使用后端经纬度）
const createMap = () => {
  // 1. 直接从后端数据获取商户经纬度
  if (!result.value.latitude || !result.value.longitude) {
    ElMessage.error("商户位置信息缺失");
    return;
  }
  merchantLocation.value = {
    lat: result.value.latitude, // 后端返回的纬度
    lng: result.value.longitude, // 后端返回的经度
    address: fullAddress.value,
  };

  // ========== 打印商户经纬度 ==========
  console.log("🚩 商户位置经纬度：", {
    "纬度(lat)": merchantLocation.value.lat,
    "经度(lng)": merchantLocation.value.lng,
    完整地址: merchantLocation.value.address,
  });

  // 2. 创建地图并定位到商户位置
  map.value = new AMap.Map("map", {
    zoom: 13,
    center: [merchantLocation.value.lng, merchantLocation.value.lat], // 直接用商户经纬度
  });

  // 3. 在地图上标记商户位置
  new AMap.Marker({
    position: [merchantLocation.value.lng, merchantLocation.value.lat],
    title: result.value.name,
    map: map.value,
    label: { content: "商户", offset: new AMap.Pixel(0, 30) },
  });

  // 4. 获取用户当前位置
  getUserLocation();
};

// ================================
// 方法三：浏览器定位 + 高德兜底 + 可调试模拟
// ================================
const DEBUG_USE_MOCK_LOCATION = false; // 开发想固定贵州时改成 true



const getUserLocation = () => {
  // 优先使用浏览器定位
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        // 浏览器定位成功
        const { latitude, longitude } = position.coords;
        userLocation.value = {
          lat: latitude,
          lng: longitude,
          address: "正在获取地址..."
        };
        
        console.log("📍 浏览器定位成功：", { latitude, longitude });
        
        // 调用高德反地理编码获取真实地址
        AMap.plugin("AMap.Geocoder", () => {
          const geocoder = new AMap.Geocoder();
          
          geocoder.getAddress([longitude, latitude], (status, result) => {
            if (status === "complete" && result.regeocode) {
              const addr = result.regeocode.formattedAddress;
              userLocation.value.address = addr;
              console.log("✅ 反地理编码成功：", addr);
            } else {
              userLocation.value.address = "无法获取详细地址";
              console.error("❌ 反地理编码失败：", result);
            }
            
            // 移动地图
            map.value.setCenter([longitude, latitude]);
            
            // 计算距离
            calculateDistance();
          });
        });
      },
      (error) => {
        // 浏览器定位失败，使用高德定位
        console.error("❌ 浏览器定位失败：", error.message);
        ElMessage.warning("浏览器定位失败，尝试使用高德定位");
        useAMapLocation();
      },
      {
        enableHighAccuracy: true,     // 启用高精度
        timeout: 10000,               // 10秒超时
        maximumAge: 60000             // 缓存1分钟
      }
    );
  } else {
    // 浏览器不支持定位
    ElMessage.error("浏览器不支持定位功能");
    console.warn("浏览器不支持 navigator.geolocation");
    // 使用高德定位作为备选方案
    useAMapLocation();
  }
};

// ================================
// 高德兜底定位
// ================================
const useAMapLocation = () => {
  AMap.plugin("AMap.Geolocation", () => {
    const geolocation = new AMap.Geolocation({
      enableHighAccuracy: true,
      timeout: 10000,
      convert: true,
      useNative: true,      // 强制原生
      panToLocation: true,  // 自动移动
      showButton: false,
      showMarker: false,
      showCircle: false,
      needAddress: true,
    });

    geolocation.getCurrentPosition((status, result) => {
      if (status === "complete") {
        const { position } = result;

        userLocation.value = {
          lat: position.lat,
          lng: position.lng,
          address:
            result.regeocode?.formattedAddress ||
            "高德定位（无详细地址）",
        };

        console.log("✅ 高德兜底定位成功：", {
          lat: position.lat,
          lng: position.lng,
        });

        map.value.setCenter([position.lng, position.lat]);
        calculateDistance();
      } else {
        ElMessage.error("定位失败，请检查浏览器GPS权限");
        console.error("❌ 高德定位失败：", result.message);
      }
    });
  });
};

// 计算用户与商户的距离（直接使用高德API）
const calculateDistance = () => {
  if (userLocation.value && merchantLocation.value) {
    // ========== 打印距离计算的原始经纬度 ==========
    console.log("📏 距离计算依据：", {
      用户经纬度: [userLocation.value.lng, userLocation.value.lat],
      商户经纬度: [merchantLocation.value.lng, merchantLocation.value.lat],
    });

    AMap.plugin("AMap.GeometryUtil", () => {
      const distanceMeters = AMap.GeometryUtil.distance(
        [userLocation.value.lng, userLocation.value.lat],
        [merchantLocation.value.lng, merchantLocation.value.lat]
      );
      distance.value = distanceMeters / 1000; // 转换为公里
      console.log(
        "📐 计算结果：",
        `${distance.value.toFixed(2)} 公里（${distanceMeters} 米）`
      );
    });
  }
};

// 规划路线（直接使用高德API）
const calculateRoute = () => {
  if (!userLocation.value || !merchantLocation.value) {
    ElMessage.warning("用户或商户位置缺失");
    return;
  }

  if (driving.value) driving.value.clear();

  AMap.plugin("AMap.Driving", () => {
    driving.value = new AMap.Driving({
      map: map.value,
      panel: "route-panel",
      policy: AMap.DrivingPolicy.LEAST_TIME,
    });

    const start = new AMap.LngLat(
      userLocation.value.lng,
      userLocation.value.lat
    );
    const end = new AMap.LngLat(
      merchantLocation.value.lng,
      merchantLocation.value.lat
    );

    driving.value.search(start, end, (status, result) => {
      if (status === "complete" && result.routes?.length) {
        ElMessage.success("✅ 路线规划成功");
        console.log("🛣 路线结果：", result);
      } else {
        ElMessage.error("❌ 路线规划失败");
        console.error("路线失败详情：", result);
      }
    });
  });
};

// 关闭弹窗时清理资源
const handleClose = () => {
  showMapDialog.value = false;
  if (map.value) map.value.destroy();
  if (driving.value) driving.value.destroy();
  userLocation.value = null;
  distance.value = 0;
};

// 监听弹窗显示状态，初始化地图
watch(showMapDialog, (newVal) => {
  if (newVal) setTimeout(initMap, 100); // 延迟确保弹窗渲染完成
});
</script>

<style scoped>
/* 地图弹窗样式 */
.map-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: 500px;
}

#map {
  width: 100%;
  height: 300px;
  border-radius: 8px;
}

#mapContainer {
  width: 100%; /* 必须设置具体宽度 */
  height: 500px; /* 必须设置具体高度 */
  position: relative; /* 确保定位正常 */
}

.map-info {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.map-info p {
  margin: 8px 0;
  line-height: 1.5;
}

.route-btn {
  margin: 10px 0;
}

.route-panel {
  max-height: 200px;
  overflow-y: auto;
}

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

/* AI评分理由区域样式 */
.ai-reason-section {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.reason-content {
  min-height: 24px;
}

.reason-content .loading-state {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.reason-content .reason-text pre {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: inherit;
  color: #606266;
  line-height: 1.6;
}

.reason-content .no-reason {
  color: #909399;
  font-style: italic;
}

.toggle-reason-btn {
  margin-left: auto;
}
</style>
