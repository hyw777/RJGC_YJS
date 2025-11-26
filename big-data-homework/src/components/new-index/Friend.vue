<template>
  <div class="content-card" v-for="(review,index) in friendData.reviewVOS" :key="index">
    <div class="profile">
      <div class="pro-box">
        <div class="avatar">
          <el-avatar :size="40" :src="defaultAvatar"/>
        </div>
        <div class="detail">
          <div class="username">{{ review.userName }}</div>
          <div class="time">{{ review.date }}</div>
        </div>
      </div>
    </div>
    <div class="line-box">
      <div class="line"></div>
    </div>
    <div class="show">
      <div class="title">
        <a>{{ review.businessName }}</a>
      </div>
      <div class="rate">
        <el-rate v-model="review.stars" :colors="colors" disabled/>
      </div>
      <div class="text" :class="{'expanded':isExpanded[index]}" :ref="el => textContentRefs[index] = el">
        {{ review.text }}
      </div>
      <div @click="expand(index)" v-if="showExpandButton[index]" class="show-more">
        {{ isExpanded[index] ? 'Show less' : 'Read more' }}
      </div>
    </div>
    <div class="down">
      <div class="down-box">
        <div class="icon-box">
          <el-icon class="icon">
            <Pointer/>
          </el-icon>
          <span>{{ review.useful }}</span>
        </div>
        <div class="icon-box">
          <el-icon class="icon">
            <Sunrise/>
          </el-icon>
          <span>{{ review.funny }}</span>
        </div>
        <div class="icon-box">
          <el-icon class="icon">
            <Headset/>
          </el-icon>
          <span>{{ review.cool }}</span>
        </div>
      </div>
    </div>
  </div>
  <div class="content-card" v-for="(collection,index) in friendData.collectionVOS" :key="index">
    <div class="profile">
      <div class="pro-box">
        <div class="avatar">
          <el-avatar :size="40" src="defaultAvatar"/>
        </div>
        <div class="detail">
          <div class="username">{{ collection.userName }}</div>
          <div class="time">{{ collection.date }}</div>
        </div>
      </div>
    </div>
    <div class="show-box">
      <router-link class="img" :to="{path:'/merchantDetail',query:{id:collection.businessVO1.businessId}}">
        <img :src="filePath(collection.businessVO1.image)">
      </router-link>
      <div class="info-box">
        <div class="info1">
          <span>{{ collection.businessVO1.name }}</span>
        </div>
        <div class="info2">
          <el-rate v-model="collection.businessVO1.stars" size="large" disabled></el-rate>
          <div class="rate-box">
            <div style="margin-right: 4%">{{ collection.businessVO1.stars }}</div>
            <span>({{ collection.businessVO1.reviewCount }} reviews)</span>
          </div>
        </div>
        <div class="info3">
          <span>{{ collection.businessVO1.categories }}</span>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup lang="ts">

import {onMounted, toRefs, ref, computed, nextTick} from "vue";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {useFriendActivity} from "@/hooks/UseFriendActivity";

