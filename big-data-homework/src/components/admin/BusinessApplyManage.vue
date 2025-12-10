<template>
  <div class="container">
    <div class="middle" v-show="display">
      <p v-show="data.length === 0" class="tip">当前没有申请信息！</p>
      <div v-show="data.length > 0" class="business-list">
        <div class="info-card" v-for="(apply, index) in data" :key="index">
          <div class="card-header">
            <div class="business-name">{{ apply.name }}</div>
            <div class="apply-time">申请时间: {{ formatDate(apply.applyTime) }}</div>
          </div>
          
          <div class="card-body">
            <div class="info-section">
              <h3>基本信息</h3>
              <div class="info-grid">
                <div class="info-item">
                  <span class="label">类别：</span>
                  <span class="value">{{ apply.categories }}</span>
                </div>
                <div class="info-item">
                  <span class="label">城市：</span>
                  <span class="value">{{ apply.city }}</span>
                </div>
                <div class="info-item">
                  <span class="label">州/省：</span>
                  <span class="value">{{ apply.state }}</span>
                </div>
                <div class="info-item">
                  <span class="label">邮政编码：</span>
                  <span class="value">{{ apply.postalCode }}</span>
                </div>
              </div>
            </div>
            
            <div class="info-section">
              <h3>营业信息</h3>
              <div class="info-grid">
                <div class="info-item">
                  <span class="label">营业时间：</span>
                  <span class="value">{{ formatHours(apply.hours) }}</span>
                </div>
                <div class="info-item">
                  <span class="label">适合儿童：</span>
                  <span class="value">{{ getAttributeValue(apply.attributes, 'GoodForKids') === 'True' ? '是' : '否' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">接受信用卡：</span>
                  <span class="value">{{ getAttributeValue(apply.attributes, 'BusinessAcceptsCreditCards') === 'True' ? '是' : '否' }}</span>
                </div>
                <div class="info-item">
                  <span class="label">提供WiFi：</span>
                  <span class="value">{{ getAttributeValue(apply.attributes, 'WiFi') ? '是' : '否' }}</span>
                </div>
              </div>
            </div>
            
            <div class="info-section">
              <h3>详细地址</h3>
              <div class="info-item full-width">
                <span class="value address">{{ apply.address }}</span>
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

<script setup>
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
      ElMessage.success("操作成功！");
      console.log(response);
      applyInfo();
    })
    .catch((error) => {
      console.log(error);
    });
};

// 格式化日期显示
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('zh-CN');
};

// 格式化营业时间显示
const formatHours = (hoursString) => {
  try {
    const hours = JSON.parse(hoursString);
    // 只显示周一到周五的时间（如果都一样的话）
    const mondayTime = hours.Monday || '';
    const isSameEveryday = Object.values(hours).every(time => time === mondayTime);
    
    if (isSameEveryday) {
      return `每日 ${mondayTime}`;
    } else {
      return `工作日 ${mondayTime} | 周末 ${hours.Saturday || ''}`;
    }
  } catch (e) {
    return hoursString;
  }
};

// 获取属性值
const getAttributeValue = (attributesString, attributeName) => {
  try {
    const attributes = JSON.parse(attributesString);
    return attributes[attributeName] || '';
  } catch (e) {
    return '';
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
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
  grid-template-columns: repeat(auto-fill, minmax(450px, 1fr));
  gap: 20px;
}

.info-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  border: 1px solid #ebeef5;
}

.info-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  transform: none;
}

.card-header {
  padding: 15px 20px 10px;
  border-bottom: 1px solid #ebeef5;
}

.business-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.apply-time {
  color: #909399;
  font-size: 12px;
}

.card-body {
  padding: 15px 20px;
  flex: 1;
}

.info-section {
  margin-bottom: 15px;
}

.info-section:last-child {
  margin-bottom: 0;
}

.info-section h3 {
  font-size: 14px;
  color: #409eff;
  margin: 0 0 10px 0;
  padding-bottom: 5px;
  border-bottom: 1px solid #ebeef5;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item.full-width {
  grid-column: span 2;
}

.label {
  font-weight: 500;
  color: #606266;
  font-size: 12px;
  margin-bottom: 2px;
}

.value {
  color: #303133;
  font-size: 13px;
}

.value.address {
  background-color: #f5f7fa;
  padding: 8px 10px;
  border-radius: 4px;
  line-height: 1.4;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px 15px;
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