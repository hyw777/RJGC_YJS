<template>
  <div class="mid">
    <div class="box">
      <el-form :model="userInfo" label-width="auto" size="default" style="max-width: 600px" :rules="rules" class="form">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userInfo.email"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userInfo.password" type="password"/>
        </el-form-item>
      </el-form>
      <div class="register-row">
        <el-button type="primary" class="register-button color" text @click="register()">注册</el-button>
      </div>
      <el-button type="primary" color="#ff6633" class="login-button" @click="login()">登录</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {router} from "@/router";
import {ref} from "vue"
import useLogin from "@/hooks/UseLogin"
let {userInfo,login} = useLogin()
let state = ref(0) // 状态0为登录页面,1为注册页面
let buttonName = ref('注册') //默认按钮为注册
let buttonName2 = ref('登录') //默认按钮2为注册
let emits = defineEmits(['loginOrRegister']) // 绑定emit事件
let messageValidate = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/ //正则表达式
const rules = { // 定义校验规则
  email:[
    {
      required: true,
      pattern:messageValidate,
      message:'请输入正确的邮箱',
      trigger:'change'
    }
  ],
  password:[
    {
      required:true,
      message:'请输入密码',
      trigger:'change'
    }
  ]
}


function register() {
  router.push('/register')
}
</script>

<style scoped>

.mid {
  display: flex;
  background-color: #F7F7F7;
  background-image: url("https://p0.meituan.net/dppclogin/0e02c95f7baaf00a3ddbd3b4068e3756207203.png");
  background-repeat: no-repeat;
  background-size: 30vw 30vw;
  background-position: 1vw -1vw;
  margin: 0 21vw;
  height: 600px;
  flex-direction: row-reverse;
  align-items: center;
  width: 60%;
}

.box {
  display: flex;
  flex-direction: column;
  margin-right: 8vw;
  width: 27%;
}

.form{
  width: 100%;
}

.login-button {
  width: 100%;
  margin-top: 20px;
}

.register-row {
  display: flex;
  justify-content: end;
}

.register-button {
  color: #666666;
  font-size: 13px;
}

</style>