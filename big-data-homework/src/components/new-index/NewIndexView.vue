<template>
  <div class="view" :style="view">
    <el-dialog
      v-model="merchantVisible"
      title="申请成为商家"
      width="600"
      class="merchant-dialog"
    >
      <el-form
        :model="merchantForm"
        label-width="auto"
        style="max-width: 450px"
      >
        <el-form-item label="商家名" prop="name">
          <el-input v-model="merchantForm.name" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="merchantForm.address" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="merchantForm.city" />
        </el-form-item>
        <el-form-item label="州" prop="state">
          <el-input v-model="merchantForm.state" />
        </el-form-item>
        <el-form-item label="分类" prop="categories">
          <el-input v-model="merchantForm.categories" />
        </el-form-item>
        <el-form-item label="营业时间" prop="hours">
          <el-input v-model="merchantForm.hours" />
        </el-form-item>
        <el-form-item label="儿童适宜" prop="goodForKids">
          <el-radio-group v-model="merchantForm.goodForKids" class="ml-4">
            <el-radio value="yes" size="large">是</el-radio>
            <el-radio value="no" size="large">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="接受信用卡" prop="businessAcceptsCreditcards">
          <el-radio-group
            v-model="merchantForm.businessAcceptsCreditcards"
            class="ml-4"
            style="color: #e00707"
          >
            <el-radio value="true" size="large">是</el-radio>
            <el-radio value="false" size="large">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="merchantVisible = false">取消</el-button>
          <el-button type="primary" @click="submitMerchan" color="#E00707">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="loginVisible" title="登录" width="500">
      <el-form :model="loginData" label-width="auto" style="max-width: 450px">
        <el-form-item label="邮箱" prop="mail">
          <el-input v-model="loginData.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginData.password" type="password" />
        </el-form-item>
      </el-form>
      <div class="pwd">
        <span
          class="find"
          @click="
            loginVisible = false;
            findVisible = true;
          "
          >忘记密码</span
        >
        <el-checkbox
          style="margin-left: 65%"
          v-model="check"
          label="记住密码"
          size="large"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button
            @click="
              loginVisible = false;
              loginData.password = '';
            "
            >取消</el-button
          >
          <el-button
            type="primary"
            @click="
              loginVisible = false;
              login(loginData);
            "
            color="#E00707"
          >
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="findVisible" title="密码找回" width="500">
      <el-form :model="findData" label-width="auto" style="max-width: 340px">
        <div class="mail-line">
          <el-form-item label="邮箱号" prop="email" style="width: 300px">
            <el-input v-model="findData.email" />
          </el-form-item>
          <div style="margin-left: 40px; width: 100px">
            <span style="cursor: pointer" @click="sendEmail(findData.email)"
              >发送验证码</span
            >
          </div>
        </div>
        <el-form-item label="验证码" prop="code" style="max-width: 160px">
          <el-input v-model="findData.code" />
        </el-form-item>
        <el-form-item label="新密码" prop="password" style="width: 226px">
          <el-input v-model="findData.newPassword" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="findVisible = false">取消</el-button>
          <el-button
            color="#e00707"
            type="primary"
            @click="
              findVisible = false;
              findPassword(findData);
            "
          >
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="signUpVisible" title="注册" width="600">
      <el-form
        :model="signUpData"
        label-width="auto"
        style="width: 400px; margin-left: 70px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="signUpData.name" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="signUpData.password" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group
            v-model="signUpData.gender"
            class="ml-4"
            text-color="#E00707"
          >
            <el-radio value="male" size="large">男</el-radio>
            <el-radio value="female" size="large">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="signUpData.age" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="signUpData.city" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email" style="margin-bottom: 5px">
          <el-input v-model="signUpData.email" />
        </el-form-item>
        <el-form-item>
          <span
            style="margin-left: 80%; cursor: pointer"
            @click="sendEmail(signUpData.email)"
            >发送验证码</span
          >
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width: 200px">
          <el-input v-model="signUpData.code" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="signUpVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="
              signUpVisible = false;
              register(signUpData);
            "
            color="#E00707"
          >
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <div class="nav">
      <div class="nav-1">
        <router-link to="/" class="logo"></router-link>
        <div class="input-box">
          <div class="input">
            <el-input
              v-model="info"
              placeholder="找找你喜欢的商户"
              @click="
                display = !display;
                getHotSearchTop10();
              "
            ></el-input>
            <el-card style="max-width: 400px" v-show="display">
              <template #header>
                <div class="card-header">
                  <span>猜你想搜</span>
                </div>
              </template>
              <p
                v-for="(item, index) in hotSearchRank"
                :key="index"
                class="text item"
                @click="searchTop10ToSearch(item)
                "
              >
                {{ index + 1 + " " + item.searchContent }}
              </p>
            </el-card>
          </div>
         <div @click="openUpload" class="camera">
            <el-icon size="30px" color="white">
              <Camera />
            </el-icon>
          </div>
          <div @click="reload()" class="search">
            <el-icon size="30px" color="white">
              <Search />
            </el-icon>
          </div>
          
        </div>
        <div class="item">
          <router-link
            to="/adminPage"
            v-if="userType == 'admin'"
            style="margin-right: 50px"
          >
            <el-icon color="#E00707" size="25" class="boss">
              <Avatar />
            </el-icon>
          </router-link>
          <router-link
            to="/boss"
            v-if="userType == 'boss'"
            style="margin-right: 50px"
          >
            <el-icon color="#E00707" size="25" class="boss">
              <Shop />
            </el-icon>
          </router-link>
          <div
            v-if="userType != 'admin'"
            class="apply item-style"
            @click="merchantVisible = true"
          >
            <span>Apply for Business</span>
          </div>
          <template v-if="token">
            <div style="display: flex">
              <div class="notifications item-style2" @click="switchExpand">
                <div
                  class="notifications-container"
                  @click.stop
                  v-show="isExpanded"
                >
                  <div
                    class="notification-box"
                    v-for="(info, index) in applyInfo"
                    :key="index"
                  >
                    <div class="row1">
                      <span style="font-size: 20px">{{ info.name }}</span>
                      <span style="color: #6e7072">{{
                        formatDateTime(info.time)
                      }}</span>
                    </div>
                    <span class="row1.5">{{ info.reason }}</span>
                    <div class="row2">
                      <el-icon
                        @click="agree(info.applicantId)"
                        style="cursor: pointer"
                        size="20"
                        color="green"
                        ><Select
                      /></el-icon>
                      <el-icon
                        @click="disagree(info.applicantId)"
                        style="cursor: pointer"
                        size="20"
                        color="#E00707"
                        ><CloseBold
                      /></el-icon>
                    </div>
                  </div>
                </div>
                <el-badge :show-zero="false" :value="notifications">
                  <el-icon size="24px" color="black">
                    <Bell />
                  </el-icon>
                </el-badge>
              </div>
              <div
                class="avatar"
                style="margin-left: 15px"
                @click.stop="select"
              >
                <el-avatar class="el-avatar" :size="44" :src="circleUrl" />
                <div
                  class="avatar-select"
                  v-show="isSelected == true"
                  ref="selectRef"
                >
                  <div class="select-box">
                    <router-link
                      class="container-box"
                      to="/profile"
                      @click="jumpFirst"
                    >
                      <el-icon size="24"> <User /> </el-icon>&nbsp;&nbsp;About
                      Me
                    </router-link>
                    <router-link
                      to="/profile/collections"
                      class="container-box"
                    >
                      <el-icon size="24"> <CollectionTag /> </el-icon
                      >&nbsp;&nbsp;My Collections
                    </router-link>
                    <router-link to="/findFriends" class="container-box">
                      <el-icon size="24"> <Plus /> </el-icon>&nbsp;&nbsp;Find
                      Friends
                    </router-link>
                    <div class="container-box" @click="logout">
                      <el-icon size="24"> <SwitchButton /> </el-icon
                      >&nbsp;&nbsp;Log Out
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="login" @click="writeBack">Log In</div>
            <div class="signup" @click="signUpVisible = true">Sign Up</div>
          </template>
        </div>
      </div>
      <div class="nav-2"></div>
    
    <!-- Upload Dialog -->
    <el-dialog v-model="uploadVisible" title="上传图片" width="640">
      <div class="upload-body">
        <input type="file" accept="image/*" @change="onFileChange" />
        <div class="preview" v-if="previewUrl">
          <img :src="previewUrl" alt="preview" />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="uploadVisible = false">取消</el-button>
          <el-button type="primary" color="#E00707" @click="submitUpload">上传</el-button>
        </div>
      </template>
    </el-dialog>
     <!-- Background Image Description -->
    <div class="background-description" v-show="showBackgroundDescription && backgroundImage[imageIndex].description">
      <div class="desc-text">{{ backgroundImage[imageIndex].description }}</div>
      <div class="desc-actions">
        <el-button class="detail-btn" type="primary" @click="goToDetails">
          <el-icon><Search /></el-icon>
          查看详情
        </el-button>
      </div>
    </div>
   
    </div>
  </div>
