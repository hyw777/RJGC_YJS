<template>
  <div class="nearby-container">
    <!-- 添加无商户提示 -->
    <div v-if="businesses && businesses.length === 0" class="empty-state">
      <div class="empty-icon">🏪</div>
      <h3>您的附近暂时没有商户注册</h3>
      <p>敬请谅解，您可以稍后再试或联系客服了解更多</p>
    </div>

    <div class="business-grid" v-else>
      <div
        class="show-box"
        v-for="(business, index) in businesses"
        :key="index"
      >
        <router-link
          class="img"
          :to="{ path: '/merchantDetail', query: { id: business.businessId } }"
        >
          <img
            v-if="business.image"
            :src="filePath(business.image)"
            :alt="business.name"
          />
          <div v-else class="initials-placeholder">
            {{ getInitials(business.name) }}
          </div>
        </router-link>
        <div class="info-box">
          <div class="info-content">
            <div class="info-header">
              <h3 class="business-name">{{ business.name }}</h3>
              <div class="distance-tag" v-if="locationError">
                <span>获取不到您的定位</span>
              </div>
              <div
                class="distance-tag"
                v-else-if="business.distance !== undefined"
              >
                <span>{{ formatDistance(business.distance) }}</span>
              </div>
            </div>

            <div class="info-rating">
              <el-rate v-model="business.stars" size="large" disabled></el-rate>
              <div class="rate-info">
                <span class="stars-value">{{ business.stars }}</span>
                <span class="review-count"
                  >({{ business.reviewCount }} reviews)</span
                >
              </div>
            </div>

            <div class="info-categories">
              <span class="category">{{ business.categories }}</span>
            </div>
          </div>

          <div class="info-actions">
            <el-button
              type="primary"
              size="small"
              round
              @click.prevent="
                $router.push({
                  path: '/merchantDetail',
                  query: { id: business.businessId },
                })
              "
            >
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, toRefs } from "vue";
import { useActivity } from "@/hooks/UseActivity";
import { useAuthStore } from "@/stores/UseAuthStore";
import { UseButtonStore } from "@/stores/UseButtonStore";
// 导入图标组件
import { Check, Star, Collection, CircleClose } from "@element-plus/icons-vue";

let authStore = useAuthStore();
let buttonStore = UseButtonStore();
let { businesses, getNearByBusinesses } = toRefs(useActivity());

// 添加获取商户名称首字母的函数
const getInitials = (name: string) => {
  if (!name) return "";
  return name.charAt(0).toUpperCase();
};

// 添加格式化距离的函数
const formatDistance = (distance: number) => {
  if (distance < 1) {
    return `${Math.round(distance * 1000)}米`;
  }
  return `${distance.toFixed(2)}公里`;
};
const filePath = (file: any) => {
  if (file == null) {
    console.log("null");
    return "";
  }
  // 如果图片链接已经是完整的URL，直接使用；否则拼接静态服务器地址
  const fullPath = file.includes("http")
    ? file
    : `http://localhost:3000/images/${file}.jpg`;
  return fullPath;
};

function ensureInteractions(business: any) {
  if (!business.__interactions) {
    business.__interactions = { useful: 0, thanks: 0, like: 0, ohno: 0 };
  }
}

function markUseful(business: any) {
  ensureInteractions(business);
  business.__interactions.useful++;
}

function markThanks(business: any) {
  ensureInteractions(business);
  business.__interactions.thanks++;
}

function markLike(business: any) {
  ensureInteractions(business);
  business.__interactions.like++;
}

function markOhNo(business: any) {
  ensureInteractions(business);
  business.__interactions.ohno++;
}

// **************获取用户当前位置

import { ref } from "vue";

// 添加位置相关状态
const userLocation = ref<{ latitude: number; longitude: number } | null>(null);
const locationError = ref<string | null>(null);

