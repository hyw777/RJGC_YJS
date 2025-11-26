<template>
  <el-card class="high">
    <div class="high-head">
      <div class="high-head-left">高分榜Top12</div>
      <div class="high-head-right color">全部</div>
    </div>
    <div class="high-body">
      <img class="high-body-info" v-for="(highImage,index) in highImages" :key="index" :src="highImage">
    </div>
  </el-card>
</template>

<script setup lang="ts">
import UseHighImg from "@/hooks/UseHighImg"
let {highImages} = UseHighImg()
</script>

<style scoped>
.high {
  display: flex;
  background-color: white;
  margin: 0px 17.7vw;
  flex-direction: column;
}

.high-head {
  display: flex;
  height: 70px;
  justify-content: space-between;
  align-items: center;
}

.high-head-left {
  font-size: 20px;
  cursor: pointer;
  color: black;
  margin-left: 12px;
}

.high-head-right {
  color: #333;
  font-size: 12px;
  cursor: pointer;
  margin-right: 12px;
}

.high-body {
  height: 410px;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.high-body-info {
  display: flex;
  width: 170px;
  height: 190px;
  background-color: aquamarine;
  cursor: pointer;
  margin: 12px 12px;
}
</style>