</template>

<script setup lang="ts">
// 添加 showBackgroundDescription 属性定义
const props = defineProps({
  showBackgroundDescription: {
    type: Boolean,
    default: true
  }
});

import { computed, onMounted, onUnmounted, ref, toRefs } from "vue";
// ElMessage 是 Element Plus 提供的全局消息方法，声明用于 TypeScript 校验
declare const ElMessage: any;
import { useAuth } from "@/hooks/UseAuth";
import { useAuthStore } from "@/stores/UseAuthStore";
import UseRegister from "@/hooks/UseRegister";
import UseCode from "@/hooks/UseCode";
import { UseButtonStore } from "@/stores/UseButtonStore";
import UseFind from "@/hooks/UseFind";
import { useMerchant } from "@/hooks/UseMerchant";
import { useSearch } from "@/hooks/UseSearch";
import { router } from "@/router";
import { UseSearchStore } from "@/stores/UseSearchStore";
import { useAddFriends } from "@/hooks/UseAddFriends";

function formatDateTime(dateTime) {
  const date = new Date(dateTime);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

let searchStore = UseSearchStore();

let { info, search, result, getResult } = toRefs(useSearch());

let {
  notifications,
  getNotifications,
  isExpanded,
  switchExpand,
  applyInfo,
  getApplyInfo,
  agreeInfo,
  agree,
  disagree,
} = toRefs(useAddFriends());

function reload() {
  router.push({
    name: "Search", // 假设你的路由配置中有一个名为 'Search' 的命名路由
    query: { info: info.value },
  });
  getResult.value(1, info.value);
}

function goToDetails() {
  // Use the current background description as the search query and navigate to Search
  const desc = backgroundImage.value[imageIndex.value]?.description || '';
  console.log("Searching for:", desc);
  router.push({ name: 'Search', query: { info: desc } });
  // trigger the search result load
  try {
    getResult.value(1, desc);
  } catch (e) {
    // safe no-op if getResult is not available
  }
}

let { merchantVisible, merchantForm, submitMerchan } = toRefs(useMerchant());

// 使用找回hook
let { findData, findPassword } = UseFind();
let findVisible = ref(false);

// 发送验证码
let { sendEmail } = UseCode();

// 用户注册
let { signUpData, register } = UseRegister();

// 注册框展示
let signUpVisible = ref(false);

// 获取store
const auth = useAuth();

// 使用auth hook
let { login, logout, loginData, loginVisible, writeBack, check } = toRefs(auth);
let authStore = useAuthStore();
let token = computed(() => authStore.token);
let userType = computed(() => authStore.userType);

let circleUrl = "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/OIP-C.jpg";
let backgroundImage = ref([
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/4.jpg",
    key: 1,
    description: "探索城市中的美味佳肴"
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/3.jpg",
    key: 2,
    description: "感受都市夜晚的璀璨灯火"
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/1.jpg",
    key: 3,
    description: "拥抱大自然的宁静美景"
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/2.jpg",
    key: 4,
    description: "体验现代城市的繁华生活"
  },
]);
let imageIndex = ref(0);
let imageUrl = ref(backgroundImage.value[imageIndex.value].url);
let view = ref({
  "background-image": `url(${imageUrl.value})`,
  height: 800 + "px",
  "background-repeat": "no-repeat",
  "background-size": "cover",
  "background-position": "30% 25%",
});

