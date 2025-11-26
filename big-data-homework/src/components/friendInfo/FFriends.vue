<template>
  <div class="title">Friends</div>
  <template v-for="(friend, index) in friendInfo.findfriendVOS" :key="index">
    <div class="info-box">
      <img :src="defaultImg" class="img">
      <div class="mid-box">
        <div class="name">{{ friend.name }}</div>
        <div class="city">{{ friend.city }}</div>
        <div class="friends">
          <el-icon color="#f37325">
            <User/>
          </el-icon>
          <div class="number">{{ friend.friendNumbers }}</div>
          <div class="detail">friends</div>
        </div>
        <div class="reviews">
          <el-icon color="#f37325">
            <Star/>
          </el-icon>
          <div class="number">{{ friend.friendNumbers }}</div>
          <div class="detail">reviews</div>
        </div>
      </div>
    </div>
    <div class="line"></div>
  </template>
</template>

<script setup lang="ts">
import {computed, onMounted, onUnmounted, ref, toRefs} from "vue";
import UseFriendInfo from "@/hooks/UseFriendInfo";

// 默认图片路径
let defaultImg = 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/052c7073-c107-4267-9852-1a6d9d8284f5.jpg'

// 获取用户id
let props = defineProps(['userId'])
let userId = computed(() => props.userId)

// 使用朋友信息hook
let {friendInfo, getFriendInfo} = toRefs(UseFriendInfo())

onMounted(() => {
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

.info-box {
  display: flex;
}

.img {
  width: 90px;
  height: 90px;
}

.mid-box {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
  justify-content: start;
}

.name {
  color: #0073BB;
  font-size: 20px;
}

.city {
  color: #333333;
  font-size: 16px;
}

.line {
  height: 1px;
  width: 100%;
  background-color: rgb(235, 235, 235);
  margin-top: 15px;
  margin-bottom: 30px;
}

.number {
  margin-left: 10px;
  color: #111111;
}

.detail {
  margin-left: 10px;
  color: #666666;
}

.friends {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.reviews {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.right-box {
  display: flex;
  flex-direction: column;
  margin-left: auto;
  font-size: 16px;
}

.send-box {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #0073BB;
  margin-bottom: 10px;
}

.delete-box {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #0073BB;
}

.span {
  margin-left: 20px;
}
</style>