<template>
  <template v-if="!isEdit">
    <div class="title-box">
      <div class="title">Profile overview</div>
      <div class="edit" @click="editSwitch">
        <el-icon>
          <Edit/>
        </el-icon>
      </div>
    </div>
    <div class="container">
      <div class="profile">
        <h2>{{userInfo.name}}</h2>
        <p>{{ userInfo.age }}岁&nbsp;&nbsp;{{userInfo.gender == 'male' ? '男' : '女'}}</p>
        <p>{{userInfo.city}}</p>
      </div>
      <div class="profile-info">
        <div><span>邮箱 :</span>&nbsp;&nbsp;{{userInfo.email}}</div>
        <div><span>评论数 :</span>&nbsp;&nbsp;{{userInfo.reviewCount}} 条</div>
        <div><span>注册时间 :</span>&nbsp;&nbsp;{{userInfo.yelpingSince}}</div>
      </div>
    </div>
  </template>
  <template v-else>
    <div @click="editSwitch" class="back">
      <el-icon>
        <Back/>
      </el-icon>
    </div>
    <div class="el-box">
      <div class="el-form">
        <el-form :model="form" label-width="auto" size="large">
          <el-form-item label="name" prop="name" class="form-item">
            <el-input v-model="form.name"/>
          </el-form-item>
          <el-form-item label="age" prop="age" class="form-item">
            <el-input v-model="form.age"/>
          </el-form-item>
          <el-form-item label="gender" prop="gender" class="form-item">
            <el-input v-model="form.gender"/>
          </el-form-item>
          <el-form-item label="city" prop="city" class="form-item">
            <el-input v-model="form.city"/>
          </el-form-item>
          <el-form-item label="email" prop="email" class="form-item">
            <el-input v-model="form.email"/>
          </el-form-item>
          <div class="submit" >
            <el-button @click="submit" type="primary" color="#E00707" size="large">submit</el-button>
            <el-button @click="writeBack" type="danger" color="#6E7072" size="large" style="margin-left: 50px;">
              <span style="margin-top: 5px;margin-right: 5px"><el-icon><RefreshLeft /></el-icon></span>
              <span>reset</span>
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </template>
</template>

<script lang="ts" setup>
import {computed, onMounted, onUnmounted, ref, toRef, toRefs, watch, watchEffect} from 'vue';
import UseUserEdit from "@/hooks/UseUserEdit";
import UseUserInfo from "@/hooks/UseUserInfo";
import {useEditStore} from "@/stores/UseEditStore";

// 使用编辑store
let editStore = useEditStore();

// 使用用户信息hook
let {userInfo,getUserInfo} = toRefs(UseUserInfo());

// 使用编辑hook
let userEdit = UseUserEdit()
let {form, editSwitch, isEdit, submit,writeBack} = userEdit

onMounted(()=>{
  getUserInfo.value()
  writeBack()
})

onUnmounted(()=>{
  editStore.edit = false
})
</script>

<style scoped>

.container {
  align-self: center;
  background-color: #f5f5f5;
  font-family: Arial, sans-serif;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 40px;

}
.profile {
  text-align: center;
}
.profile img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 20px;
}
.profile h2 {
  font-size: 24px;
  color: #333333;
  margin: 0;
  margin-bottom: 10px;
}
.profile p {
  font-size: 16px;
  color: #777777;
  margin: 5px 0;
}
.profile-info {
  text-align: left;
  margin-top: 20px;
}
.profile-info div {
  margin-bottom: 10px;
}
.profile-info span {
  font-weight: bold;
  color: #333333;
}

.el-form {
  margin-left: 60px;
  margin-top: 20px;
}

.form-item {
  width: 600px;
}

.title-box {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.title {
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
}

.edit {
  color: #6E7072;
  font-size: 28px;
  cursor: pointer;
  display: flex;
  align-items: end;
}

.back {
  color: #6E7072;
  font-size: 28px;
  align-items: start;
  cursor: pointer;
  margin-left: auto;
}

.el-box {
  display: flex;
}

.submit {
  margin-top: 20px;
}

</style>