function changeImage() {
  imageIndex.value = (imageIndex.value + 1) % 4;
  imageUrl.value = backgroundImage.value[imageIndex.value].url;
  view.value = {
    "background-image": `url(${imageUrl.value})`,
    height: 800 + "px",
    "background-repeat": "no-repeat",
    "background-size": "cover",
    "background-position": "30% 25%",
  };
  // console.log('更换背景图片... '+'当前图片URL: '+imageUrl.value+' 样式对象url: '+view.value["background-image"]);
}

let intervalId;

let selectRef = ref(null);
let isSelected = ref(false);

function select() {
  isSelected.value = !isSelected.value;
}

function handleClickOutside(event) {
  // 判断点击是否发生在下拉框外部
  if (isSelected.value && !selectRef.value.contains(event.target)) {
    isSelected.value = false;
  }
}

// 点击about me跳转到overview
function jumpFirst() {
  let buttonStore = UseButtonStore();
  buttonStore.setButton(0);
}

onMounted(async () => {
  intervalId = setInterval(changeImage, 5000);

  // 添加全局点击事件监听器
  document.addEventListener("click", handleClickOutside);

  if (authStore.token) {
    await getNotifications.value();
    await getApplyInfo.value();
  }
});

onUnmounted(() => {
  // 在组件销毁时清除定时器
  clearInterval(intervalId);

  // 移除全局点击事件监听器
  document.removeEventListener("click", handleClickOutside);
});

