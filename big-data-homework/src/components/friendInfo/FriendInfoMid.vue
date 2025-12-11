<template>
  <div class="mid">
    <div class="display">
      <div class="nav-box">
        <div class="nav-top">
          <div class="content-box">
            <!-- 将头像替换为首字母 -->
            <div class="avatar-placeholder">
              <div class="avatar-initials">{{ getInitials(friendInfo.userVO.name) }}</div>
            </div>
            <h2 class="name">{{ friendInfo.userVO.name }}</h2>
            <p class="text">{{ friendInfo.userVO.city }}</p>
            <div class="item-box">
              <!-- <div class="message-box">
                <div class="icon-box">
                  <el-icon size="24"><Message /></el-icon>
                </div>
                <div style="color: #6E7072">发送消息</div>
              </div> -->
            </div>
          </div>
        </div>
        <div class="nav-bottom">
          <router-link :to="{path:'/friendInfo/overview',query:{userId : userId}}" replace class="nav-bottom-content" :class="{'isActive':showButton == 0}">
            <el-icon size="24">
              <User/>
            </el-icon>
            <div class="nav-bottom-content-style">个人资料概览</div>
          </router-link>
          <router-link :to="{path:'/friendInfo/reviews',query:{userId : userId}}" replace class="nav-bottom-content" :class="{'isActive':showButton == 1}">
            <el-icon size="24">
              <PriceTag/>
            </el-icon>
            <div class="nav-bottom-content-style">评价</div>
          </router-link>
          <router-link :to="{path:'/friendInfo/collections',query:{userId : userId}}" replace class="nav-bottom-content" :class="{'isActive':showButton == 2}">
            <el-icon size="24">
              <CollectionTag/>
            </el-icon>
            <div class="nav-bottom-content-style">收藏</div>
          </router-link>
          <router-link :to="{path:'/friendInfo/friends',query:{userId : userId}}" replace class="nav-bottom-content" :class="{'isActive':showButton == 3}">
            <el-icon size="24">
              <Coordinate/>
            </el-icon>
            <div class="nav-bottom-content-style">好友</div>
          </router-link>
        </div>
      </div>
      <div class="info">
        <router-view :userId="userId"></router-view>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref} from "vue";
import {Coordinate, User, PriceTag, CollectionTag, Message} from "@element-plus/icons-vue";
import {UseButtonStore} from "@/stores/UseButtonStore"
import UseFriendInfo from "@/hooks/UseFriendInfo";

// 获取用户id
const props = defineProps(['userId'])
const userId = computed(() => props.userId)

// 使用朋友信息hook
const {friendInfo,getFriendInfo} = UseFriendInfo()

// 获取姓名首字母
const getInitials = (name: string) => {
  if (!name) return '?';
  return name.trim().charAt(0).toUpperCase();
}

// 按钮
const buttonStore = UseButtonStore()
const showButton = computed(()=>buttonStore.button)

onMounted(()=>{
  if (userId.value) {
    getFriendInfo(userId.value)
  }
})
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
}

.nav-box {
  display: flex;
  flex-direction: column;
  width: 280px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.nav-top {
  display: flex;
  flex-direction: column;
  padding: 30px 24px;
  background: linear-gradient(135deg, #6EE7B7, #3B82F6);
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
  font-size: 28px;
  color: #2D2E2F;
  margin: 20px 0 5px 0;
  text-align: center;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.text {
  font-size: 16px;
  color: #6E7072;
  margin: 0px;
  margin-top: 5px;
  color: rgba(255, 255, 255, 0.9);
}

.item-box {
  display: flex;
  justify-content: center;
  height: 72px;
  width: 100%;
  margin-top: 20px;
  font-size: 14px;
}

.message-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.icon-box {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 44px;
  height: 44px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.icon-box:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.nav-bottom {
  display: flex;
  flex-direction: column;
  background: white;
  padding: 16px 0px 8px 0px;
}

.nav-bottom-content {
  display: flex;
  align-items: center;
  height: 50px;
  cursor: pointer;
  color: #2D2E2F;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  margin-bottom: 5px;
  padding: 0 20px;
  transition: all 0.3s ease;
  text-decoration: none;
}

.nav-bottom-content:hover {
  background-color: rgba(235, 235, 235, 1);
}

.nav-bottom-content.isActive {
  background-color: rgba(235, 235, 235, 1);
  border-left: 4px solid #3B82F6;
}

.nav-bottom-content-style {
  margin-left: 15px;
  font-size: 15px;
  font-weight: 500;
}

.info {
  display: flex;
  flex-direction: column;
  width: calc(100% - 320px);
  margin-left: 30px;
  overflow: auto;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  background: white;
  min-height: 600px;
}

@media (max-width: 992px) {
  .display {
    flex-direction: column;
    align-items: center;
  }
  
  .nav-box {
    width: 100%;
    max-width: 500px;
    margin-bottom: 30px;
  }
  
  .info {
    width: 100%;
    margin-left: 0;
    max-width: 800px;
  }
}

@media (max-width: 576px) {
  .mid {
    padding: 10px;
    margin-top: 15px;
  }
  
  .nav-top {
    padding: 20px 15px;
  }
  
  .avatar-placeholder {
    width: 100px;
    height: 100px;
  }
  
  .avatar-initials {
    font-size: 40px;
  }
  
  .name {
    font-size: 24px;
  }
}
</style>