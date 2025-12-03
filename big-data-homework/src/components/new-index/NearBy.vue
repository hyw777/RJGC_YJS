<template>
  <div class="nearby-container">
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
        <el-button type="text" class="action-btn" @click.prevent="markUseful(business)">
          <el-icon><Check /></el-icon>
          有用 
          <span class="count">{{ business.__interactions?.useful ?? 0 }}</span>
        </el-button>
        <el-button type="text" class="action-btn" @click.prevent="markThanks(business)">
          <el-icon><Star /></el-icon>
          感谢 
          <span class="count">{{ business.__interactions?.thanks ?? 0 }}</span>
        </el-button>
        <el-button type="text" class="action-btn" @click.prevent="markLike(business)">
          <el-icon><Collection /></el-icon>
          喜欢 
          <span class="count">{{ business.__interactions?.like ?? 0 }}</span>
        </el-button>
        <el-button type="text" class="action-btn" @click.prevent="markOhNo(business)">
          <el-icon><CircleClose /></el-icon>
          哦不 
          <span class="count">{{ business.__interactions?.ohno ?? 0 }}</span>
        </el-button>
      </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, toRefs } from "vue";
import { useActivity } from "@/hooks/UseActivity";
import { useAuthStore } from "@/stores/UseAuthStore";
import { UseButtonStore } from "@/stores/UseButtonStore";
// 导入图标组件
import { Check, Star, Collection, CircleClose } from '@element-plus/icons-vue';

let authStore = useAuthStore()
let buttonStore = UseButtonStore()
let { businesses, getNearByBusinesses } = toRefs(useActivity())

const filePath = (file: any) => {
  if (file == null) {
    console.log('null');
    return ''
  }
  // 如果图片链接已经是完整的URL，直接使用；否则拼接静态服务器地址
  const fullPath = file.includes('http') ? file : `http://localhost:3000/images/${file}.jpg`;
  return fullPath;
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
.nearby-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 24px;
  /* make the visible area wider and centered */
  width: 100%;
  max-width: 2000px; /* increase max width */
  margin: 0 auto;
  padding: 0 20px; /* small side padding */
}

.show-box {
  display: flex;
  flex-direction: row;
  width: calc(50% - 12px); /* two per row with gap */
  padding: 18px;
  border: 1px solid #eee;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: white;
  box-sizing: border-box;
}

.show-box:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.img {
  /* fixed image column width, keep proportions via flex */
  flex: 0 0 140px; /* fixed column so image doesn't get squashed */
  min-width: 140px;
  width: 140px;
  height: 140px;
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.img:hover img {
  transform: scale(1.05);
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: start;
  flex: 1 1 auto;
  height: 100%;
  margin-left: 16px;
  min-width: 0; /* allow children to shrink for ellipsis */
}

.info1 {
  color: #2D2E2F;
  font-size: 25px;
  margin-bottom: 12px;
  font-weight: 600;
}

.info1 span {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}

.info2 {
  display: flex;
  margin-bottom: 12px;
  align-items: center;
}

.info2 span {
  color: #6E7072;
}

.rate-box {
  display: flex;
  align-items: center;
  margin-left: 12px;
  width: 100%;
  font-size: 14px;
}

.info3 {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 6px;
}

.info3 span {
  color: #6E7072;
  font-size: 14px;
  padding: 6px 10px;
  background-color: #f5f7fa;
  border-radius: 20px;
  /* allow the badge text to wrap within available space */
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.3;
  vertical-align: middle;
}

.actions {
  display: flex;
  gap: 16px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #eee;
}

.action-btn {
  padding: 4px 4px;
  color: #6E7072;
  font-size: 14px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.action-btn:hover {
  background-color: #f5f7fa;
  color: #333;
}

.action-btn .count {
  margin-left: 6px;
  color: #2D2E2F;
  font-weight: 600;
}

:deep(.el-rate__icon) {
  font-size: 18px;
}

:deep(.el-rate__decimal) {
  font-size: 18px;
}

@media (max-width: 900px) {
  .nearby-container { gap: 16px }
  .show-box { width: 100%; flex-direction: column }
  .img { width: 100%; height: 200px }
  .info-box { width: 100%; margin-left: 0; margin-top: 12px }
}
</style>