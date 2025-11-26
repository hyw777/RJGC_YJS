<template>
  <div class="merchant">
    <div class="img-box">
      <img class="img1" :src="result.imageList[0]" alt="img1">
      <img class="img2" :src="result.imageList[1]" alt="img2">
      <img class="img3" :src="result.imageList[2]" alt="img3">
    </div>
    <div class="info-box">
      <el-icon @click="cancle(result.businessId)" v-if="isCollected" size="50" color="#E00707" style="cursor: pointer;margin-right: 1vw"><StarFilled /></el-icon>
      <el-icon @click="collect(result.businessId)" v-else size="50" color="#E00707" style="cursor: pointer;margin-right: 1vw"><Star /></el-icon>
      <div class="mid">
        <span class="name">{{ result.name }}</span>
        <div class="rate-box">
          <el-rate size="large" v-model="result.stars" disabled/>
         <span style="margin-left: 10px;color: #FFFFFF">{{ result.stars }}</span>
          <span style="color: #FFFFFF;margin-left: 5px">({{ result.reviewCount }} reviews)</span>
        </div>
        <div class="info-bottom">
          <div style="color: #58B4FF">Location</div>
          <div class="state">{{ result.state }}</div>
          <div class="city">{{ result.city }}</div>
        </div>
        <div class="hours-box">
          <span class="hours-desc">Open</span>
          <span class="hours">{{ result.hours }}</span>
        </div>
        <div class="category-box">
          <span class="category-desc">Category</span>
          <span class="category">{{ result.categories }}</span>
        </div>
      </div>
      <el-button @click="jump" class="button">展示所有图片</el-button>
    </div>
    <div class="bottom">
      <div class="good">
        <span style="font-size: 35px">Q & A</span>
        <div class="good-line">
          <div class="qgood">Q : &nbsp;Is it good for kids ?</div>
          <div class="agood">A : &nbsp;{{ result.goodForKids }}</div>
        </div>
        <div class="card-line">
          <div class="qcard">Q : &nbsp;Does it accept credit cards?</div>
          <div class="acard">A : &nbsp;{{ result.businessAcceptsCreditcards == 'true' ? 'yes' : 'no' }}</div>
        </div>
      </div>
      <div class="lines"></div>
      <div class="bottom-right">
        <div class="title">
          <span>Reviews</span>
          <el-icon size="25" class="icon" @click="switchStatus"><EditPen /></el-icon>
        </div>
        <div class="input-box" v-if="isDisplayed">
          <el-rate class="rate" v-model="request.stars"></el-rate>
          <el-input v-model="request.text" class="input" rows="5" type="textarea" placeholder="Please write a review"></el-input>
          <el-button @click="writeReview(result.businessId,result.bid)" color="#E00707" type="primary">提交</el-button>
        </div>

        <div class="reviews-box" v-for="(reviews,index) in result.reviewVOList" :key="index">
          <div class="head-box">
            <span class="businessName">{{ reviews.userName }}</span>
          </div>
          <div class="rate-box">
            <div class="rate">
              <el-rate v-model="reviews.stars" disabled/>
            </div>
            <div class="date">{{ reviews.date }}</div>
          </div>
          <div class="text-box">
            <p class="text">{{ reviews.text }}</p>
          </div>
          <div class="icon-boxes">
            <div class="icon-box">
              <el-icon class="icons" @click="useful(reviews.reviewId,reviews.cool,reviews.useful,reviews.funny,index);satisfaction();">
                <Pointer/>
              </el-icon>
              <span>{{ reviews.useful }}</span>
            </div>
            <div class="icon-box">
              <el-icon class="icons" @click="funny(reviews.reviewId,reviews.cool,reviews.useful,reviews.funny,index);satisfaction();">
                <Sunrise/>
              </el-icon>
              <span>{{ reviews.funny }}</span>
            </div>
            <div class="icon-box">
              <el-icon class="icons" @click="cool(reviews.reviewId,reviews.cool,reviews.useful,reviews.funny,index);satisfaction();">
                <Headset/>
              </el-icon>
              <span>{{ reviews.cool }}</span>
            </div>
          </div>
          <div class="line"></div>
        </div>
      </div>
      <div class="lines2"></div>
    </div>
  </div>
</template>

<script setup lang="ts">

// 使用 ref 创建一个响应式集合来存储已隐藏的 reviewId
// 从 localStorage 获取已隐藏的 reviewId
import {log} from "echarts/types/src/util/log";

const storedHiddenIds = localStorage.getItem('hiddenReviewIds');
const hiddenReviewIds = ref(storedHiddenIds ? new Set(JSON.parse(storedHiddenIds)) : new Set<number>());

// 检查 reviewId 是否在 hiddenReviewIds 中
function isReviewHidden(reviewId: number) {
  return hiddenReviewIds.value.has(reviewId);
}

import {computed, onMounted, ref, toRefs} from "vue";
import { useBusiness } from "@/hooks/UseBusiness";
import {useRoute} from "vue-router";
import {StarFilled} from "@element-plus/icons-vue";
import {UseCollectStore} from "@/stores/UseCollectStore";
import axios from "axios";
import {router} from "@/router";
import {UseImageListStore} from "@/stores/UseImageListStore";
import {useWriteReviews} from "@/hooks/UseWriteReviews";
let collectStore = UseCollectStore()

let {result, getResult} = toRefs(useBusiness());
let route = useRoute()

