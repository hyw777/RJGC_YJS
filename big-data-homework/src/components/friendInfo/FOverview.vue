<template>
    <div class="title-box">
      <div class="title">Profile overview</div>
    </div>
    <div class="data">name:&nbsp;&nbsp;{{ friendInfo.userVO.name }}</div>
    <div class="data">age:&nbsp;&nbsp;{{ friendInfo.userVO.age }}</div>
    <div class="data">gender:&nbsp;&nbsp;{{ friendInfo.userVO.gender }}</div>
    <div class="data">city:&nbsp;&nbsp;{{ friendInfo.userVO.city }}</div>
    <div class="data">email:&nbsp;&nbsp;{{ friendInfo.userVO.email }}</div>
    <div class="data">reviewCount:&nbsp;&nbsp;{{ friendInfo.userVO.reviewCount }}</div>
    <div class="data">cool:&nbsp;&nbsp;{{ friendInfo.userVO.cool }}</div>
    <div class="data">useful:&nbsp;&nbsp;{{ friendInfo.userVO.useful }}</div>
    <div class="data">elite:&nbsp;&nbsp;{{ friendInfo.userVO.elite }}</div>
    <div class="data">yelpingSince:&nbsp;&nbsp;{{ friendInfo.userVO.yelpingSince }}</div>
    <div class="data">funny:&nbsp;&nbsp;{{ friendInfo.userVO.funny }}</div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, toRef, toRefs, watch, watchEffect} from 'vue';
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
.title-box {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.title {
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
}

.edit {
  color: #6E7072;
  font-size: 28px;
  cursor: pointer;
  display: flex;
  align-items: end;
}

.back {
  color: #6E7072;
  font-size: 28px;
  align-items: start;
  cursor: pointer;
  margin-left: auto;
}

.el-box {
  display: flex;
}

.submit {
  margin-top: 20px;
}

</style>