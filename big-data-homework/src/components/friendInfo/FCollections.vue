<template>
  <div class="title">Collections</div>
  <template v-for="(collection, index) in friendInfo.collectionVOList" :key="index">
    <div class="collections">
      <img :src="`http://localhost:8080/images/${collection.businessVO1.image}`" class="img">
      <div class="info-box">
        <div class="name">{{ collection.businessVO1.name }}</div>
        <div class="rate">
          <el-rate v-model="collection.businessVO1.stars" disabled/>
          <div class="reviewCount">({{ collection.businessVO1.reviewCount }}reviews)</div>
        </div>
        <div class="categories">{{ collection.businessVO1.categories }}</div>
        <div class="date">{{ collection.date }}</div>
      </div>
    </div>
    <div class="line"></div>
  </template>
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

.collections {
  display: flex;
}

.img {
  width: 90px;
  height: 90px;
}

.name {
  color: #0073BB;
  font-size: 14px;
  cursor: pointer;
}

.categories {
  color: #0073BB;
  font-size: 12px;
  cursor: pointer;
}

.line {
  height: 1px;
  width: 100%;
  background-color: rgb(235, 235, 235);
  margin-top: 15px;
  margin-bottom: 30px;
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: end;
  margin-left: 20px;
}

.rate {
  display: flex;
  color: #2D2E2F;
}

.icon {
  margin-left: auto;
  cursor: pointer;
}

.date {
  color: #999999;
}
</style>