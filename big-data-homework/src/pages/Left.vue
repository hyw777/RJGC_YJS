<template>
  <div class="top">
    <div class="logo" >
      <p style="margin-left: 77vh ;margin-top: -10px;font-size: 38px">AdminPage</p>
    </div>
    <div class="logout" @click="logout">
      <el-icon size="30" style="top:10px">
        <SwitchButton/>
      </el-icon>&nbsp;&nbsp;
    </div>
  </div>
    <div class="left">
      <el-dropdown>
        <el-button type="primary"  class="manage-center">
          <el-icon style="font-size: 40px;"><User /></el-icon>
          <el-icon style="font-size: 30px"><Tools /></el-icon>
         <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu class="menu">
            <el-dropdown-item class="item" @click="router.push('/adminPage/userManage')">用户管理</el-dropdown-item>
            <el-dropdown-item class="item" @click="router.push('/adminPage/businessManage')" >
              商家管理
            </el-dropdown-item>
            <el-dropdown-item class="item" @click="router.push('/adminPage/reviewManage')" >评价管理</el-dropdown-item>
            <el-dropdown-item class="item" @click="router.push('/adminPage/profileCenter')">画像中心</el-dropdown-item>
            <el-dropdown-item class="item">标签平台</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </div>
  <div class="right">
    <router-view ></router-view>
  </div>
  </template>

<script lang="ts" setup>
import { useAuth } from "@/hooks/UseAuth";
import { router } from "@/router";
let {logout} = useAuth()
</script>

<style scoped>
.top{
  width: 100%;
  height: 90px;
  background-color: #f3f8fb;
  position: relative;
}

.logo {
  height: 40px;
  width: 100px;
  background-image: url("https://s3-media0.fl.yelpcdn.com/assets/public/logo_desktop.yji-0a2bf1d9c330d8747446.svg");
  background-size: contain;
  background-repeat: no-repeat;
  cursor: pointer;
  margin-left: 40px;
  padding-top: 30px;
}

.logout{
    height: 50px;
    width: 80px;
    bottom: 30px;
    right: 0;
    position: absolute;
}

.left{
  background-color: #ffffff;
  border: #f1f1f1 1px solid;
  width: 290px;
  height: 99vh;
  position: fixed;
  display: flex;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}


.dropdown-content a:hover {background-color: #766c6c;}

.manage-center{
  width: 290px;
  height: 70px;
  position:static;
}


.menu{
  font-size: 80px;
  width: 260px;
}



</style>