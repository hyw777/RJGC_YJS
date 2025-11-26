<template>
  <el-card class="popular">
    <div class="popular-head">
      <div class="popular-head-left">热度榜Top12</div>
      <div class="popular-head-right  color">全部</div>
    </div>
    <div class="popular-body">
      <img class="popular-body-info" v-for="(popImage,index) in popImages" :key="index" :src="popImage">
    </div>
  </el-card>
</template>

<script setup lang="ts">
import UsePopImg from "@/hooks/UsePopImg"
let {popImages} = UsePopImg()
</script>

<style scoped>
.popular {
  display: flex;
  margin: 0px 17.7vw;
  flex-direction: column;
}

.popular-head {
  display: flex;
  height: 70px;
  justify-content: space-between;
  align-items: center;
}

.popular-head-left {
  font-size: 20px;
  cursor: pointer;
  color: black;
  margin-left: 12px;
}

.popular-head-right {
  color: #333;
  font-size: 12px;
  cursor: pointer;
  margin-right: 12px;
}

.popular-body {
  height: 410px;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.popular-body-info {
  display: flex;
  width: 170px;
  height: 190px;
  background-color: aquamarine;
  cursor: pointer;
  margin: 12px 12px;
}
</style>