// 热搜榜*************************************************
import axios from "axios";

const display = ref(false);

const hotSearchRank = ref([]);

const getHotSearchTop10 = () => {
  axios.get("/ppi/hot_search/top10").then((resp) => {
    hotSearchRank.value = resp.data;
    console.log(resp);
  });
};

function searchTop10ToSearch(searchContent) {
  router.push({
    name: "Search", // 假设你的路由配置中有一个名为 'Search' 的命名路由
    query: { info: searchContent },
  });
  getResult.value(1, searchContent);
}

// Upload dialog state and handlers
import { ref as vueRef } from 'vue';

const uploadVisible = vueRef(false);
const uploadFile = vueRef<File | null>(null);
const previewUrl = vueRef('');

function openUpload() {
  uploadVisible.value = true;
}

function onFileChange(e: Event) {
  const input = e.target as HTMLInputElement;
  const file = input.files && input.files[0];
  if (file) {
    uploadFile.value = file;
    previewUrl.value = URL.createObjectURL(file);
  } else {
    uploadFile.value = null;
    previewUrl.value = '';
  }
}

async function submitUpload() {
  if (!uploadFile.value) {
    ElMessage({ message: '请选择图片后再上传', type: 'warning' });
    return;
  }
  const formData = new FormData();
  formData.append('file', uploadFile.value);
  formData.append('bId', "yyy");
  try {
    // 默认 POST 到 /api/images/upload，按需修改
    // Let axios set the Content-Type (including boundary) automatically
    const resp = await axios.post('/api/images/uploadImage', formData);
    ElMessage({ message: '上传成功', type: 'success' });
    uploadVisible.value = false;
    // 清理
    uploadFile.value = null;
    previewUrl.value = '';
  } catch (err) {
    console.error(err);
    ElMessage({ message: '上传失败', type: 'error' });
  }
}
</script>

<style scoped>
.mail-line {
  display: flex;
  justify-content: space-between;
}

.find {
  cursor: pointer;
  color: #6e7072;
  font-size: 13px;
}

.avatar {
  position: relative;
}

.el-avatar {
  cursor: pointer;
}

