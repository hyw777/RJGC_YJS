<template>
  <div class="boss">
    <div class="nav-top">
      <router-link to="/" class="logo">
        <el-icon size="40" color="#1890ff">
            <ShoppingCart />
          </el-icon>
      </router-link>
      <span class="title">商户管理平台</span>
      <div v-if="businessName" class="current-business">
        当前商户: {{ businessName }}
      </div>
    </div>
    <div class="mid">
      <div class="nav-left">
        <router-link to="/boss/information" replace class="info button" :class="{'isActive':bossButton == 0}">
          <el-icon class="icon"><House /></el-icon>
          <span class="button-title">商户信息</span>
        </router-link>
        <router-link to="/boss/editInfo" replace class="info button" :class="{'isActive':bossButton == 1}">
          <el-icon class="icon"><Edit /></el-icon>
          <span class="button-title">编辑信息</span>
        </router-link>
        <router-link to="/boss/workbench" replace class="workbench button" :class="{'isActive':bossButton == 2}">
          <el-icon class="icon"><Odometer /></el-icon>
          <span class="button-title">工作台</span>
        </router-link>
        <router-link to="/boss/uploadImg" replace class="user-profile button" :class="{'isActive':bossButton == 4}">
          <el-icon class="icon"><Upload /></el-icon>
          <span class="button-title">上传图片</span>
        </router-link>
      </div>
      <router-view 
        class="content-area"
        :business-id="businessId"
        :key="businessId"
      ></router-view>
    </div>
  </div>
</template>

<script setup lang="ts">
import { UseButtonStore } from '@/stores/UseButtonStore';
import { computed, ref, watch } from "vue";
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

let buttonStore = UseButtonStore()
let bossButton = computed(()=>buttonStore.bossButton)

// 获取路由参数
const route = useRoute();
const router = useRouter();
const businessId = ref<string | null>(null);
const businessName = ref<string>('');

// 监听路由变化
watch(
  () => route.query.businessId,
  async (newBusinessId) => {
    if (newBusinessId) {
      businessId.value = newBusinessId as string;
      // 获取商户名称
      try {
        const response = await axios.get('/api/business/businessInfo', {
          params: {
            bId: businessId.value
          }
        });
        if (response.data.code === 200) {
          businessName.value = response.data.data.name;
        }
      } catch (error) {
        console.error("获取商户信息失败:", error);
      }
    }
  },
  { immediate: true }
);

// 在路由变化时确保子组件能接收到 businessId
watch(
  () => route,
  (newRoute) => {
    // 什么都不做，只是确保路由变化被监听
  },
  { immediate: true }
);
</script>

<style scoped>
.boss {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.nav-top {
  display: flex;
  align-items: center;
  height: 80px;
  border-bottom: 1px solid #dee2e6;
  background: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 0 40px;
  flex-shrink: 0; /* 防止在空间不足时缩小 */
}

.title {
  margin-left: 20px;
  font-size: 28px;
  font-weight: 700;
  color: #343a40;
  letter-spacing: 0.5px;
}

.current-business {
  margin-left: auto;
  font-size: 16px;
  color: #6c757d;
  background-color: #e9ecef;
  padding: 6px 12px;
  border-radius: 20px;
}

.button:hover {
  background-color: #f1f3f5;
  transition: all 0.3s ease;
}

.mid {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.logo {
  height: 40px;
  width: 120px;
  /* background-image: url("https://s3-media0.fl.yelpcdn.com/assets/public/logo_desktop.yji-0a2bf1d9c330d8747446.svg"); */
  background-size: contain;
  background-repeat: no-repeat;
  cursor: pointer;
}

.isActive {
  background-color: #f8f9fa;
  border-left: 4px solid #6c757d;
  font-weight: 600;
  color: #495057;
}

.nav-left {
  display: flex;
  flex-direction: column;
  width: 240px;
  background: white;
  border-right: 1px solid #dee2e6;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  z-index: 10;
  overflow: hidden; /* 防止子元素悬停时超出容器边界 */
  flex-shrink: 0; /* 防止在空间不足时缩小 */
}

.button {
  display: flex;
  align-items: center;
  height: 60px;
  width: calc(100% - 1px); /* 略微减少宽度以确保不会超出边界 */
  cursor: pointer;
  color: #6c757d;
  font-size: 16px;
  font-weight: 400;
  padding: 0 24px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.button:not(:last-child) {
  border-bottom: 1px solid #f1f3f5;
}

.button:hover {
  background-color: #f1f3f5;
  color: #495057;
  transform: none;
  overflow: hidden; /* 确保悬停时内容不会超出边界 */
}

.button-title {
  margin-left: 16px;
}

.icon {
  margin-left: 0;
  font-size: 18px;
  width: 24px;
  text-align: center;
}

.content-area {
  flex: 1;
  height: calc(100vh - 80px); /* 减去顶部导航栏高度 */
  overflow: auto;
  padding: 20px;
  box-sizing: border-box;
}
</style>