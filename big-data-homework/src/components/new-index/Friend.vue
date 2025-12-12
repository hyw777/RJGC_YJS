<template>
  <!-- 评论部分的头像修改 -->
  <div
    class="content-card"
    v-for="(review, index) in friendData.reviewVOS"
    :key="index"
  >
    <div class="profile">
      <div class="pro-box">
        <div class="avatar">
          <el-avatar :size="40" :style="getAvatarStyle(review.userName)">
            {{ getInitials(review.userName) }}
          </el-avatar>
        </div>
        <div class="detail">
          <div class="username">{{ review.userName }}</div>
          <div class="time">{{ review.date }}</div>
        </div>
      </div>
    </div>
    <div class="line-box">
      <div class="line"></div>
    </div>
    <div class="show">
      <div class="title">
        <router-link
          :to="{ path: '/merchantDetail', query: { id: review.businessId } }"
          class="business-link"
        >
          {{ console.log(review) }}
          {{ review.businessName }}
        </router-link>
      </div>
      <div class="rate">
        <el-rate v-model="review.stars" :colors="colors" disabled />
      </div>
      <div
        class="text"
        :class="{ expanded: isExpanded[index] }"
        :ref="(el) => (textContentRefs[index] = el)"
      >
        {{ review.text }}
      </div>
      <div
        @click="expand(index)"
        v-if="showExpandButton[index]"
        class="show-more"
      >
        {{ isExpanded[index] ? "Show less" : "Read more" }}
      </div>
    </div>
  </div>
  <div
    class="content-card"
    v-for="(collection, index) in friendData.collectionVOS"
    :key="index"
  >
    <div class="profile">
      <div class="pro-box">
        <div class="avatar">
          <el-avatar :size="40" :style="getAvatarStyle(collection.userName)">
            {{ getInitials(collection.userName) }}
          </el-avatar>
        </div>
        <div class="detail">
          <div class="username">{{ collection.userName }}</div>
          <div class="time">{{ collection.date }}</div>
        </div>
      </div>
    </div>
    <div class="show-box">
      <router-link
        class="img"
        :to="{
          path: '/merchantDetail',
          query: { id: collection.businessVO1.businessId },
        }"
      >
        <img
          v-if="collection.businessVO1.image"
          :src="filePath(collection.businessVO1.image)"
          :alt="collection.businessVO1.name"
        />
        <div
          v-else
          class="default-avatar"
          :style="getBusinessAvatarStyle(collection.businessVO1.name)"
        >
          {{ getInitials(collection.businessVO1.name) }}
        </div>
      </router-link>
      <div class="info-box">
        <div class="info1">
          <span>{{ collection.businessVO1.name }}</span>
        </div>
        <div class="info2">
          <el-rate
            v-model="collection.businessVO1.stars"
            size="large"
            disabled
          ></el-rate>
          <div class="rate-box">
            <div style="margin-right: 4%">
              {{ collection.businessVO1.stars }}
            </div>
            <span>({{ collection.businessVO1.reviewCount }} reviews)</span>
          </div>
        </div>
        <div class="info3">
          <span>{{ collection.businessVO1.categories }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, toRefs, ref, computed, nextTick } from "vue";
import { UseButtonStore } from "@/stores/UseButtonStore";
import { useFriendActivity } from "@/hooks/UseFriendActivity";

let defaultAvatar =
  "https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/OIP-C.jpg";
let buttonStore = UseButtonStore();
let { friendData, getFriendData, formatDateTime } = toRefs(useFriendActivity());

const textContentRefs = ref([]);
const isExpanded = ref([]);
const showExpandButton = ref([]);

function expand(index) {
  isExpanded.value[index] = !isExpanded.value[index];
}

const filePath = (file) => {
  if (file == null) {
    return ``;
  }
  return file.includes("http")
    ? file
    : `http://localhost:3000/images/${file}.jpg`;
};

const colors = ref(["#99A9BF", "#F7BA2A", "#FF9900"]); // same as { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }

onMounted(async () => {
  buttonStore.setIndexButton(1);
  getFriendData.value();

  if (friendData.value.reviewVOS.length > 3) {
    friendData.value.reviewVOS = friendData.value.reviewVOS.slice(0, 3);
  }
  if (friendData.value.collectionVOS.length > 3) {
    friendData.value.collectionVOS = friendData.value.collectionVOS.slice(0, 3);
  }
  friendData.value.reviewVOS.forEach((review) => {
    review.date = formatDateTime.value(review.date);
  });
  friendData.value.collectionVOS.forEach((collection) => {
    collection.date = formatDateTime.value(collection.date);
  });

  // 初始化 isExpanded 和 showExpandButton
  friendData.value.reviewVOS.forEach((review, index) => {
    isExpanded.value[index] = false;
    showExpandButton.value[index] = false;
  });

  textContentRefs.value.forEach((content, index) => {
    if (content.scrollHeight > content.clientHeight) {
      showExpandButton.value[index] = true;
    } else {
      showExpandButton.value[index] = false;
    }
  });
});

