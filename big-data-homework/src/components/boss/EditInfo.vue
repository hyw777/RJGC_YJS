<template>
  <div class="edit-info">
    <!-- 添加加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <el-icon class="is-loading" size="48"><Loading /></el-icon>
        <p>数据提交中...</p>
      </div>
    </div>

    <div class="form-card">
      <h2 class="card-title">商家信息编辑</h2>
      <el-form :model="info" label-width="auto">
        <!-- 两列布局容器 -->
        <div class="form-columns">
          <div class="form-column">
            <el-form-item label="商家名" prop="name">
              <el-input v-model="info.name" />
            </el-form-item>

            <el-form-item label="地址" prop="address">
              <el-input
                v-model="info.address"
                @input="autoFillCoordinates"
                placeholder="请输入详细地址"
              />
            </el-form-item>

            <!-- 城市和州在同一排 -->
            <div class="form-row">
              <el-form-item label="城市" prop="city" class="form-item-half">
                <el-input v-model="info.city" />
              </el-form-item>
              
              <el-form-item label="州" prop="state" class="form-item-half">
                <el-input v-model="info.state" />
              </el-form-item>
            </div>

            <!-- 邮政编码、纬度和经度在同一排 -->
            <div class="form-row">
              <el-form-item label="邮政编码" prop="postalCode" class="form-item-third">
                <el-input v-model="info.postalCode" />
              </el-form-item>
              
              <el-form-item label="纬度" prop="latitude" class="form-item-third">
                <el-input-number
                  v-model="info.latitude"
                  :precision="8"
                  disabled
                />
              </el-form-item>
              
              <el-form-item label="经度" prop="longitude" class="form-item-third">
                <el-input-number
                  v-model="info.longitude"
                  :precision="8"
                  disabled
                />
              </el-form-item>
            </div>

            <el-form-item label="分类" prop="categories">
              <el-input v-model="info.categories" />
            </el-form-item>

            <el-form-item label="营业时间" prop="hours">
              <div
                v-for="(time, day) in businessHours"
                :key="day"
                class="hours-item"
              >
                <span class="day-label">{{ day }}:</span>
                <el-time-picker
                  v-model="businessHours[day].start"
                  placeholder="开始时间"
                  format="HH:mm"
                  value-format="HH:mm"
                  width="80px"
                />
                <span class="separator">-</span>
                <el-time-picker
                  v-model="businessHours[day].end"
                  placeholder="结束时间"
                  format="HH:mm"
                  value-format="HH:mm"
                  width="80px"
                />
              </div>
            </el-form-item>
          </div>

          <div class="form-column">
            <el-form-item label="商户属性" prop="attributes">
              <div class="attributes-container">
                <!-- WiFi选项 -->
                <div class="attribute-item">
                  <el-checkbox
                    v-model="selectedAttributes"
                    label="WiFi"
                    class="main-checkbox"
                  >
                    提供WiFi
                  </el-checkbox>
                  <el-select
                    v-model="wifiOption"
                    placeholder="WiFi类型"
                    size="small"
                    class="attribute-select"
                    :disabled="!selectedAttributes.includes('WiFi')"
                  >
                    <el-option label="免费WiFi" value="u'free'"></el-option>
                    <el-option label="无WiFi" value="u'no'"></el-option>
                  </el-select>
                </div>

                <!-- Alcohol选项 -->
                <div class="attribute-item">
                  <el-checkbox
                    v-model="selectedAttributes"
                    label="Alcohol"
                    class="main-checkbox"
                  >
                    提供酒精饮品
                  </el-checkbox>
                  <el-select
                    v-model="alcoholOption"
                    placeholder="酒水类型"
                    size="small"
                    class="attribute-select"
                    :disabled="!selectedAttributes.includes('Alcohol')"
                  >
                    <el-option
                      label="啤酒和葡萄酒"
                      value="'beer_and_wine'"
                    ></el-option>
                    <el-option label="不提供" value="'none'"></el-option>
                  </el-select>
                </div>

                <!-- 其他选项，每行显示两个 -->
                <div class="checkbox-grid">
                  <div class="checkbox-row">
                    <el-checkbox v-model="selectedAttributes" label="OutdoorSeating">
                      户外座位
                    </el-checkbox>
                    <el-checkbox v-model="selectedAttributes" label="RestaurantsTakeOut">
                      提供外卖
                    </el-checkbox>
                  </div>
                  <div class="checkbox-row">
                    <el-checkbox v-model="selectedAttributes" label="RestaurantsDelivery">
                      提供外带
                    </el-checkbox>
                    <el-checkbox v-model="selectedAttributes" label="BusinessAcceptsCreditCards">
                      接受信用卡
                    </el-checkbox>
                  </div>
                  <div class="checkbox-row">
                    <el-checkbox v-model="selectedAttributes" label="DogsAllowed">
                      允许宠物入内
                    </el-checkbox>
                    <el-checkbox v-model="selectedAttributes" label="GoodForKids">
                      适合儿童
                    </el-checkbox>
                  </div>
                </div>
              </div>
            </el-form-item>
          </div>
        </div>
        
        <div class="form-actions">
          <el-button 
            @click="handleSubmit" 
            type="primary" 
            class="submit-btn"
          >
            提交
          </el-button>
          <el-button 
            @click="writeBcak" 
            class="reset-btn"
          >
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, watch, ref} from "vue";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {useEditMerchant} from "@/hooks/UseEditMerchant";
import { useRoute } from 'vue-router';
import { Loading } from '@element-plus/icons-vue';
import axios from "axios";

