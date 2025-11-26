<template>
  <div class="top-nav">
    <div class="top-nav-left">
      <div class="logo">
        <img class="logo" src="https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/logo.webp" alt="logo"/>
      </div>
      <div class="top-nav-left-search">
        <Input></Input>
      </div>
    </div>
    <div class="top-nav-right">
      <RouterLink to="/login" class="color">你好,请登录/注册</RouterLink>
      <RouterLink to="/center" class="color">个人中心</RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">

import Input from "@/components/common/Input.vue";
import {RouterLink} from "vue-router";
</script>

<style scoped>
.top-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 85px;
  background-color: #ffffff;
}

.top-nav-left {
  display: flex;
  align-items: center;
}

.top-nav-left-search {
  margin-left: 13.4vw;
}

.top-nav-right {
  margin-right: 330px;
}

.top-nav-right a {
  margin-left: 20px;
  font-size: 14px;
  color: black;
}
</style>