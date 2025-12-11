<template>
  <div class="view" :style="view">
    <el-dialog
      v-model="merchantVisible"
      title="申请成为商家"
      width="700"
      class="merchant-dialog"
    >
      <el-form
        :model="merchantForm"
        label-width="auto"
        style="max-width: 600px"
      >
        <el-form-item label="商家名" prop="name">
          <el-input v-model="merchantForm.name" />
        </el-form-item>

        <el-form-item label="地址" prop="address">
          <el-input
            v-model="merchantForm.address"
            @input="autoFillCoordinates"
            placeholder="请输入详细地址"
          />
        </el-form-item>

        <el-form-item label="城市" prop="city">
          <el-input v-model="merchantForm.city" />
        </el-form-item>

        <el-form-item label="州" prop="state">
          <el-input v-model="merchantForm.state" />
        </el-form-item>

        <el-form-item label="邮政编码" prop="postalCode">
          <el-input v-model="merchantForm.postalCode" />
        </el-form-item>

        <el-form-item label="纬度" prop="latitude">
          <el-input-number
            v-model="merchantForm.latitude"
            :precision="8"
            disabled
          />
        </el-form-item>

        <el-form-item label="经度" prop="longitude">
          <el-input-number
            v-model="merchantForm.longitude"
            :precision="8"
            disabled
          />
        </el-form-item>

        <el-form-item label="分类" prop="categories">
          <el-input v-model="merchantForm.categories" />
        </el-form-item>

        <el-form-item label="营业时间" prop="hours">
          <div
            v-for="(time, day) in businessHours"
            :key="day"
            class="hours-item"
          >
            <span class="day-label">{{ day }}:</span>
            <el-time-picker
              v-model="businessHours[day].start"
              placeholder="开始时间"
              format="HH:mm"
              value-format="HH:mm"
              width="80px"
            />
            <span class="separator">-</span>
            <el-time-picker
              v-model="businessHours[day].end"
              placeholder="结束时间"
              format="HH:mm"
              value-format="HH:mm"
              width="80px"
            />
          </div>
        </el-form-item>

        <el-form-item label="商户属性" prop="attributes">
          <div class="attributes-container">
            <!-- WiFi选项 -->
            <div class="attribute-item">
              <el-checkbox
                v-model="selectedAttributes"
                label="WiFi"
                class="main-checkbox"
              >
                提供WiFi
              </el-checkbox>
              <el-select
                v-model="wifiOption"
                placeholder="WiFi类型"
                size="small"
                class="attribute-select"
                :disabled="!selectedAttributes.includes('WiFi')"
              >
                <el-option label="免费WiFi" value="u'free'"></el-option>
                <el-option label="无WiFi" value="u'no'"></el-option>
              </el-select>
            </div>

            <!-- Alcohol选项 -->
            <div class="attribute-item">
              <el-checkbox
                v-model="selectedAttributes"
                label="Alcohol"
                class="main-checkbox"
              >
                提供酒精饮品
              </el-checkbox>
              <el-select
                v-model="alcoholOption"
                placeholder="酒水类型"
                size="small"
                class="attribute-select"
                :disabled="!selectedAttributes.includes('Alcohol')"
              >
                <el-option
                  label="啤酒和葡萄酒"
                  value="'beer_and_wine'"
                ></el-option>
                <el-option label="不提供" value="'none'"></el-option>
              </el-select>
            </div>

            <!-- 其他选项 -->
            <el-checkbox v-model="selectedAttributes" label="OutdoorSeating"
              >户外座位</el-checkbox
            >
            <el-checkbox v-model="selectedAttributes" label="RestaurantsTakeOut"
              >提供外卖</el-checkbox
            >
            <el-checkbox
              v-model="selectedAttributes"
              label="RestaurantsDelivery"
              >提供外带</el-checkbox
            >
            <el-checkbox
              v-model="selectedAttributes"
              label="BusinessAcceptsCreditCards"
              >接受信用卡</el-checkbox
            >
            <el-checkbox v-model="selectedAttributes" label="DogsAllowed"
              >允许宠物入内</el-checkbox
            >
            <el-checkbox v-model="selectedAttributes" label="GoodForKids"
              >适合儿童</el-checkbox
            >
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="merchantVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="handleMerchantSubmit"
            color="#E00707"
          >
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

    <div class="modern-nav">
      <div class="nav-container">
        <div class="nav-content">
          <!-- Logo 区域 -->
          <router-link to="/" class="logo">
            <el-icon size="40" color="#1890ff">
              <ShoppingCart />
            </el-icon>
          </router-link>

          <!-- 搜索区域 -->
          <div class="search-area">
            <div class="input-box">
              <div class="input-wrapper">
                <el-input
                  v-model="info"
                  placeholder="搜索商户、服务或商品..."
                  @click="
                    display = !display;
                    getHotSearchTop10();
                  "
                  class="search-input"
                ></el-input>
                <!-- <el-card class="hot-search-card" v-show="display">
                  <template #header>
                    <div class="card-header">
                      <span>热门搜索</span>
                    </div>
                  </template>
                  <p
                    v-for="(item, index) in hotSearchRank"
                    :key="index"
                    class="text item"
                    @click="searchTop10ToSearch(item)"
                  >
                    {{ index + 1 + ". " + item.searchContent }}
                  </p>
                </el-card> -->
              </div>
              <div @click="openUpload" class="action-button camera-button">
                <el-icon size="20px" color="#ffffff">
                  <Camera />
                </el-icon>
              </div>
              <div @click="reload()" class="action-button search-button">
                <el-icon size="20px" color="#ffffff">
                  <Search />
                </el-icon>
              </div>
            </div>
          </div>

          <!-- 用户操作区域 -->
          <div class="user-actions">
            <template v-if="token">
              <!-- 商家/管理员入口 -->
              <router-link
                to="/adminPage"
                v-if="userType == 'admin'"
                class="admin-link"
              >
                <el-icon color="#1890ff" size="24">
                  <Avatar />
                </el-icon>
                <span class="link-text">管理后台</span>
              </router-link>

              <router-link
                to="/boss"
                v-if="userType == 'boss'"
                class="boss-link"
              >
                <el-icon color="#1890ff" size="24">
                  <Shop />
                </el-icon>
                <span class="link-text">商家中心</span>
              </router-link>

              <!-- 商家申请 -->
              <div
                v-if="userType != 'admin'"
                class="apply-business"
                @click="merchantVisible = true"
              >
                <el-icon color="#1890ff" size="20">
                  <Shop />
                </el-icon>
                <span class="apply-text">申请成为商家</span>
              </div>

              <!-- 通知区域 -->
              <div class="notifications-wrapper">
                <div class="notifications item-style2" @click="switchExpand">
                  <div
                    class="notifications-container"
                    @click.stop
                    v-show="isExpanded"
                  >
                    <div class="notifications-header">
                      <h3>好友申请</h3>
                      <span class="notification-count" v-if="notifications > 0"
                        >{{ notifications }}个未读</span
                      >
                    </div>
                    <div class="notifications-list">
                      <div
                        class="notification-box"
                        v-for="(info, index) in applyInfo"
                        :key="index"
                      >
                        <div class="notification-avatar">
                          <div class="avatar-placeholder">
                            {{ getInitials(info.name) }}
                          </div>
                        </div>
                        <div class="notification-content">
                          <div class="notification-header">
                            <span class="user-name">{{ info.name }}</span>
                            <span class="notification-time">{{
                              formatDateTime(info.time)
                            }}</span>
                          </div>
                          <div class="notification-message">
                            {{ info.reason }}
                          </div>
                          <div class="notification-actions">
                            <el-button
                              size="small"
                              type="success"
                              @click="agree(info.applicantId)"
                              round
                            >
                              <el-icon><Check /></el-icon> 接受
                            </el-button>
                            <el-button
                              size="small"
                              type="danger"
                              @click="disagree(info.applicantId)"
                              round
                            >
                              <el-icon><Close /></el-icon> 拒绝
                            </el-button>
                          </div>
                        </div>
                      </div>
                      <div
                        class="no-notifications"
                        v-if="applyInfo.length === 0"
                      >
                        <el-icon><Bell /></el-icon>
                        <p>暂无好友申请</p>
                      </div>
                    </div>
                  </div>
                  <el-badge
                    :show-zero="false"
                    :value="notifications"
                    class="badge-item"
                  >
                    <el-icon size="24px" color="#1890ff">
                      <BellFilled />
                    </el-icon>
                  </el-badge>
                </div>
              </div>

              <!-- 用户头像菜单 -->
              <div class="avatar-menu" @click.stop="select">
                <div class="avatar-text" v-if="authStore.userName">
                  {{ getInitials(authStore.userName) }}
                </div>
                <div class="avatar-text" v-else>?</div>
                <div
                  class="avatar-dropdown"
                  v-show="isSelected == true"
                  ref="selectRef"
                >
                  <div class="dropdown-content">
                    <router-link
                      class="dropdown-item"
                      to="/profile"
                      @click="jumpFirst"
                    >
                      <el-icon size="20"> <User /> </el-icon>
                      <span>个人资料</span>
                    </router-link>
                    <router-link
                      to="/profile/collections"
                      class="dropdown-item"
                    >
                      <el-icon size="20"> <CollectionTag /> </el-icon>
                      <span>我的收藏</span>
                    </router-link>
                    <router-link to="/findFriends" class="dropdown-item">
                      <el-icon size="20"> <UserFilled /> </el-icon>
                      <span>查找好友</span>
                    </router-link>
                    <div class="dropdown-item" @click="logout">
                      <el-icon size="20"> <SwitchButton /> </el-icon>
                      <span>退出登录</span>
                    </div>
                  </div>
                </div>
              </div>
            </template>

            <!-- 未登录状态 -->
            <template v-else>
              <div class="auth-buttons">
                <div class="login-button" @click="writeBack">
                  <el-icon><User /></el-icon>
                  <span>登录</span>
                </div>
                <div class="signup-button" @click="signUpVisible = true">
                  <el-icon><EditPen /></el-icon>
                  <span>注册</span>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>

      <div class="nav-divider"></div>
    </div>

    <!-- Upload Dialog -->
    <el-dialog v-model="uploadVisible" title="上传待识别的图片" width="640">
      <div class="upload-body">
        <div class="upload-area" @click="$refs.fileInput.click()">
          <el-icon size="40" color="#1890ff"><Upload /></el-icon>
          <p class="upload-text">点击选择图片文件</p>
          <p class="upload-hint">支持 JPG、PNG、JPEG 格式</p>
        </div>
        <input 
          ref="fileInput" 
          type="file" 
          accept="image/*" 
          @change="onFileChange" 
          style="display: none;"
        />
        <div class="preview" v-if="previewUrl">
          <img :src="previewUrl" alt="preview" />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="uploadVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUpload">上传</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- Background Image Description -->
    <div
      class="background-description"
      v-show="showBackgroundDescription && top5Businesses[imageIndex]"
    >
      <div class="desc-text">{{ top5Businesses[imageIndex]?.name }}</div>
      <div class="desc-subtext">
        {{ top5Businesses[imageIndex]?.categories }}
      </div>
      <div class="desc-actions">
        <el-button
          class="detail-btn"
          type="primary"
          @click="goToBusinessDetails"
        >
          <el-icon><Search /></el-icon>
          查看详情
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 添加 showBackgroundDescription 属性定义
const props = defineProps({
  showBackgroundDescription: {
    type: Boolean,
    default: true,
  },
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

// ****************申请成为商家
// 定义一周的天数
const weekDays = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

// 营业时间数据结构
const businessHours = ref({
  Monday: { start: "", end: "" },
  Tuesday: { start: "", end: "" },
  Wednesday: { start: "", end: "" },
  Thursday: { start: "", end: "" },
  Friday: { start: "", end: "" },
  Saturday: { start: "", end: "" },
  Sunday: { start: "", end: "" },
});

// 在提交前将营业时间转换为 JSON 字符串
function formatBusinessHours() {
  const hoursObj = {};
  for (const day of weekDays) {
    const timeSlot = businessHours.value[day];
    if (timeSlot.start && timeSlot.end) {
      hoursObj[day] = `${timeSlot.start}-${timeSlot.end}`;
    }
  }
  return JSON.stringify(hoursObj);
}
// 属性相关数据
const selectedAttributes = ref<string[]>([]);
const wifiOption = ref("u'free'");
const alcoholOption = ref("'beer_and_wine'");

// 格式化 attributes 为 JSON 字符串的方法
function formatAttributes() {
  const attrs: Record<string, string> = {};

  // 处理 WiFi 选项
  if (selectedAttributes.value.includes("WiFi")) {
    attrs["WiFi"] = wifiOption.value;
  } else {
    attrs["WiFi"] = "u'no'";
  }

  // 处理酒精饮品选项
  if (selectedAttributes.value.includes("Alcohol")) {
    attrs["Alcohol"] = alcoholOption.value;
  } else {
    attrs["Alcohol"] = "'none'";
  }

  // 处理其他布尔型属性
  const booleanAttrs = [
    "OutdoorSeating",
    "RestaurantsTakeOut",
    "RestaurantsDelivery",
    "BusinessAcceptsCreditCards",
    "DogsAllowed",
    "GoodForKids",
  ];

  booleanAttrs.forEach((attr) => {
    if (selectedAttributes.value.includes(attr)) {
      attrs[attr] = "True";
    } else {
      attrs[attr] = "False";
    }
  });

  return JSON.stringify(attrs);
}

let {
  merchantVisible,
  merchantForm,
  submitMerchan: originalSubmitMerchan,
} = toRefs(useMerchant());

function handleMerchantSubmit() {
  // 格式化营业时间
  merchantForm.value.hours = formatBusinessHours();

  // 格式化商户属性
  merchantForm.value.attributes = formatAttributes();

  // 调用原始的提交函数
  originalSubmitMerchan.value();
}

// ******************导入购物车图标
import { ShoppingCart, Upload } from "@element-plus/icons-vue";

// build full image URL safely; accepts remote URLs or stored filenames
const filePath = (file: any) => {
  if (file == null) {
    console.log("null");
    return "";
  }
  // 如果图片链接已经是完整的URL，直接使用；否则拼接静态服务器地址
  const fullPath = file.includes("http")
    ? file
    : `http://localhost:3000/images/${file}.jpg`;
  return fullPath;
};

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

let { info, search, result, getResult, getTop5BusinessWith5Stars } = toRefs(
  useSearch()
);

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

function goToBusinessDetails() {
  const business = top5Businesses.value[imageIndex.value];
  if (business && business.businessId) {
    router.push({
      path: "/merchantDetail",
      query: { id: business.businessId },
    });
  }
}

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

// 初始化top5商家数据
const top5Businesses = ref([]);

let backgroundImage = ref([
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/4.jpg",
    key: 1,
    description: "探索城市中的美味佳肴",
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/3.jpg",
    key: 2,
    description: "感受都市夜晚的璀璨灯火",
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/1.jpg",
    key: 3,
    description: "拥抱大自然的宁静美景",
  },
  {
    url: "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/2.jpg",
    key: 4,
    description: "体验现代城市的繁华生活",
  },
]);
let imageIndex = ref(0);
let imageUrl = ref(backgroundImage.value[imageIndex.value].url);
// 修改 view 的初始化逻辑

let view = ref({
  "background-image": `url(${imageUrl.value})`,
  height: props.compactMode ? "auto" : "800px",
  "background-repeat": "no-repeat",
  "background-size": "cover",
  "background-position": "30% 25%",
});

async function changeImage() {
  // prefer cycling through top5Businesses if available, otherwise fallback images
  const total =
    top5Businesses.value && top5Businesses.value.length > 0
      ? top5Businesses.value.length
      : backgroundImage.value.length;
  imageIndex.value = (imageIndex.value + 1) % total;

  // choose URL: business image if present, else fallback array url
  let chosenUrl = "";
  if (top5Businesses.value && top5Businesses.value.length > 0) {
    const b = top5Businesses.value[imageIndex.value];
    chosenUrl = filePath(b?.image || b?.url || "");
  } else {
    chosenUrl = backgroundImage.value[imageIndex.value].url;
  }

  imageUrl.value = chosenUrl;
  view.value = {
    "background-image": `url(${imageUrl.value})`,
    height: 800 + "px",
    "background-repeat": "no-repeat",
    "background-size": "cover",
    "background-position": "30% 25%",
  };
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

  // 初始加载top5商家数据并将背景指向第一条（如果存在）
  const businesses = await getTop5BusinessWith5Stars.value();
  if (businesses && businesses.length > 0) {
    top5Businesses.value = businesses;
    const first = top5Businesses.value[0];
    const url = filePath(first?.image || first?.url || "");
    if (url) {
      imageUrl.value = url;
      view.value["background-image"] = `url(${imageUrl.value})`;
    }
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
    console.log("top10:" + resp);
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
import { ref as vueRef } from "vue";

const uploadVisible = vueRef(false);
const uploadFile = vueRef<File | null>(null);
const previewUrl = vueRef("");

function getInitials(name: string) {
  if (!name) return "?";
  const firstChar = name.trim().charAt(0).toUpperCase();
  return firstChar || "?";
}

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
    previewUrl.value = "";
  }
}

async function submitUpload() {
  if (!uploadFile.value) {
    ElMessage({ message: "请选择图片后再上传", type: "warning" });
    return;
  }
  const formData = new FormData();
  formData.append("file", uploadFile.value);
  try {
    const resp = await axios.post("http://localhost:3000/photos", formData);

    ElMessage({ message: "上传成功", type: "success" });
   
    uploadVisible.value = false;
    // 清理
    uploadFile.value = null;
    previewUrl.value = "";
    
    // 修复语法错误：确保路由跳转和搜索执行都在try块中正确执行
    router.push({
      name: "Search",
      query: { info: info.value },
    });
    getResult.value(1, info.value);
  } catch (err) {
    console.error(err);
    ElMessage({ message: "上传失败", type: "error" });
  }
}

// **************************高德
// 添加一个方法来根据地址获取经纬度
async function getAddressCoordinates(address: string) {
  try {
    // 使用高德地图API（需要申请Key）
    const response = await axios.get(
      "/gpi/v3/geocode/geo",
      {
        params: {
          key: "96b27b572a0e16d551ac65dd0e02f65f", // 需要申请的API Key
          address: address,
          city: "城市名", // 可选，指定城市
        },
      }
    );

    if (response.data.status === "1" && response.data.geocodes.length > 0) {
      const geocode = response.data.geocodes[0];
      return {
        latitude: geocode.location.split(",")[1],
        longitude: geocode.location.split(",")[0],
      };
    }

    return null;
  } catch (error) {
    console.error("获取坐标失败:", error);
    return null;
  }
}

async function autoFillCoordinates() {
  if (!merchantForm.value.address || merchantForm.value.address.length < 5) {
    return;
  }
  
  // 短暂延迟，避免频繁请求
  setTimeout(async () => {
    const coordinates = await getAddressCoordinates(merchantForm.value.address);
    if (coordinates) {
      merchantForm.value.latitude = parseFloat(coordinates.latitude);
      merchantForm.value.longitude = parseFloat(coordinates.longitude);
    }
  }, 500);
}

</script>

<style scoped>
/* 添加以下样式 */
/* 在现有样式中添加以下内容 */
.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 90px !important;
  font-weight: 500;
  color: #666;
}

