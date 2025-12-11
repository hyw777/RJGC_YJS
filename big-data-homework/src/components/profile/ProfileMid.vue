<template>
  <div class="mid">
    <div class="display">
      <div class="nav-box">
        <div class="nav-top">
          <div class="content-box">
            <div class="avatar-placeholder">
              <div class="avatar-initials">
                {{ getInitials(userInfo.name) }}
              </div>
            </div>
            <h2 class="name">{{ userInfo.name }}</h2>
            <p class="text">{{ userInfo.city }}</p>
            <div class="item-box">
              <div class="edit-box">
                <div class="icon-box" @click="editProfile">
                  <el-icon size="24">
                    <Edit />
                  </el-icon>
                </div>
                <div>编辑资料</div>
              </div>
              <div class="add-box">
                <router-link to="/findFriends" class="icon-box">
                  <el-icon color="#000000" size="24">
                    <Plus />
                  </el-icon>
                </router-link>
                <div>添加好友</div>
              </div>
            </div>
          </div>
        </div>
        <div class="nav-bottom">
          <router-link
            v-for="(item, index) in navItems"
            :key="index"
            :to="item.path"
            replace
            class="nav-bottom-content"
            :class="{ isActive: showButton === index }"
            @click="switchButton(index)"
          >
            <el-icon :size="24">
              <component :is="item.icon" />
            </el-icon>
            <div class="nav-bottom-content-style">{{ item.label }}</div>
          </router-link>
        </div>
      </div>
      <div class="info">
        <router-view :isEdit="isEdit"></router-view>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, toRefs } from "vue";
import {
  Coordinate,
  User,
  PriceTag,
  CollectionTag,
  Edit,
  Plus,
} from "@element-plus/icons-vue";
import UseUserInfo from "@/hooks/UseUserInfo";
import UseUserEdit from "@/hooks/UseUserEdit";
import { UseButtonStore } from "@/stores/UseButtonStore";

// 使用编辑hook
const userEdit = UseUserEdit();
const { form, editSwitch, isEdit, submit, editProfile } = userEdit;

// 使用用户信息hook
const { userInfo, getUserInfo } = toRefs(UseUserInfo());

// 用户默认头像
const avatar = ref(
  "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/OIP-C.jpg"
);

// 导航项数据
const navItems = [
  { path: "/profile/overview", icon: User, label: "个人资料概览" },
  { path: "/profile/reviews", icon: PriceTag, label: "我的评价" },
  { path: "/profile/collections", icon: CollectionTag, label: "收藏夹" },
  { path: "/profile/friends", icon: Coordinate, label: "好友列表" },
];

// 按钮状态管理
const buttonStore = UseButtonStore();
const showButton = computed(() => buttonStore.button);

function switchButton(num: number) {
  buttonStore.setButton(num);
}

// 计算头像显示内容：如果有头像URL则显示图片，否则显示名字首字母
const avatarDisplay = computed(() => {
  if (avatar.value) {
    return avatar.value;
  }
  // 如果没有头像，则返回名字的首字母
  return userInfo.value?.name
    ? userInfo.value.name.charAt(0).toUpperCase()
    : "U";
});

// 计算头像文字样式
const avatarTextStyle = computed(() => {
  if (avatar.value) {
    return {};
  }
  return {
    fontSize: "40px",
    fontWeight: "bold",
    color: "#fff",
  };
});
// 获取姓名首字母
const getInitials = (name: string) => {
  if (!name) return '?';
  return name.trim().charAt(0).toUpperCase();
}


onMounted(() => {
  getUserInfo.value();
});
</script>

<style scoped>
.mid {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
  padding: 20px;
}

.display {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  gap: 24px;
}

.nav-box {
  display: flex;
  flex-direction: column;
  width: 280px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background: white;
}

.nav-top {
  display: flex;
  flex-direction: column;
  padding: 32px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.content-box {
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  align-items: center;
}

/* 首字母头像样式 */
.avatar-placeholder {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.avatar-initials {
  font-size: 48px;
  font-weight: bold;
  color: white;
}

.name {
  font-size: 24px;
  font-weight: 600;
  color: white;
  margin: 16px 0 8px 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.text {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 20px 0;
}

.item-box {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-top: 10px;
  font-size: 14px;
  color: white;
}

.edit-box,
.add-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.icon-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 48px;
  height: 48px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.icon-box:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.nav-bottom {
  display: flex;
  flex-direction: column;
  padding: 16px 0;
  background: white;
}

.nav-bottom-content {
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 24px;
  cursor: pointer;
  color: #2d2e2f;
  text-decoration: none;
  transition: all 0.2s ease;
}

.nav-bottom-content:hover {
  background-color: #f5f7fa;
}

.isActive {
  background-color: #eef5ff;
  border-left: 4px solid #409eff;
  font-weight: 500;
}

.nav-bottom-content-style {
  margin-left: 16px;
  font-size: 15px;
}

.info {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .display {
    flex-direction: column;
    gap: 20px;
  }

  .nav-box {
    width: 100%;
  }

  .info {
    margin-right: 0;
  }
}
</style>