let defaultAvatar = 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/OIP-C.jpg'
let buttonStore = UseButtonStore()
let {friendData, getFriendData, formatDateTime} = toRefs(useFriendActivity())
let contents = ref([
  {
    avatar: 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/4.jpg',
    username: 'Cxk',
    time: '1 hour ago',
    title: '麻豆传媒',
    rate: '5',
    images: [
      'https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar: 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/070e9ea0-0a81-41ad-a5ec-a586b7790610.jpg',
    username: 'Cxk',
    time: '1 hour ago',
    title: '麻豆传媒',
    rate: '2',
    images: [
      'https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar: 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/8d35ede9-a663-4de8-b39c-3382e7a3d033.jpg',
    username: 'Cxk',
    time: '1 hour ago',
    title: '麻豆传媒',
    rate: '1',
    images: [
      'https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar: 'https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/7db0cdc3-7b56-4dac-ade3-7fc073cb58d0.jpg',
    username: 'Cxk',
    time: '1 hour ago',
    title: '麻豆传媒',
    rate: '4',
    images: [
      'https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
])

const textContentRefs = ref([]);
const isExpanded = ref([]);
const showExpandButton = ref([]);

function expand(index) {
  isExpanded.value[index] = !isExpanded.value[index];
}

const filePath = (file) => {
  if (file == null) {
    return ``
  }
  return file.includes('http') ? file : `/api/images/${file}`;
}

const colors = ref(['#99A9BF', '#F7BA2A', '#FF9900']) // same as { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }

onMounted(async () => {
  buttonStore.setIndexButton(1)
  getFriendData.value()

  if (friendData.value.reviewVOS.length > 3) {
    friendData.value.reviewVOS = friendData.value.reviewVOS.slice(0, 3)
  }
  if (friendData.value.collectionVOS.length > 3) {
    friendData.value.collectionVOS = friendData.value.collectionVOS.slice(0, 3)
  }
  friendData.value.reviewVOS.forEach((review) => {
    review.date = formatDateTime.value(review.date);
  });
  friendData.value.collectionVOS.forEach((collection) => {
    collection.date = formatDateTime.value(collection.date);
  });

  // 初始化 isExpanded 和 showExpandButton
  friendData.value.reviewVOS.forEach((review, index) => {
    isExpanded.value[index] = false;
    showExpandButton.value[index] = false;
  });

  textContentRefs.value.forEach((content, index) => {
    if (content.scrollHeight > content.clientHeight) {
      showExpandButton.value[index] = true;
    } else {
      showExpandButton.value[index] = false;
    }
  });
});
</script>

<style scoped>
.content-card {
  display: flex;
  flex-direction: column;
  width: 30%;
  margin-bottom: 55px;
  margin-right: 3%;
}

.profile {
  display: flex;
  border: 1px solid rgb(235, 235, 235);
  border-bottom: none;
}

.pro-box {
  display: flex;
  padding: 16px 16px 0px 16px;
}

.avatar {
  cursor: pointer;
}

.detail {
  margin-left: 10px;
}

.username {
  font-size: 14px;
  color: #2D2E2F;
  font-weight: 600;
  line-height: 20px;
}

.time {
  font-size: 12px;
  color: #6E7072;
  font-weight: 400;
  line-height: 16px;
}

.show {
  display: flex;
  flex-direction: column;
  padding: 16px 16px 16px 16px;
  border: 1px solid rgb(235, 235, 235);
  border-top: none;
  border-bottom: none;
}

.down {
  display: flex;
  padding: 0px 16px;
  border: 1px solid rgb(235, 235, 235);
  height: 55px;
  border-top: none;
}

.down-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  width: 100%;
  padding: 0px 16px;
  border-top: 1px solid rgb(235, 235, 235);
  font-size: 14px;
  color: #2D2E2F;
}

.icon-box {
  display: flex;
  align-items: center;
}

.icon {
  font-size: 24px;
  margin-right: 5px;
}

.title {
  color: #2D2E2F;
  font-size: 16px;
  font-weight: 600;
}

.text {
  width: 100%;
  max-height: 40px;
  color: #2D2E2F;
  font-size: 14px;
  overflow: hidden;
}

.expanded {
  max-height: none;
}

.show-more {
  color: #027A97;
  font-size: 14px;
  cursor: pointer;
  font-weight: 550;
}

.image-style {
  width: 100%;
  height: 110px;
}

.image-box {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}

.single-image-box {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
  width: 48%;
}

.image-bottom {
  display: flex;
  align-items: center;
  width: 94%;
  height: 43px;
  padding: 4px;
  border: 1px solid rgb(235, 235, 235);
  border-top: none;
}

.pointer {
  margin-left: 2px;
}

.expand {
  padding: 8px;
  height: 45px;
}

.expand span {
  font-size: 14px;
  font-weight: 600;
  color: #027A97;
  cursor: pointer;
}

.show-box {
  display: flex;
  flex-direction: row;
  padding-bottom: 10px;
  padding-top: 10px;
  border: 1px solid rgba(235, 235, 235, 1);
  border-top: none;
}

.img {
  width: 25%;
  height: 90px;
  cursor: pointer;
}

.img img {
  width: 100%;
  height: 100%;
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: start;
  width: 75%;
  height: 100%;
  margin-left: 3%;
}

.info1 {
  color: #2D2E2F;
  font-size: 20px;
}

.info2 {
  display: flex;
}

.info2 span {
  color: #6E7072;
}

.rate-box {
  display: flex;
  align-items: center;
  margin-left: 3%;
  width: 100%;
}

.info3 span {
  color: #6E7072;
  font-size: 14px;
  padding: 0.5%;
  background-color: #edeff1;
  margin-left: 1.1%;
  margin-right: 5%;
}
</style>