// 添加获取用户名首字母的方法
const getInitials = (name: string) => {
  if (!name) return "U";
  return name.charAt(0).toUpperCase();
};

// 添加生成头像样式的函数
const getAvatarStyle = (name: string) => {
  if (!name) return { backgroundColor: "#409EFF" };

  // 根据用户名生成颜色
  const colors = [
    "#409EFF",
    "#67C23A",
    "#E6A23C",
    "#F56C6C",
    "#909399",
    "#00C1D4",
  ];
  const charCode = name.charCodeAt(0);
  const colorIndex = charCode % colors.length;

  return {
    backgroundColor: colors[colorIndex],
    color: "white",
    fontWeight: "bold",
  };
};

// 添加获取商户名称首字母的方法
const getBusinessInitials = (name: string) => {
  if (!name) return "B"; // 默认字母
  return name.charAt(0).toUpperCase();
};

// 添加生成商户头像样式的方法
const getBusinessAvatarStyle = (name: string) => {
  if (!name) return { backgroundColor: "#409EFF" };

  // 根据商户名称生成颜色
  const colors = [
    "#409EFF",
    "#67C23A",
    "#E6A23C",
    "#F56C6C",
    "#909399",
    "#00C1D4",
  ];
  const charCode = name.charCodeAt(0);
  const colorIndex = charCode % colors.length;

  return {
    backgroundColor: colors[colorIndex],
    color: "white",
    fontWeight: "bold",
    width: "100%",
    height: "100%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "24px",
    borderRadius: "8px",
  };
};

</script>

<style scoped>
.default-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  border-radius: 8px;
  color: white;
  font-weight: bold;
}

/* 卡片容器美化 */
.content-card {
  display: flex;
  flex-direction: column;
  width: 30%;
  margin-bottom: 24px;
  margin-right: 3%;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: #ffffff;
  overflow: hidden;
}

.content-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* 用户信息区域 */
.profile {
  display: flex;
  border: none;
  padding: 16px;
}

.pro-box {
  display: flex;
  align-items: center;
}

.avatar {
  cursor: pointer;
}

.detail {
  margin-left: 12px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.username {
  font-size: 15px;
  color: #1a1a1a;
  font-weight: 600;
  line-height: 1.4;
}

.time {
  font-size: 13px;
  color: #8c8c8c;
  font-weight: 400;
  line-height: 1.4;
}

/* 分隔线 */
.line-box {
  padding: 0 16px;
}

.line {
  height: 1px;
  background-color: #f0f0f0;
}

/* 内容展示区域 */
.show {
  display: flex;
  flex-direction: column;
  padding: 16px;
  border: none;
}

.title a {
  color: #1a1a1a;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 10px;
  text-decoration: none;
}

.title a:hover {
  color: #1890ff;
}

.rate {
  margin: 10px 0;
}

.text {
  width: 100%;
  max-height: 40px;
  color: #262626;
  font-size: 14px;
  overflow: hidden;
  line-height: 1.5;
  position: relative;
  transition: max-height 0.3s ease;
}

.expanded {
  max-height: 500px;
}

.show-more {
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
  margin-top: 8px;
  display: inline-block;
  transition: color 0.2s ease;
}

.show-more:hover {
  color: #40a9ff;
  text-decoration: underline;
}

/* 底部操作栏 */
.down {
  display: flex;
  padding: 0 16px;
  border: none;
  height: auto;
  min-height: 55px;
}

.down-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 12px 0;
  border-top: 1px solid #f0f0f0;
  font-size: 14px;
  color: #595959;
}

.icon-box {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: color 0.2s ease;
  padding: 6px 8px;
  border-radius: 4px;
}

.icon-box:hover {
  background-color: #f5f5f5;
  color: #1890ff;
}

.icon {
  font-size: 20px;
  margin-right: 6px;
  transition: transform 0.3s ease;
}

.icon-box:hover .icon {
  transform: scale(1.1);
}

/* 收藏展示区域 */
.show-box {
  display: flex;
  flex-direction: row;
  padding: 16px;
  border: none;
  gap: 16px;
}

.img {
  width: 80px;
  height: 80px;
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.img:hover img {
  transform: scale(1.05);
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-grow: 1;
  min-width: 0;
}

.info1 {
  color: #1a1a1a;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.info2 {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.info2 span {
  width: 90px;
}

.rate-box {
  display: flex;
  align-items: center;
  margin-left: 12px;
  color: #8c8c8c;
  font-size: 13px;
}

.info3 {
  margin-top: 4px;
}

.info3 span {
  color: #595959;
  font-size: 12px;
  padding: 3px 8px;
  background-color: #f5f5f5;
  border-radius: 12px;
  margin-right: 6px;
}
</style>