.separator {
  margin: 0 10px;
  color: #999;
}
.attributes-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.attribute-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.main-checkbox {
  margin-right: 15px !important;
}

.attribute-select {
  width: 160px;
  margin-left: 8px;
}

.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.el-form-item:last-child {
  margin-bottom: 0;
}
.merchant-dialog :deep(.el-dialog__body) {
  padding: 20px 30px;
}

/* 调整表单项间距和整体布局 */
.merchant-dialog :deep(.el-form-item) {
  margin-bottom: 20px;
}

.merchant-dialog :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
}

.attribute-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.main-checkbox {
  margin-right: 15px !important;
}

.attribute-select {
  width: 160px;
}

.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 80px;
  font-weight: 500;
  color: #666;
}

.separator {
  margin: 0 10px;
  color: #999;
}

/* 强制设置时间选择器宽度 */
.el-time-picker {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}

:deep(.el-time-picker) {
  width: 80px !important;
  min-width: 80px !important;
  max-width: 80px !important;
}
.attributes-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 10px;
}

.checkbox-with-select {
  margin-left: 20px;
  margin-bottom: 10px;
}

.checkbox-with-select .el-select {
  width: 150px;
  margin-top: 5px;
}

.hours-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.day-label {
  width: 80px;
  font-weight: 500;
  color: #666;
}
.separator {
  margin: 0 10px;
  color: #999;
}