// 获取用户位置
// 修改 getUserLocation 函数中的成功回调
const getUserLocation = () => {
  console.log("开始获取用户位置");
  if (navigator.geolocation) {
    console.log("浏览器支持地理位置");
    navigator.geolocation.getCurrentPosition(
      (position) => {
        console.log("用户位置：", position);
        userLocation.value = {
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        };
        // 获取位置后调用附近商户接口，传递位置信息
        getNearByBusinessesWithLocation();
      },
      (error) => {
        console.error("获取位置失败：", error);
        locationError.value = `获取位置失败: ${error.message}`;
        // 降级到原有逻辑（不传递位置信息）
        if (authStore.token) {
          getNearByBusinesses.value();
        }
      }
    );
  } else {
    console.log("浏览器不支持地理位置");
    locationError.value = "浏览器不支持地理位置";
    // 降级到原有逻辑（不传递位置信息）
    if (authStore.token) {
      getNearByBusinesses.value();
    }
  }
};

// 带位置信息的获取附近商户方法
const getNearByBusinessesWithLocation = () => {
  if (userLocation.value && authStore.token) {
    // 传递用户位置给后端
    getNearByBusinesses
      .value(userLocation.value.latitude, userLocation.value.longitude)
      .then(() => {
        // 打印获取到的商户信息
        console.log("从后端获取的商户信息:", businesses.value);
      });
  }
};

onMounted(() => {
  buttonStore.setIndexButton(0);
  getUserLocation();
});

// onMounted(() => {
//   buttonStore.setIndexButton(0);
//   if (authStore.token) {
//     getNearByBusinesses.value();
//   }
// });
</script>

<style scoped>
.nearby-container {
  padding: 20px;
}

.business-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 固定3列 */
  gap: 24px;
  padding: 20px 0;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .business-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .business-grid {
    grid-template-columns: 1fr;
  }
}
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #f8f9fa;
  border-radius: 12px;
  margin: 20px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state h3 {
  color: #333;
  margin-bottom: 8px;
}

.empty-state p {
  color: #666;
}

/* 卡片样式 */
.show-box {
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #eee;
  min-height: 380px; /* 设置最小高度确保一致性 */
  width: 390px;
} 

.show-box:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 图片区域 */
.img {
  display: block;
  height: 200px;
  overflow: hidden;
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.img:hover img {
  transform: scale(1.05);
}

.initials-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 48px;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 信息区域 */
.info-box {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex: 1;
  min-height: 180px; /* 确保信息区域有最小高度 */
}

.info-content {
  flex: 1; /* 使内容区域占据可用空间 */
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-actions {
  margin-top: auto; /* 将按钮推到底部 */
}
.info-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.business-name {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  flex: 1;
}

.distance-tag {
  background-color: #e8f4ff;
  color: #1a73e8;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;
}

/* 评分区域 */
.info-rating {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

:deep(.el-rate) {
  margin: 0;
}

:deep(.el-rate__icon) {
  font-size: 18px;
}

.rate-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stars-value {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.review-count {
  color: #666;
  font-size: 14px;
}

/* 分类标签 */
.info-categories {
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.category {
  display: inline-block;
  background-color: #f5f7fa;
  color: #6e7072;
  font-size: 14px;
  padding: 6px 12px;
  border-radius: 20px;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 操作区域 */
.info-actions {
  margin-top: 8px;
}

:deep(.el-button--primary) {
  width: 100%;
  background: linear-gradient(to right, #409eff, #108ee9);
  border: none;
  font-weight: 500;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(to right, #66b1ff, #40a9ff);
  transform: translateY(-1px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nearby-container {
    padding: 12px;
  }

  .business-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .show-box {
    border-radius: 12px;
  }

  .img {
    height: 180px;
  }

  .info-box {
    padding: 16px;
  }

  .business-name {
    font-size: 18px;
  }

  .info-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .distance-tag {
    align-self: flex-end;
  }
}
</style>
