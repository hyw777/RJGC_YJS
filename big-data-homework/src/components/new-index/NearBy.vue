<template>
  <div class="show-box" v-for="(business,index) in businesses" :key="index">
    <router-link class="img" :to="{path:'/merchantDetail',query:{id:business.businessId}}">
      <img :src="filePath(business.image)" >
    </router-link>
    <div class="info-box">
      <div class="info1">
        <span>{{business.name}}</span>
      </div>
      <div class="info2">
        <el-rate v-model="business.stars" size="large" disabled></el-rate>
        <div class="rate-box">
          <div style="margin-right: 1%">{{business.stars}}</div>
          <span>({{business.reviewCount}} reviews)</span>
        </div>
      </div>
      <div class="info3">
        <span>{{business.categories}}</span>
      </div>
      <div class="actions">
      <el-button type="text" class="action-btn" @click.prevent="markUseful(business)">有用 <span class="count">{{ business.__interactions?.useful ?? 0 }}</span></el-button>
      <el-button type="text" class="action-btn" @click.prevent="markThanks(business)">感谢 <span class="count">{{ business.__interactions?.thanks ?? 0 }}</span></el-button>
      <el-button type="text" class="action-btn" @click.prevent="markLike(business)">喜欢 <span class="count">{{ business.__interactions?.like ?? 0 }}</span></el-button>
      <el-button type="text" class="action-btn" @click.prevent="markOhNo(business)">哦不 <span class="count">{{ business.__interactions?.ohno ?? 0 }}</span></el-button>
    </div>
    </div>
  
   

  </div>
</template>

<script setup lang="ts">

import {onMounted, toRefs} from "vue";
import {useActivity} from "@/hooks/UseActivity";
import {useAuthStore} from "@/stores/UseAuthStore";
import {UseButtonStore} from "@/stores/UseButtonStore";

let authStore = useAuthStore()
let buttonStore = UseButtonStore()
let {businesses,getNearByBusinesses} = toRefs(useActivity())

const filePath  = (file: any) => {
  if (file == null) {
    return ``
  }
  return file.includes('http') ? file : `/api/images/${file}`;
}



onMounted(()=>{
  buttonStore.setIndexButton(0)
  if(authStore.token){
    getNearByBusinesses.value()
  }
})

function ensureInteractions(business: any) {
  if (!business.__interactions) {
    business.__interactions = { useful: 0, thanks: 0, like: 0, ohno: 0 }
  }
}

function markUseful(business: any) {
  ensureInteractions(business)
  business.__interactions.useful++
}

function markThanks(business: any) {
  ensureInteractions(business)
  business.__interactions.thanks++
}

function markLike(business: any) {
  ensureInteractions(business)
  business.__interactions.like++
}

function markOhNo(business: any) {
  ensureInteractions(business)
  business.__interactions.ohno++
}
</script>

<style scoped>
.show-box {
  display: flex;
  flex-direction: row;
  width: 45%;
  height: 20%;
  padding-bottom: 3%;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  margin-bottom: 4%;
  margin-right: 5%;
}

.img {
  width: 25%;
  height: 140px;
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
  margin-left: 4%;
}

.info1 {
  color: #2D2E2F;
  font-size: 25px;
  margin-bottom: 2.3%;
}

.info2 {
  display: flex;
  margin-bottom: 2%;
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
  font-size: 16px;
  padding: 0.5%;
  background-color: #edeff1;
  margin-left: 1.1%;
  margin-right: 5%;
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.action-btn {
  padding: 0;
  color: #6E7072;
  font-size: 14px;
}

.action-btn .count {
  margin-left: 6px;
  color: #2D2E2F;
  font-weight: 600;
}

</style>