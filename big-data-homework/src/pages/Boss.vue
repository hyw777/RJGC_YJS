<template>
  <div class="boss">
    <div class="nav-top">
      <router-link to="/" class="logo"></router-link>
      <span class="title">Merchant management platform</span>
    </div>
    <div class="mid">
      <div class="nav-left">
        <router-link to="/boss/information" replace class="info button" :class="{'isActive':bossButton == 0}">
          <el-icon class="icon"><House /></el-icon>
          <span class="button-title">Information</span>
        </router-link>
        <router-link to="/boss/editInfo" replace class="info button" :class="{'isActive':bossButton == 1}">
          <el-icon class="icon"><Edit /></el-icon>
          <span class="button-title">EditInfo</span>
        </router-link>
        <router-link to="/boss/workbench" replace class="workbench button" :class="{'isActive':bossButton == 2}">
          <el-icon class="icon"><Odometer /></el-icon>
          <span class="button-title">Workbench</span>
        </router-link>
        <router-link to="/boss/uploadImg" replace class="user-profile button" :class="{'isActive':bossButton == 4}">
          <el-icon class="icon"><Upload /></el-icon>
          <span class="button-title">Upload or delete</span>
        </router-link>
      </div>
      <router-view style="width: 80%;height: 100%"></router-view>
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
}

.nav-top {
  display: flex;
  align-items: center;
  height: 10%;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
}

.title {
  margin-left: 20px;
  font-size: 30px;
  font-weight: bold;
}

.button:hover {
  background-color: rgba(235, 235, 235, 1);
}

.mid {
  display: flex;
  height: 90%;
}

.logo {
  height: 40px;
  width: 4.5%;
  background-image: url("https://s3-media0.fl.yelpcdn.com/assets/public/logo_desktop.yji-0a2bf1d9c330d8747446.svg");
  background-size: contain;
  background-repeat: no-repeat;
  cursor: pointer;
  margin-left: 40px;
}

.isActive {
  background-color: rgba(235, 235, 235, 1);
}

.nav-left {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  width: 20%;
  border-right: 1px solid rgba(235, 235, 235, 1);
}

.button {
  display: flex;
  align-items: center;
  height: 10%;
  width: 100%;
  cursor: pointer;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  color: #2A3439;
  font-size: 20px;
  font-weight: 340;
}

.button-title {
  margin-left: 5%;
}

.icon {
  margin-left: 15%;
}

</style>