let {isDisplayed,switchStatus,writeReview,request,satisfactionData,satisfaction} = useWriteReviews()
request.value.businessId = result.value.businessId
request.value.bid = result.value.bid
function cool(reviewId,cool,useful,funny,index) {
  satisfactionData.value.reviewId = reviewId
  satisfactionData.value.cool = 1
  satisfactionData.value.useful = 0
  satisfactionData.value.funny = 0
  result.value.reviewVOList[index].cool = cool + 1
}

function useful(reviewId,cool,useful,funny,index) {
  satisfactionData.value.reviewId = reviewId
  satisfactionData.value.cool = 0
  satisfactionData.value.useful = 1
  satisfactionData.value.funny = 0
  result.value.reviewVOList[index].useful = useful + 1
}

function funny(reviewId,cool,useful,funny,index) {
  satisfactionData.value.reviewId = reviewId
  satisfactionData.value.cool = 0
  satisfactionData.value.useful = 0
  satisfactionData.value.funny = 1
  result.value.reviewVOList[index].funny = funny + 1
}

let isCollected = computed(() => {
  return collectStore.collect.includes(result.value.businessId) ? true : false
});

let imageListStore = UseImageListStore()

function jump() {
  imageListStore.setImageList(result.value.imageList)
  router.push('/imageDisplay')
}

async function collect(businessId) {
  try {
    let res = await axios.post(`/api/collect/save?businessId=${businessId}`)
    ElMessage({
                message: res.data.data,
                type: 'success',
            })
    collectStore.pushCollect(businessId)
  }
  catch(error) {
    console.log(error)
  }
}

async function cancle(businessId) {
  try {
    let res = await axios.delete(`/api/collect/cancel?businessId=${businessId}`)
    ElMessage({
                message: res.data.data,
                type: 'success',
            })
    collectStore.removeCollect(businessId)
  }
  catch(error) {
    console.log(error)
  }
}

onMounted(() => {
  getResult.value(route.query.id);
});
</script>

<style scoped>
.button {
  background-color: rgb(0,0,0,0);
  font-size: 16px;
  color: #FFFFFF;
  width: 200px;
  height: 48px;
  margin-left: auto;
  margin-top: auto;
}

.merchant {
  display: flex;
  flex-direction: column;
}

.img-box {
  display: flex;
  height: 400px;
  position: relative; /* 设置相对定位 */
}

.img-box::after {
  content: "";
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  height: 50%;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0), rgba(0, 0, 0, 1));
  pointer-events: none; /* 确保覆盖层不影响其他元素的交互 */
}

.img1, .img2, .img3 {
  width: 33.33%;
}

.rate-box {
  display: flex;
  align-items: center;
}

.info-box {
  display: flex;
  position: absolute;
  left: 5%;
  top: 32%;
  width: 90%;
}

.mid {
  display: flex;
  flex-direction: column;
}

.name {
  font-size: 48px;
  font-weight: 700;
  color: #FFFFFF;
}

.info-bottom {
  display: flex;
  color: #FFFFFF;
}

.state {
  margin-left: 10px;
}

.city {
  margin-left: 20px;
}


.hours-desc {
  color: #04C585;
}

.hours-box {
  margin-top: 10px;
}

.hours {
  margin-left: 10px;
  color: #FFFFFF;
}

.category-box {
  margin-top: 10px;
}

.category-desc {
  color: yellow;
}

.category {
  margin-left: 10px;
  color: #FFFFFF;
}

.good {
  display: flex;
  flex-direction: column;
  padding: 20px;
  width: 35%;
  height: 280px;
  margin-top: 40px;
  font-size: 25px;
  border: 1px solid rgba(235, 235, 235, 1);
  border-radius: 5px;
  margin-left: 5%;
}

.good-line {
  margin-top: 20px;
  color: #6E7072;
}

.card-line {
  color: #6E7072;
  margin-top: 40px;
}

.title {
  margin-bottom: 10px;
}

.title span {
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
}

.icon {
  margin-left: 65%;
  cursor: pointer;
}

.reviews-box {
  display: flex;
  flex-direction: column;
  margin-bottom: 25px;
}

.head-box {
  display: flex;
  justify-content: space-between;
}

.businessName {
  color: #2D2E2F;
  font-size: 16px;
}

.rate-box {
  display: flex;
}

.date {
  color: #6E7072;
  font-size: 14px;
  display: flex;
  align-items: center;
  margin-left: 15px;
}

.line {
  height: 1px;
  width: 100%;
  background-color: rgb(235, 235, 235);
}

.text {
  color: #2D2E2F;
  font-size: 14px;
}

.icon-boxes {
  display: flex;
  margin: 14px 0px;
}

.icon-box {
  margin-right: 30px;
}

.icons {
  margin-right: 10px;
  cursor: pointer;
  font-size: 18px;
}

.bottom {
  display: flex;
}

.bottom-right {
  width: 30%;
  margin-top: 40px;
  margin-left: 5%;
  margin-right: 3.5%;
  overflow: auto;
  height: 450px;
}

.input {
  width: 100%;
  margin-bottom: 25px;
}

.input-box {
  margin-bottom: 50px;
}

.rate {
  margin-bottom: 10px;
}

.lines {
  width: 1px;
  background-color: rgb(235, 235, 235);
  margin-left: 13%;
}

.lines2 {
  width: 1px;
  background-color: rgb(235, 235, 235);
}
</style>
