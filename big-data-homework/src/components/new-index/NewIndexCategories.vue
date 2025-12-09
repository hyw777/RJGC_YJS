<template>
  <div class="categories">
    <div class="category-box">
      <div class="title"><h2>categories</h2></div>
      <div class="category-card-box">
        <div class="category-card" @click="reload('restaurant')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><KnifeFork /></el-icon>
            <div class="word-box">Restaurants</div>
          </div>
        </div>
        <div class="category-card" @click="reload('shop')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><ShoppingCart /></el-icon>
            <div class="word-box">Shopping</div>
          </div>
        </div>
        <div class="category-card" @click="reload('nightlife')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><Moon /></el-icon>
            <div class="word-box">Nightlife</div>
          </div>
        </div>
        <div class="category-card" @click="reload('active')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><Aim /></el-icon>
            <div class="word-box">Active Life</div>
          </div>
        </div>
        <div class="category-card" @click="reload('beauty')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><Scissor /></el-icon>
            <div class="word-box">Beauty & Spas</div>
          </div>
        </div>
        <div class="category-card" @click="reload('automotive')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><Van /></el-icon>
            <div class="word-box">Automotive</div>
          </div>
        </div>
        <div class="category-card" @click="reload('home')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><House /></el-icon>
            <div class="word-box">Home Services</div>
          </div>
        </div>
        <div class="category-card" @click="reload('more')">
          <div class="content-box">
            <el-icon size="40px" color="#3b82f6"><MoreFilled /></el-icon>
            <div class="word-box">More</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {House, More, MoreFilled, Scissor, ShoppingCart} from "@element-plus/icons-vue";
import {useSearch} from "@/hooks/UseSearch";
import {router} from "@/router";
import {toRefs} from "vue";
let {search2,result,getResult,getCategoriesResult} = toRefs(useSearch())



function reload(category) {
  console.log(category)
  router.push({
    name: 'CategoriesSearch',
    query: {category:category}
  });
  getCategoriesResult.value(1,category)
}
</script>

<style scoped>
.categories {
  display: flex;
  justify-content: center;
  border-top: 1px solid rgba(235, 235, 235, 1);
}

.category-box {
  display: flex;
  flex-direction: column;
  width: 61.8%;
  height: 578px;
  margin: 48px 0px;
  padding: 8px 12px;
}

.title {
  display: flex;
  justify-content: center;
}

.category-card-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.category-card {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 22.3%;
  height: 183px;
  margin: 18px 0px;
  border: 1px solid rgba(210, 225, 245, 1);
  cursor: pointer;
  background: linear-gradient(180deg, #f0f9ff 0%, #fff 100%);
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(59, 130, 246, 0.06);
  transition: transform 180ms ease, box-shadow 180ms ease, border-color 180ms ease;
}

.content-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 88px;
}

.word-box {
  color: #1e40af;
  font-size: 16px;
  font-weight: 700;
  margin-top: 6px;
}

.category-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 14px 34px rgba(59, 130, 246, 0.12);
  border-color: rgba(59, 130, 246, 0.14);
}

.category-card:active { transform: translateY(-2px) }

.category-card el-icon, .category-card .el-icon {
  color: #3b82f6;
}

@media (max-width: 980px) {
  .category-box { width: 95%; }
  .category-card { width: 48%; margin: 10px 0; }
}
</style>