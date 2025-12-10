<template>
  <el-container class="admin-layout">
    <el-header class="admin-header">
      <NewIndexView
        :show-background-description="false"
        class="compact-navbar"
      />
    </el-header>
    <el-container class="main-container">
      <el-aside width="200px" class="sidebar">
        <el-menu 
          class="sidebar-menu"
          :default-active="$route.path"
          router
        >
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Operation /></el-icon>
              <span>管理中心</span>
            </template>
            <el-menu-item index="/adminPage/userManage">
              <el-icon><Avatar /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/adminPage/businessApplyManage">
              <el-icon><Shop /></el-icon>
              <span>商户申请管理</span>
            </el-menu-item>
            <el-menu-item index="/adminPage/businessManage">
              <el-icon><Shop /></el-icon>
              <span>商户管理</span>
            </el-menu-item>
            <el-menu-item index="/adminPage/reviewManage">
              <el-icon><ChatLineSquare /></el-icon>
              <span>评价管理</span>
            </el-menu-item>
            <!-- <el-menu-item index="/adminPage/profileCenter">
              <el-icon><User /></el-icon>
              <span>画像中心</span>
            </el-menu-item> -->
          </el-sub-menu>
          <!-- <el-menu-item index="2" @click="openNewTab">
            <el-icon><Monitor /></el-icon>
            <span>工作台</span>
          </el-menu-item> -->
        </el-menu>
      </el-aside>
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useAuth } from "@/hooks/UseAuth";
import { router } from "@/router";
import NewIndexView from "@/components/new-index/NewIndexView.vue";
const { logout } = useAuth();

// // 跳转标签平台
// const openNewTab = () => {
//   const url = "http://userprofile.gmall.com"; // 替换为另一个项目的完整 URL
//   window.open(url, "_blank");
// };

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
.admin-layout {
  height: 100vh;
  background-color: #f5f7fa;
}

.admin-header {
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 100;
  border-bottom: 1px solid #e4e7ed;

}

.compact-navbar {
  background-image: none !important;
  min-height: 40px;
}

:deep(.compact-navbar .view) {
  height: auto !important;
  min-height: 40px;
  background-image: none !important;
  padding-bottom: 0;
  box-shadow: none;
}

:deep(.compact-navbar .modern-nav) {
  background: white !important;
}

.main-container {
  height: calc(100vh - 60px);
}

.sidebar {
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.sidebar-menu {
  margin-top: 20px;
  border-right: none !important;
  height: 96%;
  background: transparent;
}

.sidebar-menu :deep(.el-sub-menu__title),
.sidebar-menu :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  margin: 2px 8px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sidebar-menu :deep(.el-sub-menu__title:hover),
.sidebar-menu :deep(.el-menu-item:hover) {
  background-color: #e6f7ff;
  color: #1890ff;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background-color: #1890ff;
  color: white;
}

.sidebar-menu :deep(.el-menu-item.is-active .el-icon),
.sidebar-menu :deep(.el-menu-item.is-active span) {
  color: white;
}

.main-content {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin: 15px;
  overflow-y: auto;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .sidebar {
    width: 60px;
  }
  
  .sidebar :deep(.el-menu-item span),
  .sidebar :deep(.el-sub-menu__title span) {
    display: none;
  }
  
  .sidebar :deep(.el-menu-item .el-icon),
  .sidebar :deep(.el-sub-menu__title .el-icon) {
    margin-right: 0;
  }
}
</style>
