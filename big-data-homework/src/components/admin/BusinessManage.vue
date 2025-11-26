<template>
  <div class="container">
    <div class="middle" v-show="display">
      <p v-show="data.length === 0" class="tip">There is no applys now!</p>
      <div v-show="data.length > 0">
        <div class="info" v-for="(apply, index) in data" :key="index">
          <img
            style="width: 300px; height: 200px"
            src="https://ts1.cn.mm.bing.net/th/id/R-C.b2a766602b6e286f3134318a940bf359?rik=zk5SqYpxRjaE9g&riu=http%3a%2f%2fwww.sucaijishi.com%2fuploadfile%2f2020%2f0110%2f20200110051243204.jpg%3fimageMogr2%2fformat%2fjpg%2fblur%2f1x0%2fquality%2f60&ehk=9FdfGwR4pb2c3PCltwA%2fCsi0hUFAtlMPdrTn3RIstyQ%3d&risl=&pid=ImgRaw&r=0"
            alt="please wait"
          />
          <div class="text-group">
            <div class="text-line">
              <div class="b-name">NAME：{{ apply.name }}</div>
            </div>
            <div class="text-line">
              <div class="category">CATEGORIES：{{ apply.categories }}</div>
            </div>
            <div class="text-line">
              <div class="hours">HOURS：{{ apply.hours }}</div>
            </div>
            <div class="text-line">
              <div class="kid">GOODFORKIDS：{{ apply.goodForKids }}</div>
              <div class="creditcard">
                CREDITCARDS：{{ apply.businessAcceptsCreditcards }}
              </div>
            </div>
            <div class="text-line">
              <div class="address">ADDRESS:{{ apply.address }}</div>
              <el-icon
                STYLE="margin-right: 40px;font-size: 22px"
                @click="isAgree(apply.businessId, 1)"
                ><Check
              /></el-icon>
            </div>
            <div class="text-line">
              <div class="city-state">
                <div class="city">CITY：{{ apply.city }}</div>
                <div class="state">STATE：{{ apply.state }}</div>
              </div>
              <el-icon
                STYLE="margin-right: 40px;;font-size: 22px;"
                @click="isAgree(apply.businessId, 0)"
                ><Close
              /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom">
      <p class="block" @click="applyInfo">申请通知</p>
      <p class="apply-info" @click="noDisplay">解封/封禁商家</p>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios from "axios";

let display = ref(false);
let data = ref([]);
const noDisplay = () => {
  display.value = false;
};

const applyInfo = () => {
  display.value = true;
  axios
    .get("/api/businessManage/getApply")
    .then((res) => {
      console.log(res.data.data);
      data.value = res.data.data;
    })
    .catch((error) => {
      console.log(error);
    });
};
onMounted(applyInfo);

const businessResultDTO = ref({
  businessId: 0,
  result: -1,
});
const isAgree = (bId, rlt) => {
  businessResultDTO.value.businessId = bId;
  businessResultDTO.value.result = rlt;
  axios
    .put("/api/businessManage/isAgree", businessResultDTO.value)
    .then((response) => {
      // 处理响应
      console.log(response);
      applyInfo();
    })
    .catch((error) => {
      // 处理错误
      console.log(error);
    });
};
</script>
<style scoped>
.container {
  display: flex;
  margin-left: 300px;
  background-color: #ffffff;
}
.middle {
  margin-left: -8%;
  display: flex;
  overflow-y: auto;
}
.tip {
  display: flex;
  text-align: center;
  margin-left: 40vh;
  margin-top: 20vh;
  font-size: 20px;
  color: #5b5b5b;
}

.info {
  display: flex;
  flex-direction: row;
  align-items: flex-start; /* 图片和文字垂直对齐到顶部 */
  margin-bottom: 1px;
  border-bottom: #d1dfe2 2px solid;

  width: 100%;

  /* 移除高度设置，让内容自然流动 */
}

.info img {
  width: 300px;
  height: 200px;
  margin-right: 20px; /* 图片和文字之间的间距 */
}

.text-group {
  flex: 1; /* 文本内容占据剩余空间 */
  display: flex;
  flex-direction: column; /* 文本内容垂直排列 */
  color: #282d2c;
}

.text-line {
  padding-top: 4px;
  display: flex; /* 每行内的元素水平排列 */
  justify-content: space-between; /* 文本在行内平均分布（或根据需求调整） */
  align-items: flex-start; /* 文本在行内垂直居中对齐 */
  margin-bottom: 10px; /* 行与行之间的间距 */
  font-size: 13px;
}

/* 如果需要，可以为特定的文本元素添加样式，例如： */
.b-name {
  font-size: 20px;
}
.category {
  display: flex;
  /* 样式 */
}

.hours {
  /* 样式，比如可以设置宽度为100%以确保单独占一行 */
  flex: 1 0 100%; /* 占据整行 */
}

.kid,
.creditcard {
  /* 样式，可以根据需要调整 */
  flex: 1; /* 默认平分剩余空间，但会根据内容大小自动调整 */
}
.creditcard {
  margin-right: 400px;
}

.address,
.city,
.state {
  /* 样式 */
}

.city-state {
  display: flex; /* 城市和州水平排列 */
  justify-content: space-between; /* 城市和州之间的间距 */
}
.city {
  padding-right: 20px;
}
.bottom {
  border-top: #c6c2c2 2px solid;
  position: absolute;
  display: flex;
  bottom: 0px;
  width: 70%;
  margin-left: -8%;
}
.block {
  margin-right: 115vh;

  margin-bottom: 30px;
  font-size: 18px;
}
.apply-info {
  margin-bottom: 30px;
  font-size: 18px;
}
</style>
