<template>
  <div class="container">
    <NewIndexView :show-background-description="false" style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <div class="display-box">
      <img class="img" v-for="(image,index) in imageList" :src="image" :key="index">
    </div>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import {UseImageListStore} from "@/stores/UseImageListStore";
import {computed, onMounted} from "vue";

let imageListStore = UseImageListStore()
let imageList = computed(()=>imageListStore.imageList)
</script>

<style scoped>

.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.display-box {
  display: flex;
  justify-content: space-between;
  margin: 0px 30px;
  flex-wrap: wrap;
  flex: 1;
  overflow: auto;
}

.img {
  width: 300px;
  height: 300px;
  margin-bottom: 40px;
}
</style>