/* 导航栏整体样式 */
.modern-nav {
  background: transparent; /* 将背景色设置为透明 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  padding: 0 40px;
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 关键：均匀分布元素 */
  height: 70px;
  gap: 30px;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  gap: 10px;
  flex-shrink: 0; /* 防止logo被压缩 */
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: #1890ff;
  letter-spacing: -0.5px;
}

/* 搜索区域 */
.search-area {
  flex: 1;
  max-width: 600px;
  margin: 0 auto;
}

.input-box {
  display: flex;
  align-items: center;
  height: 48px;
}

.input-wrapper {
  position: relative;
  flex: 1;
}

.search-input {
  height: 48px;
}
.search-area {
  flex: 1;
  max-width: 600px;
  margin: 0 auto; /* 居中显示 */
}

.search-input :deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.15);
  border-radius: 24px 0 0 24px;
  border: 1px solid #e8e8e8;
  background-color: #ffffff;
  height: 44px;
}

.search-input :deep(.el-input__inner) {
  height: 46px;
  border: none;
  background: transparent;
}

.hot-search-card {
  position: absolute;
  top: 50px;
  left: 0;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  z-index: 100;
}

.card-header {
  font-weight: 600;
  color: #1890ff;
}

.text.item {
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.text.item:hover {
  background-color: #f0f8ff;
}

/* 操作按钮 */
.action-button {
  height: 48px;
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  outline: none;
}
.camera-button {
  background-color: #40a9ff;
  box-shadow: 0 2px 8px rgba(64, 169, 255, 0.2);
}

.camera-button:hover {
  background-color: #1890ff;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.search-button {
  background-color: #1890ff;
  border-radius: 0 24px 24px 0;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
}

.search-button:hover {
  background-color: #40a9ff;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

/* 确保按钮容器正确对齐 */
.input-box {
  display: flex;
  align-items: center;
  height: 48px;
  width: 100%;
}

/* 用户操作区域 */
.user-actions {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-shrink: 0; /* 防止被压缩 */
}

.admin-link,
.boss-link {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #1890ff;
  text-decoration: none;
  font-weight: 500;
  padding: 8px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.admin-link:hover,
.boss-link:hover {
  background-color: #e6f7ff;
}

.apply-business {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #1890ff;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #e6f7ff;
  border: 1px solid #bae7ff;
}

.apply-business:hover {
  background-color: #bae7ff;
  transform: translateY(-2px);
}

/* 通知区域 */
.notifications-wrapper {
  position: relative;
}

.notifications {
  padding: 8px;
  cursor: pointer;
  background-color: #e6f7ff;
  border-radius: 50%;
  width: 33px; /* 从44px减小到40px */
  height: 33px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.2);
}

.notifications:hover {
  background-color: #bae7ff;
  transform: translateY(-2px);
}

/* 头像菜单 */
.avatar-menu {
  position: relative;
  cursor: pointer;
}

.avatar-text {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
  background: linear-gradient(135deg, #1890ff, #40a9ff);
  border: 2px solid #ffffff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.3);
  transition: all 0.3s ease;
}

.avatar-text:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.4);
}

.avatar-dropdown {
  position: absolute;
  top: 50px;
  right: 0;
  width: 200px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-content {
  padding: 8px 0;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: #262626;
  text-decoration: none;
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background-color: #f0f8ff;
}

/* 登录注册按钮 */
.auth-buttons {
  display: flex;
  gap: 12px;
}

.login-button,
.signup-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login-button {
  color: #1890ff;
  border: 1px solid #1890ff;
  background-color: transparent;
}

.login-button:hover {
  background-color: #1890ff;
  color: white;
}

.signup-button {
  color: white;
  background-color: #1890ff;
  border: 1px solid #1890ff;
}

.signup-button:hover {
  background-color: #40a9ff;
  transform: translateY(-2px);
}

/* 分割线 */
.nav-divider {
  height: 1px;
  background: linear-gradient(to right, transparent, #e8e8e8, transparent);
  margin: 0 40px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .nav-content {
    gap: 15px;
  }

  .search-area {
    max-width: 400px;
  }
}

@media (max-width: 992px) {
  .nav-container {
    padding: 0 20px;
  }

  .logo-text {
    font-size: 20px;
  }

  .search-area {
    max-width: 300px;
  }

  .admin-link span,
  .boss-link span,
  .apply-business span {
    display: none;
  }

  .admin-link,
  .boss-link,
  .apply-business {
    padding: 8px;
  }
}

@media (max-width: 768px) {
  .nav-content {
    height: 60px;
  }

  .search-area {
    display: none;
  }

  .user-actions {
    gap: 10px;
  }

  .nav-divider {
    margin: 0 20px;
  }
}

.view {
  width: 100vw;
  min-height: 100vh; /* 保持这个设置 */
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf5 100%);
  padding-bottom: 40px;
  height: auto !important; /* 覆盖js中设置的固定高度 */
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
  background-color: rgba(255, 255, 255, 0.12);
  border-color: rgba(255, 255, 255, 0.18);
  color: #fff;
}

.background-description .detail-btn :deep(.el-icon) {
  margin-right: 8px;
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

/* 好友通知 */
.notifications-container {
  position: absolute;
  width: 320px; /* 从380px减小到320px */
  background-color: #ffffff;
  left: -270px; /* 调整位置 */
  top: 50px;
  border-radius: 12px;
  overflow: hidden;
  cursor: auto;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
  border: 1px solid #e8e8e8;
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.notifications-header {
  padding: 16px 20px;
  background: linear-gradient(135deg, #1890ff, #40a9ff);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notifications-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.notification-count {
  background-color: #ff4d4f;
  border-radius: 10px;
  padding: 2px 8px;
  font-size: 12px;
}

.notifications-list {
  max-height: 300px; /* 减小高度 */
  overflow-y: auto;
}

.notification-box {
  display: flex;
  padding: 12px 16px; /* 减小内边距 */
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}
.notification-box:hover {
  background-color: #f0f8ff;
}

.notification-avatar {
  margin-right: 12px;
}

.avatar-placeholder {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1890ff, #40a9ff);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 16px;
}

.notification-content {
  flex: 1;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.user-name {
  font-weight: 600;
  color: #262626;
}

.notification-time {
  font-size: 12px;
  color: #8c8c8c;
}

.notification-message {
  font-size: 13px; /* 减小字体 */
  color: #595959;
  margin-bottom: 10px;
  line-height: 1.4;
}

.notification-actions {
  display: flex;
  gap: 8px;
}

.no-notifications {
  text-align: center;
  padding: 40px 20px;
  color: #bfbfbf;
}

.no-notifications .el-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.badge-item :deep(.el-badge__content) {
  background-color: #ff4d4f;
  border: none;
}

.mail-line {
  display: flex;
  justify-content: space-between;
}

.find {
  cursor: pointer;
  color: #6e7072;
  font-size: 13px;
}

/* 美化表单样式 */
:deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

:deep(.el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

:deep(.el-dialog__title) {
  color: #333;
  font-weight: 600;
  font-size: 18px;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #999;
}

:deep(.el-dialog__body) {
  padding: 30px 40px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #262626;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(24, 144, 255, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

:deep(.el-button) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.pwd {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.find {
  color: #1890ff;
  font-weight: 500;
  transition: all 0.3s ease;
}

.find:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
}

.dialog-footer .el-button {
  padding: 10px 24px;
  font-weight: 500;
}

:deep(.el-button--primary) {
  background: #1890ff;
  border-color: #1890ff;
}

:deep(.el-button--primary:hover) {
  background: #40a9ff;
  border-color: #40a9ff;
}

:deep(.el-button--primary:active) {
  background: #096dd9;
  border-color: #096dd9;
}

/* 特殊表单项样式 */
.mail-line {
  display: flex;
  align-items: flex-start;
  gap: 15px;
}

.mail-line .el-form-item {
  flex: 1;
}

/* 单选按钮组样式 */
:deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}

:deep(.el-radio) {
  margin-right: 0;
}

:deep(.el-radio__inner) {
  border-color: #d9d9d9;
}

:deep(.el-radio__inner::after) {
  background-color: #1890ff;
}

:deep(.el-radio__label) {
  color: #595959;
}

/* 复选框样式 */
:deep(.el-checkbox__inner) {
  border-radius: 4px;
}

:deep(.el-checkbox__inner::after) {
  border-color: #1890ff;
}

/* 上传弹窗样式 */
.upload-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 30px;
  width: 100%;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.upload-area:hover {
  border-color: #1890ff;
  background-color: #f0f8ff;
}

.upload-text {
  font-size: 16px;
  font-weight: 500;
  color: #262626;
  margin: 0;
}

.upload-hint {
  font-size: 14px;
  color: #8c8c8c;
  margin: 0;
}

.upload-body .preview {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 100%;
}

.upload-body .preview img {
  width: 100%;
  height: auto;
  display: block;
}

.upload-body .preview:empty {
  display: none;
}
</style>