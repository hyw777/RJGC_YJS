<template>
  <div style="width: 100%;display: flex;justify-content: center">
    <el-card class="elcard" style="margin: 56px 0px; width: 30%; height: 386px;">
      <div slot="header" class="clearfix" style="text-align: center">
        <span>商户浏览TOP10</span>
      </div>
      <el-table :data="businessRank" height="300" border>
        <el-table-column
            prop="name"
            label="商户"
            show-overflow-tooltip
            header-align="center"
        ></el-table-column>
        <el-table-column
            prop="address"
            label="商户地址"
            show-overflow-tooltip
            header-align="center"
        ></el-table-column>
      </el-table>

    </el-card>
  </div>
</template>

<script setup lang="ts">
// 排行表
import axios from "axios";
import {onMounted, ref} from "vue";

const businessRank = ref([]);

const businessIds = ref([]);

const getPVRankList = async() => {
  await axios.get("/ppi/rank/getRankList").then((resp) => {
    businessIds.value = resp.data;
    console.log(resp.data);
  });
  axios.post("/api/rank/getRankList",businessIds.value).then((resp)=>{
    businessRank.value=resp.data;
    console.log(resp.data)
  })
};



onMounted(()=>{
  getPVRankList()
})
</script>

<style scoped>

</style>