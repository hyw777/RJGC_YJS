<template>
  <div class="boss">
    <div class="nav-top">
      <router-link to="/" class="logo">
        <el-icon size="40" color="#1890ff">
            <ShoppingCart />
          </el-icon>
      </router-link>
      <span class="title">Merchant Management Platform</span>
    </div>
    <div class="mid">
      <div class="nav-left">
        <router-link to="/boss/information" replace class="info button" :class="{'isActive':bossButton == 0}">
          <el-icon class="icon"><House /></el-icon>
          <span class="button-title">Information</span>
        </router-link>
        <router-link to="/boss/editInfo" replace class="info button" :class="{'isActive':bossButton == 1}">
          <el-icon class="icon"><Edit /></el-icon>
          <span class="button-title">Edit Information</span>
        </router-link>
        <router-link to="/boss/workbench" replace class="workbench button" :class="{'isActive':bossButton == 2}">
          <el-icon class="icon"><Odometer /></el-icon>
          <span class="button-title">Workbench</span>
        </router-link>
        <router-link to="/boss/uploadImg" replace class="user-profile button" :class="{'isActive':bossButton == 4}">
          <el-icon class="icon"><Upload /></el-icon>
          <span class="button-title">Upload Images</span>
        </router-link>
      </div>
      <router-view style="flex: 1; height: 100%; overflow: auto;"></router-view>
    </div>
  </div>
</template>

<script setup lang="ts">
import { UseButtonStore } from '@/stores/UseButtonStore';
import {computed} from "vue";
let buttonStore = UseButtonStore()
let bossButton = computed(()=>buttonStore.bossButton)
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
}

.title {
  margin-left: 20px;
  font-size: 28px;
  font-weight: 700;
  color: #343a40;
  letter-spacing: 0.5px;
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
}

.button {
  display: flex;
  align-items: center;
  height: 60px;
  width: 100%;
  cursor: pointer;
  color: #6c757d;
  font-size: 16px;
  font-weight: 400;
  padding: 0 24px;
  transition: all 0.3s ease;
  position: relative;
}

.button:not(:last-child) {
  border-bottom: 1px solid #f1f3f5;
}

.button:hover {
  background-color: #f1f3f5;
  color: #495057;
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

</style>