.avatar-select {
  display: flex;
  position: absolute;
  width: 226px;
  background-color: white;
  left: -390%;
  border-radius: 5px;
  z-index: 3;
}

.select-box {
  display: flex;
  flex-direction: column;
  margin: 16px;
  flex-grow: 1;
  border-radius: 5px;
}

.container-box {
  display: flex;
  align-items: center;
  padding: 8px;
  height: 35px;
  color: #2d2e2f;
  border-radius: 5px;
  cursor: pointer;
}

.container-box:hover {
  background-color: rgba(235, 235, 235, 1);
}

.nav {
  padding: 36px 40px 0px 40px;
}

.nav-1 {
  display: flex;
  height: 48px;
  justify-content: space-between;
  align-items: end;
}

.nav-2 {
  display: flex;
  height: 58px;
}

.logo {
  height: 40px;
  width: 4.5%;
  background-image: url("https://s3-media0.fl.yelpcdn.com/assets/public/logo_desktop.yji-0a2bf1d9c330d8747446.svg");
  background-size: contain;
  background-repeat: no-repeat;
  cursor: pointer;
}

.input-box {
  height: 48px;
  width: 51%;
  display: flex;
}

.input {
  height: 48px;
  width: 93%;
}

.search {
  height: 48px;
  width: 7%;
  background-color: #e00707;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

:deep(.el-input) {
  height: 100%;
}

.item {
  display: flex;
  justify-content: end;
  align-items: center;
  height: 44px;
  width: 27.3%;
  color: #ffffff;
  font-size: 14px;
}

.item-style:hover {
  background-color: rgba(209, 209, 209, 0.2);
  border-radius: 7px;
}

.item-style2:hover {
  background-color: rgba(209, 209, 209, 0.2);
  border-radius: 35px;
}

.projects {
  padding: 8px;
  cursor: pointer;
}

.notifications {
  padding: 8px;
  cursor: pointer;
  position: relative;
}

.notifications-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  width: 300px;
  height: 540px;
  background-color: #ffffff;
  left: -330px;
  top: 50px;
  border-radius: 5px;
  background-size: contain;
  background-repeat: no-repeat;
  overflow: auto;
  padding: 30px;
  cursor: auto;
}

.notification-box {
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-bottom: 35px;
  color: #2d2e2f;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
}

.row1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.row1.5 {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
}

.row2 {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  margin-bottom: 20px;
}

.apply {
  margin: 0px 10px;
  padding: 12px;
  cursor: pointer;
  color: #2d2e2f;
}

.login {
  height: 42px;
  width: 16.4%;
  border: 1px solid rgba(200, 201, 202, 1);
  font-size: 16px !important;
  font-weight: 540 !important;
  text-align: center;
  color: #ffffff;
  line-height: 37px;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 15px;
}

.signup {
  height: 44px;
  width: 19%;
  font-size: 16px !important;
  font-weight: 540 !important;
  text-align: center;
  color: #ffffff;
  line-height: 37px;
  background-color: #e00707;
  border-radius: 5px;
  cursor: pointer;
}

.nav-2-row-el :focus {
  outline: none;
}

.item {
  color: #e00707;
}

.camera {
  height: 48px;
  width: 7%;
  background-color: #6e7072;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  margin-left: 8px;
}

.upload-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.upload-body .preview img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 6px;
}

.background-description {
  position: absolute;
  top: 50%;
  left: 200px;
  transform: translateY(-50%);
  color: white;
  font-size: 24px;
  font-weight: bold;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8);
  background-color: rgba(0, 0, 0, 0.3);
  padding: 10px 20px;
  border-radius: 10px;
  backdrop-filter: blur(5px);
}

.background-description .desc-text {
  max-width: 680px;
  word-wrap: break-word;
}

.background-description .desc-actions {
  margin-top: 12px;
}

.background-description .detail-btn {
  background-color: rgba(255,255,255,0.12);
  border-color: rgba(255,255,255,0.18);
  color: #fff;
}

.background-description .detail-btn :deep(.el-icon) {
  margin-right: 8px;
}
</style>
