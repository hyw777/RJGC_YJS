<template>
  <div class="title">Reviews</div>
  <div class="reviews-box" v-for="(reviews,index) in userInfo.reviewsList" :key="index">
    <div class="head-box">
      <span class="businessName">{{ reviews.businessName }}</span>
      <el-icon size="20" class="delete" color="#E00707" @click="dialogVisible = true">
        <Delete/>
      </el-icon>
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
    <el-dialog
        v-model="dialogVisible"
        title="Warning!"
        width="500"
    >
      <span>Are you sure to delete this reviews?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="dialogVisible = false;deleteReviews(reviews.reviewId)">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>


</template>

<script setup lang="ts">
import {computed, onMounted, ref, toRefs} from "vue";
import UseReviewsDelete from "@/hooks/UseReviewsDelete";
import UseUserInfo from "@/hooks/UseUserInfo";

// 使用用户信息hook
let {userInfo,getUserInfo} = toRefs(UseUserInfo());

// 使用删除评论hooks
let reviewsDelete = UseReviewsDelete()
let {deleteReviews} = reviewsDelete

// 对话框展示
let dialogVisible = ref(false)

onMounted(()=>{
  getUserInfo.value()
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