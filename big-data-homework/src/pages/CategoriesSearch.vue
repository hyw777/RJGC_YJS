<template>
  <div class="search">
    <NewIndexView
        style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <div class="display">
      <div class="title">
        相关商家
      </div>
      <div class="mid">
        <div class="show-box" v-for="(record,index) in result.records" :key="index">
          <router-link class="img" :to="{path:'/merchantDetail',query:{id:record.businessId}}">
            <img :src="filePath(record.image)" >
          </router-link>
          <div class="info-box">
            <div class="info1">
              <span>{{record.name}}</span>
            </div>
            <div class="info2">
              <el-rate v-model="record.stars" size="large" disabled></el-rate>
              <div class="rate-box">
                <div style="margin-right: 1%">{{record.stars}}</div>
                <span>({{record.reviewCount}} reviews)</span>
              </div>
            </div>
            <div class="info3">
              <span>{{record.categories}}</span>
            </div>
          </div>
        </div>
      </div>
      <el-pagination
          @current-change="getResult"
          :current-page="search.page"
          :page-size="search.pageSize"
          :hide-on-single-page="search.page"
          :total="result.total"
          class="page" background layout="prev,pager, next"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import {useSearch} from "@/hooks/UseSearch";
import {useRoute} from "vue-router";
import {computed, onMounted, ref, toRefs, watch} from "vue";
import {UseSearchStore} from "@/stores/UseSearchStore";

const filePath  =(file) => {
  if(file == null){
    return ``
  }
  return  file.includes('http') ? file : `/api/images/${file}`;
}

let searchStore = UseSearchStore();
let route = useRoute()
let {search,getResult,getCategoriesResult} = toRefs(useSearch())

function  handleCurrentChange(val){
  getCategoriesResult.value(val,route.query.category)
}

let result = computed(()=>searchStore.result)

onMounted(() => {
  getCategoriesResult.value(1,route.query.category)
  console.log(searchStore.result)
})


</script>

<style scoped>
.search {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
}

.display {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  margin: 1% 15% 0px;
}

.title {
  margin-bottom: 2%;
  align-self: start;
  font-size: 25px;
  font-weight: bold;
}

.mid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-content: start;
  height: 80%;
}

.show-box {
  display: flex;
  flex-direction: row;
  width: 45%;
  height: 20%;
  padding-bottom: 1.8%;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  margin-bottom: 4%;
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
  font-size: 30px;
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

:deep(.info2 svg) {

}

.page {
  margin-left: 40%;
}
</style>