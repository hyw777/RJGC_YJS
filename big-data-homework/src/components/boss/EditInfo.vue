<template>
  <div class="edit-info">
    <div class="form">
      <div class="el-form">
        <el-form :model="info" label-width="auto" size="large">
          <el-form-item label="商家名" prop="name" class="form-item">
            <el-input v-model="info.name"/>
          </el-form-item>
          <el-form-item label="地址" prop="address" class="form-item">
            <el-input v-model="info.address"/>
          </el-form-item>
          <el-form-item label="城市" prop="city" class="form-item">
            <el-input v-model="info.city"/>
          </el-form-item>
          <el-form-item label="州" prop="state" class="form-item">
            <el-input v-model="info.state"/>
          </el-form-item>
          <el-form-item label="分类" prop="categories" class="form-item">
            <el-input v-model="info.categories"/>
          </el-form-item>
          <el-form-item label="营业时间" prop="hours" class="form-item">
            <el-input v-model="info.hours"/>
          </el-form-item>
          <el-form-item label="儿童适宜" prop="goodForKids" class="form-item">
            <el-radio-group v-model="info.goodForKids" class="ml-4">
              <el-radio value="yes" size="large">是</el-radio>
              <el-radio value="no" size="large">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="接受信用卡" prop="businessAcceptsCreditcards" class="form-item">
            <el-radio-group v-model="info.businessAcceptsCreditcards" class="ml-4">
              <el-radio value="true" size="large">是</el-radio>
              <el-radio value="false" size="large">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <div>
            <el-button @click="editInfo" type="primary" size="large"
                       style="width: 90px;margin-left: 198px;font-size: 15px" color="#E00707">提交
            </el-button>
            <el-button @click="writeBcak" type="danger" color="#6E7072" size="large"
                       style="margin-left: 198px;font-size: 15px;">
              <span style="margin-top: 5px;margin-right: 5px"><el-icon><RefreshLeft/></el-icon></span>
              <span>重置</span>
            </el-button>
          </div>
        </el-form>
      </div>
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
}

.form {
  display: flex;
  align-items: center;
  height: 90%;
  width: 70%;
  border: 2px solid rgba(235, 235, 235, 1);
  border-radius: 6px;
}

.el-form {
  width: 90%;
  margin-left: 2%;
}

.form-item {
  margin-bottom: 40px;
}

</style>