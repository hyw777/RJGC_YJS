<template>
  <div class="title">Reviews</div>
  <div class="reviews-box" v-for="(reviews,index) in friendInfo.reviewsList" :key="index">
    <div class="head-box">
      <span class="businessName">{{ reviews.businessName }}</span>
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


</template>

<script setup lang="ts">
import {computed, onMounted, ref, toRefs} from "vue";
import UseFriendInfo from "@/hooks/UseFriendInfo";
// 获取用户id
let props = defineProps(['userId'])
let userId = computed(() => props.userId)

// 使用朋友信息hook
let {friendInfo,getFriendInfo} = toRefs(UseFriendInfo())


onMounted(()=>{
  getFriendInfo.value(userId.value)
})
</script>

<style scoped>
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

.delete {
  cursor: pointer;
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
</style>