// 定义 props
const props = defineProps<{
  businessId?: string
}>()

let buttonStore = UseButtonStore();

let {info, editInfo, writeBcak} = useEditMerchant(); // 不使用 toRefs 包装自定义 hook 返回的对象

// 获取路由参数
const route = useRoute();

// 添加加载状态
const loading = ref(false);

// 定义一周的天数
const weekDays = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

// 营业时间数据结构
const businessHours = ref({
  Monday: { start: "", end: "" },
  Tuesday: { start: "", end: "" },
  Wednesday: { start: "", end: "" },
  Thursday: { start: "", end: "" },
  Friday: { start: "", end: "" },
  Saturday: { start: "", end: "" },
  Sunday: { start: "", end: "" },
});

// 属性相关数据
const selectedAttributes = ref<string[]>([]);
const wifiOption = ref("u'free'");
const alcoholOption = ref("'beer_and_wine'");

// 格式化营业时间为 JSON 字符串
function formatBusinessHours() {
  const hoursObj = {};
  for (const day of weekDays) {
    const timeSlot = businessHours.value[day];
    if (timeSlot.start && timeSlot.end) {
      hoursObj[day] = `${timeSlot.start}-${timeSlot.end}`;
    }
  }
  return JSON.stringify(hoursObj);
}

// 格式化 attributes 为 JSON 字符串
function formatAttributes() {
  const attrs: Record<string, string> = {};

  // 处理 WiFi 选项
  if (selectedAttributes.value.includes("WiFi")) {
    attrs["WiFi"] = wifiOption.value;
  } else {
    attrs["WiFi"] = "u'no'";
  }

  // 处理酒精饮品选项
  if (selectedAttributes.value.includes("Alcohol")) {
    attrs["Alcohol"] = alcoholOption.value;
  } else {
    attrs["Alcohol"] = "'none'";
  }

  // 处理其他布尔型属性
  const booleanAttrs = [
    "OutdoorSeating",
    "RestaurantsTakeOut",
    "RestaurantsDelivery",
    "BusinessAcceptsCreditCards",
    "DogsAllowed",
    "GoodForKids",
  ];

  booleanAttrs.forEach((attr) => {
    if (selectedAttributes.value.includes(attr)) {
      attrs[attr] = "True";
    } else {
      attrs[attr] = "False";
    }
  });

  return JSON.stringify(attrs);
}

// 处理提交事件
async function handleSubmit() {
  loading.value = true;
  try {
    // 格式化营业时间
    info.value.hours = formatBusinessHours();

    // 格式化商户属性
    info.value.attributes = formatAttributes();

    // 调用原始的提交函数
    await editInfo();
  } finally {
    loading.value = false;
  }
}

// 根据地址获取经纬度
async function getAddressCoordinates(address: string) {
  try {
    // 使用高德地图API（需要申请Key）
    const response = await axios.get(
      "/gpi/v3/geocode/geo",
      {
        params: {
          key: "96b27b572a0e16d551ac65dd0e02f65f", // 需要申请的API Key
          address: address,
          city: info.value.city || "城市名", // 可选，指定城市
        },
      }
    );

    if (response.data.status === "1" && response.data.geocodes.length > 0) {
      const geocode = response.data.geocodes[0];
      return {
        latitude: geocode.location.split(",")[1],
        longitude: geocode.location.split(",")[0],
      };
    }

    return null;
  } catch (error) {
    console.error("获取坐标失败:", error);
    return null;
  }
}

// 自动填充坐标
async function autoFillCoordinates() {
  if (!info.value.address || info.value.address.length < 5) {
    return;
  }
  
  // 短暂延迟，避免频繁请求
  setTimeout(async () => {
    const coordinates = await getAddressCoordinates(info.value.address);
    if (coordinates) {
      info.value.latitude = parseFloat(coordinates.latitude);
      info.value.longitude = parseFloat(coordinates.longitude);
    }
  }, 500);
}

