<template>
  <div class="categories">
    <div class="category-box">
      <div class="title"><h2>categories</h2></div>
      <div class="category-card-box">
        <div class="category-card" @click="reload('restaurant')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><KnifeFork /></el-icon>
            <div class="word-box">Restaurants</div>
          </div>
        </div>
        <div class="category-card" @click="reload('shop')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><ShoppingCart /></el-icon>
            <div class="word-box">Shopping</div>
          </div>
        </div>
        <div class="category-card" @click="reload('nightlife')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><Moon /></el-icon>
            <div class="word-box">Nightlife</div>
          </div>
        </div>
        <div class="category-card" @click="reload('active')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><Aim /></el-icon>
            <div class="word-box">Active Life</div>
          </div>
        </div>
        <div class="category-card" @click="reload('beauty')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><Scissor /></el-icon>
            <div class="word-box">Beauty & Spas</div>
          </div>
        </div>
        <div class="category-card" @click="reload('automotive')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><Van /></el-icon>
            <div class="word-box">Automotive</div>
          </div>
        </div>
        <div class="category-card" @click="reload('home')">
          <div class="content-box">
            <el-icon size="40px" color="#f43939"><House /></el-icon>
            <div class="word-box">Home Services</div>
          </div>
        </div>
        <div class="category-card" @click="reload('more')">
          <div class="content-box">
            <el-icon size="40px"><MoreFilled /></el-icon>
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
  border: 2px solid rgba(235, 235, 235, 1);
  cursor: pointer;
}

.content-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 88px;
}

.word-box {
  color: #2D2E2F;
  font-size: 16px;
}
</style>