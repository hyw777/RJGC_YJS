<template>
  <div class="top">
    <div class="top-box">
      <div class="top-left">
        <div class="logo">
          <img class="logo" src="https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/logo.webp" alt="logo"/>
        </div>
      </div>
      <div class="top-right">
        <div class="top-right-login">
          <el-page-header @back="back()" title="返回" class="page-el">
            <template #content>
              <span class="font"> 登录 </span>
            </template>
          </el-page-header>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {router} from "@/router";

function back() {
  router.back()
}


</script>

<style scoped>
.top {
  height: 85px;
  background-color: #ffffff;
}

.top-box {
  display: flex;
  align-items: center;
  margin: 0 21vw;
}

.top-left {
  display: flex;
  align-items: center;
}

.top-right {
  margin-left: auto;
  margin-right: -40px;
}

.font {
  font-size: 23px;
  color: #666666;
}

</style>