// 监听路由变化，确保在切换标签时能重新加载数据
watch(
  () => props.businessId || route.query.businessId,
  (newBusinessId) => {
    if (newBusinessId) {
      writeBcak(newBusinessId as string);
    }
  },
  { immediate: true }
);

onMounted(() => {
  buttonStore.setBossButton(1);
  // 从props或路由参数中获取 businessId 并传递给 writeBcak 函数
  const businessId = props.businessId || (route.query.businessId as string);
  console.log("businessId:"+businessId)
  if (businessId) {
    writeBcak(businessId);
  }
})
</script>

<style scoped>
/* 添加加载样式 */
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

/* 添加旋转动画样式 - 仅应用于指定的加载图标 */
.loading-overlay .is-loading {
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

.edit-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100%;
  width: 100%;
  box-sizing: border-box;
}

.form-card {
  width: 100%;
  max-width: 1400px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: box-shadow 0.3s ease;
  box-sizing: border-box;
}

.form-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 32px 0;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
  text-align: center;
}

/* 两列布局 */
.form-columns {
  display: flex;
  gap: 40px;
}

.form-column {
  flex: 1;
}

/* 表单行布局 */
.form-row {
  display: flex;
  gap: 20px;
  width: 100%;
}

.form-item-half {
  flex: 1;
}

.form-item-third {
  flex: 1;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 40px;
}

.submit-btn {
  background-color: #1890ff !important;
  border-color: #1890ff !important;
  padding: 12px 32px;
  font-size: 16px;
  min-width: 120px;
}

.reset-btn {
  padding: 12px 32px;
  font-size: 16px;
  color: #6c757d;
  border-color: #6c757d;
  min-width: 120px;
}

.reset-btn:hover {
  background-color: #f8f9fa;
  color: #495057;
  border-color: #495057;
}

:deep(.el-form-item__label) {
  color: #495057;
  font-weight: 500;
}

:deep(.el-input__inner) {
  border-radius: 6px;
  border-color: #dee2e6;
  transition: border-color 0.3s ease;
  width: 100%;
}

:deep(.el-input__inner:hover) {
  border-color: #adb5bd;
}

:deep(.el-input__inner:focus) {
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.1);
}

:deep(.el-radio__inner) {
  border-color: #dee2e6;
}

:deep(.el-radio__inner:hover) {
  border-color: #1890ff;
}

:deep(.el-radio__input.is-checked .el-radio__inner) {
  border-color: #1890ff;
  background-color: #1890ff;
}

:deep(.el-radio__label) {
  color: #495057;
}

/* 添加以下样式 */
.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 90px !important;
  font-weight: 500;
  color: #666;
}

.separator {
  margin: 0 10px;
  color: #999;
}

.attributes-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.attribute-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.main-checkbox {
  margin-right: 15px !important;
}

.attribute-select {
  width: 160px;
  margin-left: 8px;
}

.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.merchant-dialog :deep(.el-dialog__body) {
  padding: 20px 30px;
}

/* 调整表单项间距和整体布局 */
.merchant-dialog :deep(.el-form-item) {
  margin-bottom: 20px;
}

.merchant-dialog :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
}

.attribute-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.main-checkbox {
  margin-right: 15px !important;
}

.attribute-select {
  width: 160px;
}

.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 80px;
  font-weight: 500;
  color: #666;
}

.separator {
  margin: 0 10px;
  color: #999;
}

/* 强制设置时间选择器宽度 */
.el-time-picker {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

:deep(.el-time-picker) {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 10px;
}

.checkbox-with-select {
  margin-left: 20px;
  margin-bottom: 10px;
}

.checkbox-with-select .el-select {
  width: 150px;
  margin-top: 5px;
}

.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 80px;
  font-weight: 500;
  color: #666;
}

.separator {
  margin: 0 10px;
  color: #999;
}

/* 添加网格布局样式 */
.checkbox-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

.checkbox-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.checkbox-row :deep(.el-checkbox) {
  margin-right: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .edit-info {
    padding: 10px;
  }
  
  .form-card {
    padding: 20px;
    max-width: 100%;
  }
  
  .day-label {
    width: 70px !important;
  }
  
  .attribute-select {
    width: 120px;
  }
  
  .form-actions {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }
  
  .submit-btn,
  .reset-btn {
    width: 100%;
    max-width: 200px;
  }
  
  .checkbox-row {
    grid-template-columns: 1fr;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  
  /* 在小屏幕上改为单列布局 */
  .form-columns {
    flex-direction: column;
    gap: 0;
  }
}

/* 中等屏幕适配 */
@media (min-width: 769px) and (max-width: 1200px) {
  .form-card {
    max-width: 1100px;
  }
}
</style>