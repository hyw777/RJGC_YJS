<template>
  <div class="merchant">
    <div class="img-box">
        <img class="img1" :src="baseInfo.imageList[0]" alt="img1">
        <img class="img2" :src="baseInfo.imageList[1]" alt="img2">
        <img class="img3" :src="baseInfo.imageList[2]" alt="img3">
      <div class="info-box">
        <div class="mid">
          <span class="name">{{ baseInfo.name }}</span>
          <div class="rate-box">
            <el-rate size="large" v-model="baseInfo.stars" disabled/>
            <span style="margin-left: 10px;color: #FFFFFF">{{ baseInfo.stars }}</span>
            <span style="color: #FFFFFF;margin-left: 5px">({{ baseInfo.reviewCount }} reviews)</span>
          </div>
          <div class="info-bottom">
            <div style="color: #58B4FF">Location</div>
            <div class="state">{{ baseInfo.state }}</div>
            <div class="city">{{ baseInfo.city }}</div>
          </div>
          <div class="hours-box">
            <span class="hours-desc">Open</span>
            <span class="hours">{{ baseInfo.hours }}</span>
          </div>
          <div class="category-box">
            <span class="category-desc">Category</span>
            <span class="category">{{ baseInfo.categories }}</span>
          </div>
        </div>
        <el-button @click="jump" class="button">展示所有图片</el-button>
      </div>
    </div>
    <div class="bottom">
      <div class="good">
        <span style="font-size: 35px">Q & A</span>
        <div class="good-line">
          <div class="qgood">Q : &nbsp;Is it good for kids ?</div>
          <div class="agood">A : &nbsp;{{ baseInfo.goodForKids }}</div>
        </div>
        <div class="card-line">
          <div class="qcard">Q : &nbsp;Does it accept credit cards?</div>
          <div class="acard">A : &nbsp;{{ baseInfo.businessAcceptsCreditcards == 'true' ? 'yes' : 'no' }}</div>
        </div>
      </div>
      <div class="lines"></div>
      <div class="bottom-right">
        <div class="title">Reviews</div>
        <div class="reviews-box" v-for="(reviews,index) in baseInfo.reviewVOList" :key="index">
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
          <div class="line"></div>
        </div>
      </div>
      <div class="lines2"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, toRefs} from "vue";
import {useBaseInfo} from "@/hooks/UseBaseInfo";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {router} from "@/router";
import {UseImageListStore} from "@/stores/UseImageListStore";

// 使用按钮store
let buttonStore = UseButtonStore();

// 使用baseInfohook
let {baseInfo, getBaseInfo} = toRefs(useBaseInfo());

let imageListStore = UseImageListStore()

function jump() {
  imageListStore.setImageList(baseInfo.value.imageList)
  router.push('/imageDisplay')
}

onMounted(() => {
  buttonStore.setBossButton(0)
  getBaseInfo.value();
});
</script>

<style scoped>
.merchant {
  display: flex;
  flex-direction: column;
}

.img-box {
  display: flex;
  height: 40%;
  width: 80vw;
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
  height: 170px;
  width: 90%;
  position: absolute;
  left: 5%;
  top: 45%; /* 修改top值，使其相对于伪元素的位置 */
  z-index: 1; /* 确保info-box在伪元素上方 */
}

.mid {
  display: flex;
  flex-direction: column;
}

.button {
  background-color: rgb(0,0,0,0);
  font-size: 16px;
  color: #FFFFFF;
  width: 200px;
  height: 48px;
  margin-left: auto;
  margin-top: auto;
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
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 40px;
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

.bottom {
  height: 60%;
  display: flex;
  flex: 1;
}

.bottom-right {
  width: 30%;
  margin-top: 40px;
  margin-left: 5%;
  margin-right: 3.5%;
  overflow: auto;
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
