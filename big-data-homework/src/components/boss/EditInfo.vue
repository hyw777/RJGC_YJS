<template>
  <div class="edit-info">
    <div class="form-card">
      <h2 class="card-title">商家信息编辑</h2>
      <el-form :model="info" label-width="120px" size="large" class="edit-form">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="商家名" prop="name">
              <el-input v-model="info.name" placeholder="请输入商家名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="地址" prop="address">
              <el-input v-model="info.address" placeholder="请输入详细地址" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="info.city" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="州" prop="state">
              <el-input v-model="info.state" placeholder="请输入州/省" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="分类" prop="categories">
              <el-input v-model="info.categories" placeholder="请输入商家分类，多个分类用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="营业时间" prop="hours">
              <el-input v-model="info.hours" placeholder="请输入营业时间，例如: 09:00-22:00" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="儿童适宜" prop="goodForKids">
              <el-radio-group v-model="info.goodForKids">
                <el-radio value="yes">是</el-radio>
                <el-radio value="no">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接受信用卡" prop="businessAcceptsCreditcards">
              <el-radio-group v-model="info.businessAcceptsCreditcards">
                <el-radio value="true">是</el-radio>
                <el-radio value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <div class="form-actions">
          <el-button 
            @click="editInfo" 
            type="primary" 
            size="large" 
            class="submit-btn"
          >
            提交
          </el-button>
          <el-button 
            @click="writeBcak" 
            size="large" 
            class="reset-btn"
          >
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, toRefs} from "vue";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {useEditMerchant} from "@/hooks/UseEditMerchant";

let buttonStore = UseButtonStore();

let {info, editInfo, writeBcak} = toRefs(useEditMerchant())

onMounted(() => {
  buttonStore.setBossButton(1)
  writeBcak.value()
})
</script>

<style scoped>
.edit-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100%;
}

.form-card {
  width: 100%;
  max-width: 800px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: box-shadow 0.3s ease;
}

.form-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 32px 0;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
  text-align: center;
}

.edit-form {
  width: 100%;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 40px;
}

.submit-btn {
  background-color: #6c757d !important;
  border-color: #6c757d !important;
  padding: 12px 32px;
  font-size: 16px;
}

.reset-btn {
  padding: 12px 32px;
  font-size: 16px;
  color: #6c757d;
  border-color: #6c757d;
}

.reset-btn:hover {
  background-color: #f8f9fa;
  color: #495057;
  border-color: #495057;
}

:deep(.el-form-item__label) {
  color: #495057;
  font-weight: 500;
}

:deep(.el-input__inner) {
  border-radius: 6px;
  border-color: #dee2e6;
  transition: border-color 0.3s ease;
}

:deep(.el-input__inner:hover) {
  border-color: #adb5bd;
}

:deep(.el-input__inner:focus) {
  border-color: #6c757d;
  box-shadow: 0 0 0 3px rgba(108, 117, 125, 0.1);
}

:deep(.el-radio__inner) {
  border-color: #dee2e6;
}

:deep(.el-radio__inner:hover) {
  border-color: #6c757d;
}

:deep(.el-radio__input.is-checked .el-radio__inner) {
  border-color: #6c757d;
  background-color: #6c757d;
}

:deep(.el-radio__label) {
  color: #495057;
}

</style>