<template>
  <div class="container">
    <div class="message" v-for="(n,index) in news" :key="index">
      <div class="time">{{n.time}}</div>
      <div class="content">{{n.content}}</div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {onMounted, toRefs} from "vue";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {useNews} from "@/hooks/UseNews";

let {news, getNews} = toRefs(useNews())
let buttonStore = UseButtonStore()

onMounted(() => {
  buttonStore.setIndexButton(2)
  getNews.value()
})
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  margin: 0;
  padding: 20px;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: start;
  height: 400px;
  width: 100%;
  overflow: auto;
  border: 1px solid rgba(235, 235, 235, 1);
  padding: 20px;
}

.message {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}
.time {
  font-size: 14px;
  color: #888888;
  margin-bottom: 10px;
}
.content {
  font-size: 16px;
  color: #333333;
}
</style>