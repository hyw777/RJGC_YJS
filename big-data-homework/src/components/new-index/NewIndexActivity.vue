<template>
  <div class="container" style="float: left">
    <div class="big-box">
      <div class="top-box">
        <h2 class="top-title">Recent Activity</h2>
        <div class="top-nav">
          <router-link to="/nearby"
            class="nearby nav-button"
            :class="{ 'active-button': buttonStore.indexButton == 0 }"
          >
            <div class="word-style">Nearby</div>
          </router-link>
          <router-link to="/friend"
            class="friends nav-button"
            :class="{ 'active-button': buttonStore.indexButton == 1 }"
          >
            <div class="word-style">Friends</div>
          </router-link>
          <router-link to="news"
            class="following nav-button"
            :class="{ 'active-button': buttonStore.indexButton == 2 }"
          >
            <div class="word-style">News</div>
          </router-link>
        </div>
      </div>
      <div class="content-box">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, toRefs} from "vue";
import axios from "axios";
import {useAuthStore} from "@/stores/UseAuthStore";
import {UseButtonStore} from "@/stores/UseButtonStore";

let buttonStore = UseButtonStore()

let contents = ref([
  {
    avatar: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/4.jpg",
    username: "Cxk",
    time: "1 hour ago",
    title: "麻豆传媒",
    rate: "5",
    images: [
      "https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg",
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar:
      "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/070e9ea0-0a81-41ad-a5ec-a586b7790610.jpg",
    username: "Cxk",
    time: "1 hour ago",
    title: "麻豆传媒",
    rate: "2",
    images: [
      "https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg",
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg',
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar:
      "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/8d35ede9-a663-4de8-b39c-3382e7a3d033.jpg",
    username: "Cxk",
    time: "1 hour ago",
    title: "麻豆传媒",
    rate: "1",
    images: [
      "https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg",
      // 'https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg'
    ],
  },
  {
    avatar:
      "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/7db0cdc3-7b56-4dac-ade3-7fc073cb58d0.jpg",
    username: "Cxk",
    time: "1 hour ago",
    title: "麻豆传媒",
    rate: "4",
    images: [
      "https://s3-media0.fl.yelpcdn.com/bphoto/yvDAklbjr-WkDa9aj7saAA/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/Gigpu4BxcWL2-Ztdt7X5Ag/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/bT_lJyq84ZxvwVfHKXkZ4g/l.jpg",
      "https://s3-media0.fl.yelpcdn.com/bphoto/UnjYuRdVRdC2_0R2H74zLA/l.jpg",
    ],
  },
]);

// 图片是否全部展示
const showAllImages = ref(contents.value.map(() => false));

// 该展示的图片
const displayImages = computed(() =>
  contents.value.map((content, index) =>
    showAllImages.value[index] ? content.images : content.images.slice(0, 2)
  )
);

const colors = ref(["#99A9BF", "#F7BA2A", "#FF9900"]); // same as { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }

function changeActive(index: number) {
  isActive.value = index;
}

function expand(index: number) {
  showAllImages.value[index] = true;
  console.log(showAllImages.value[index]);
}

function close(index: number) {
  showAllImages.value[index] = false;
}

// 排行表
const businessRank = ref([
  // {
  //   "name":"djssdmewk.fnkjhfaewgawgweagwaegeawfsdv",
  //   "address":"chinadhsjgdjsghdhyjey"
  // }
]);

const getPVRankList = () => {
  axios.get("/api/rank/getRankList").then((resp) => {
    businessRank.value = resp.data;
    console.log(resp.data);
  });
};

onMounted(()=>{
  // getPVRankList()
});
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.big-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 66%;
  padding-bottom: 56px;
}

.top-box {
  display: flex;
  flex-direction: column;
  /* justify-content: space-between; */
  align-items: center;
  margin-bottom: 48px;
}

.top-title {
  margin: 0px;
  padding-bottom: 16px;
  font-size: 28px;
  color: #2d2e2f;
}

.top-nav {
  display: flex;
  justify-content: center;
}

.nav-button {
  border-bottom: 4px solid #0000;
  cursor: pointer;
  color: #6e7072;
}

.nav-button:hover {
  color: #2d2e2f;
}

.active-button {
  border-color: #e00707;
  color: #2d2e2f;
}

.word-style {
  padding: 0px 16px 16px 16px;
  font-size: 16px;
}

.content-box {
  display: flex;
  align-content: start;
  flex-wrap: wrap;
  width: 100%;
}

.content-card {
  display: flex;
  flex-direction: column;
  margin-right: 20px;
  width: 31%;
  margin-bottom: 30px;
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
  color: #2d2e2f;
  font-weight: 600;
  line-height: 20px;
}

.time {
  font-size: 12px;
  color: #6e7072;
  font-weight: 400;
  line-height: 16px;
}

.show {
  display: flex;
  flex-direction: column;
  padding: 16px 16px 0px 16px;
  border: 1px solid rgb(235, 235, 235);
  border-top: none;
}

.title {
  color: #2d2e2f;
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
  color: #027a97;
  cursor: pointer;
}
</style>
