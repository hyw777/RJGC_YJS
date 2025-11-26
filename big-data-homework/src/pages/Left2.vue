<template>
  <el-container>
    <el-header style="border-bottom: 1px solid #dedfe0">
      <img
        src="https://s3-media0.fl.yelpcdn.com/assets/public/logo_desktop.yji-0a2bf1d9c330d8747446.svg"
        mode="scaleToFill"
        width="100px"
        @click="router.push('/')"
        style="margin: 0 0 0 20px"
      />
      <el-icon
        size="30"
        @click="logout"
        style="float: right; margin-right: 50px"
        ><SwitchButton
      /></el-icon>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu class="el-menu-vertical-demo">
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Operation /></el-icon>
              <span>管理中心</span>
            </template>
            <el-menu-item
              index="1-1"
              @click="router.push('/adminPage/userManage')"
              ><el-icon><Avatar /></el-icon>用户管理</el-menu-item
            >
            <el-menu-item
              index="1-2"
              @click="router.push('/adminPage/businessManage')"
              ><el-icon><Shop /></el-icon>商家管理</el-menu-item
            >
            <el-menu-item
              index="1-3"
              @click="router.push('/adminPage/reviewManage')"
              ><el-icon><ChatLineSquare /></el-icon>评价管理</el-menu-item
            >
            <el-menu-item
                index="1-4"
                @click="router.push('/adminPage/profileCenter')"
            ><el-icon><ChatLineSquare /></el-icon>画像中心</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="2" @click="openNewTab">
            <template #title>
              <el-icon><Flag /></el-icon>
              <span>工作台</span>
            </template>
          </el-menu-item>
        </el-menu></el-aside
      >
      <el-main><router-view></router-view></el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useAuth } from "@/hooks/UseAuth";
import { router } from "@/router";

const { logout } = useAuth();

// 跳转标签平台
const openNewTab = () => {
  const url = "http://userprofile.gmall.com"; // 替换为另一个项目的完整 URL
  window.open(url, "_blank");
};

// 下拉菜单
import { ref } from "vue";
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from "@element-plus/icons-vue";
</script>

<style scoped>
.el-aside {
  height: calc(100vh - 60px);
}
.el-container {
  height: 100vh;
}
.el-menu {
  height: calc(100vh - 60px);
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
