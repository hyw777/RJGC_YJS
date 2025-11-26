<template>
  <div class="container">
    <div class="search-box">
     <div class="date-selector"> <el-date-picker
          v-model="value2"
          type="daterange"
          size="large"
          value-format="YYYY-MM-DD"
          unlink-panels
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
          :shortcuts="shortcuts"
          :size="size"
      />
       <el-icon style="font-size: 25px;margin-left: 20px; margin-top: 10px" @click="search" ><Pointer /></el-icon>
     </div>
    </div>
    <div class="review-list">
     <div class="each-data"  v-for="(review,index) in data.records" :key="index" v-show="display">
       <el-icon style="margin-left: 10px;font-size: 20px;color: #58B4FF"><User /></el-icon>:
       <div class="user-name">
         {{review.userName}}
       </div>
         <el-icon style="color: #e19898;font-size: 20px;margin-left: 100px"><Shop /></el-icon>
       <div class="bussiness-name">
         :{{review.businessName}}
       </div>
       <el-rate v-model="review.stars" disabled style="margin-left: 150px; margin-top: -8px;"/>
       <div class="time" >
         {{formatDate(review.date)}}
       </div>

       <div class="warning" v-if="!isReviewHidden(review.reviewId)"   @click="warning(review.reviewId,review)">
         <el-icon  ><WarningFilled /></el-icon>
      </div>
       <div class="text-line">
       </div>
      <div class="text">
        {{review.text}}
      </div>
     </div>
    </div>
    <div class="pageBottom">
      <el-pagination background v-model:current-page="findReviewDTO.pageNum" layout="prev, pager, next" :page-count=" Math.ceil(data.total/findReviewDTO.pageSize)"    @current-change="search"  style="margin-left: 533px"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted,ref } from 'vue';
import axios from "axios";
const size = ref<'default' | 'large' | 'small'>('default')
const display = ref([true,true,true,true,true]);
const value2 = ref('')
const findReviewDTO=ref({
  pageSize:5,
  pageNum:1,
  begin:'',
  end:''
})
let data=ref([]);


const search=()=>{
  findReviewDTO.value.begin=value2.value[0];
  findReviewDTO.value.end=value2.value[1];
  axios.post('/api/reviewManage/getAll',findReviewDTO.value)
      .then((response)=>{
        console.log(response.data.data);
        console.log(findReviewDTO.value.begin);
        console.log(findReviewDTO.value.end);
        data.value=response.data.data;
      })
      .catch((error)=>{
        console.log(error);
      })
};
onMounted(()=>{
  search();
  if (storedHiddenIds) {
    hiddenReviewIds.value = new Set(JSON.parse(storedHiddenIds));
  }
});


// 使用 ref 创建一个响应式集合来存储已隐藏的 reviewId
// 从 localStorage 获取已隐藏的 reviewId
const storedHiddenIds = localStorage.getItem('hiddenReviewIds');
const hiddenReviewIds = ref(storedHiddenIds ? new Set(JSON.parse(storedHiddenIds)) : new Set<number>());


// 检查 reviewId 是否在 hiddenReviewIds 中
function isReviewHidden(reviewId: number) {
  return hiddenReviewIds.value.has(reviewId);
}

// 隐藏警告并将 reviewId 添加到 hiddenReviewIds 中，并且发出警告
function warning(reviewId: number,reviewVO) {
  axios.post('/api/reviewManage/warning',reviewVO).
      then((response)=>{
        console.log(response.data.data);
    hiddenReviewIds.value.add(reviewId);
    // 更新 localStorage
    localStorage.setItem('hiddenReviewIds', JSON.stringify(Array.from(hiddenReviewIds.value)));
  }).catch((error)=>{
    console.log(error);
  })

}


const isoString=ref('');
// 格式化日期的函数
function formatDate(dateString) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`; // 格式化到秒
}


const shortcuts = [
  {
    text: 'Last week',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: 'Last month',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: 'Last 3 months',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]
</script>

<style scoped>
.container{
  margin-left: 5%;
  flex-direction: column;
  position: relative;
}
.search-box{
 margin-left: 264px;
  margin-top: -10px;
}
.date-selector{

  display: flex;
  width: 80vh;
  height: 40px;
}

.review-list{
 width: 100%;
  height: 68vh;
  margin-top: 30px;
}
.each-data{
  width: 98%;
  height: 80px;
 border-bottom: 2px solid rgba(198, 193, 193, 0.18);
  border-top: 2px solid rgba(198, 193, 193, 0.18);
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap; /* 如果需要的话，可以启用换行 */
}
.user-name{
  margin-left: 5px;
  font-size: 14px;
}
.bussiness-name{
  height: 20px;
  overflow: hidden;
  font-size: 14px;
  width: 100px;
}
.time{
  margin-left: 66px;
  font-size: 12px;
}
.warning{
  font-size: 20px;
  color: #e24e4e;
  margin-left: 20%;

}
.text-line{
  width: 100%;
  height:1px;
}
.text{
  overflow: auto ;
  width: 100%;
  height: 50px;
  font-size: 14px;
}

.pageBottom{
  width: 100%;
  height: 6%;

  position: absolute;
  bottom: -65px;
  right:13%;
}



</style>