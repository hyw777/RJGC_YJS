<template>
  <div class="head">
    <div class="box">
      <img class="logo" src="https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/logo.webp" alt="logo"/>
      <div class="username">{{ username }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

let username = ref('斯蒂芬库里')
</script>

<style scoped>
.head {
  background: #313131;
  display: flex;
}

.box {
  display: flex;
  align-items: center;
  flex-grow: 1;
  margin: 0 21vw;
}

.username {
  font-size: 20px;
  color: #999999;
  margin-left: auto;
  letter-spacing: 2px;
}

.username::before {
  content: "欢迎,";
  color: #999999;
  font-size: 15px;
}
</style>