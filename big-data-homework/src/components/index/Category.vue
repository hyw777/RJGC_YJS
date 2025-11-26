<template>
  <div class="category">
    <div class="category-left">
      全部分类:
    </div>
    <div class="category-right">
      <div class="element" @click="active(1)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 1}">分类1</el-button></div>
      <div class="element" @click="active(2)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 2}">分类2</el-button></div>
      <div class="element" @click="active(3)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 3}">分类3</el-button></div>
      <div class="element" @click="active(4)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 4}">分类4</el-button></div>
      <div class="element" @click="active(5)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 5}">分类5</el-button></div>
      <div class="element" @click="active(6)"><el-button type="primary" color="#E3E5E7" :class="{active:activeId == 6}">分类6</el-button></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

let activeId = ref(1)
function active(id:number) {
  activeId.value = id
}
</script>

<style scoped>
.category {
  display: flex;
  background-color: #ff6b37;
  height: 60px;
  align-items: center;
  justify-content: start;
  margin: 0px 17.7vw;
  border-radius: 7px;
}

.category-right {
  display: flex;
  width: 87%;
  margin-left: auto;
}

.category-left {
  color: black;
  font-size: 20px;
  margin-left: 3%;
}

.element {
  margin-right: 5%;
}

.element el-button{
  cursor: pointer;
}

.active {
  color: #ff6b37;
  background-color: #ebedf0;
}

</style>