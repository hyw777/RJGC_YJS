<template>
  <div class="container">
    <div class="middle" v-show="display">
      <p v-show="data.length === 0" class="tip">当前没有申请信息！</p>
      <div v-show="data.length > 0" class="business-list">
        <div class="info-card" v-for="(apply, index) in data" :key="index">
          <div class="card-header">
            <img
              class="business-image"
              src="https://ts1.cn.mm.bing.net/th/id/R-C.b2a766602b6e286f3134318a940bf359?rik=zk5SqYpxRjaE9g&riu=http%3a%2f%2fwww.sucaijishi.com%2fuploadfile%2f2020%2f0110%2f20200110051243204.jpg%3fimageMogr2%2fformat%2fjpg%2fblur%2f1x0%2fquality%2f60&ehk=9FdfGwR4pb2c3PCltwA%2fCsi0hUFAtlMPdrTn3RIstyQ%3d&risl=&pid=ImgRaw&r=0"
              alt="商家图片"
            />
            <div class="business-name">{{ apply.name }}</div>
          </div>
          
          <div class="card-body">
            <div class="info-row">
              <div class="info-item">
                <span class="label">类别：</span>
                <span class="value">{{ apply.categories }}</span>
              </div>
              <div class="info-item">
                <span class="label">营业时间：</span>
                <span class="value">{{ apply.hours }}</span>
              </div>
            </div>
            
            <div class="info-row">
              <div class="info-item">
                <span class="label">适合儿童：</span>
                <span class="value">{{ apply.goodForKids ? '是' : '否' }}</span>
              </div>
              <div class="info-item">
                <span class="label">接受信用卡：</span>
                <span class="value">{{ apply.businessAcceptsCreditcards ? '是' : '否' }}</span>
              </div>
            </div>
            
            <div class="info-row">
              <div class="info-item full-width">
                <span class="label">地址：</span>
                <span class="value">{{ apply.address }}</span>
              </div>
            </div>
            
            <div class="info-row">
              <div class="info-item">
                <span class="label">城市：</span>
                <span class="value">{{ apply.city }}</span>
              </div>
              <div class="info-item">
                <span class="label">州：</span>
                <span class="value">{{ apply.state }}</span>
              </div>
            </div>
          </div>
          
          <div class="card-footer">
            <el-button 
              type="success" 
              :icon="Check" 
              circle 
              @click="isAgree(apply.businessId, 1)"
              title="同意申请"
            />
            <el-button 
              type="danger" 
              :icon="Close" 
              circle 
              @click="isAgree(apply.businessId, 0)"
              title="拒绝申请"
            />
          </div>
        </div>
      </div>
    </div>
    
    <div class="bottom">
      <el-button 
        type="primary" 
        @click="applyInfo"
        :plain="!display"
      >
        申请通知
      </el-button>
      <el-button 
        type="warning" 
        @click="noDisplay"
        :plain="display"
      >
        解封/封禁商家
      </el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import { Check, Close } from '@element-plus/icons-vue';

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
      console.log(response);
      applyInfo();
    })
    .catch((error) => {
      console.log(error);
    });
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  margin-left: 300px;
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 20px;
}

.middle {
  flex: 1;
  overflow-y: auto;
  padding: 20px 0;
}

.tip {
  text-align: center;
  margin: 20vh auto;
  font-size: 20px;
  color: #909399;
}

.business-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.info-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  overflow: hidden;
}

.info-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.card-header {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.business-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.business-name {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  color: white;
  padding: 20px 15px 10px;
  font-size: 18px;
  font-weight: bold;
}

.card-body {
  padding: 15px;
}

.info-row {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-item {
  flex: 1;
  min-width: 50%;
  margin-bottom: 8px;
}

.info-item.full-width {
  min-width: 100%;
}

.label {
  font-weight: 500;
  color: #606266;
  font-size: 14px;
}

.value {
  color: #303133;
  font-size: 14px;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  padding: 0 15px 15px;
  gap: 10px;
}

.bottom {
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: center;
  padding: 20px 0;
  background: white;
  margin: 0 -20px;
}

.el-button {
  margin: 0 10px;
}
</style>