<template>
  <div class="mid">
    <div class="display">
      <div class="nav-box">
        <div class="nav-top">
          <div class="content-box">
            <el-avatar class="el-avatar" :size="120" :src="avatar"/>
            <h2 class="name">{{ userInfo.name }}</h2>
            <p class="text">{{ userInfo.city }}</p>
            <div class="item-box">
              <div class="edit-box">
                <div class="icon-box" @click="editProfile">
                  <el-icon size="24">
                    <Edit/>
                  </el-icon>
                </div>
                <div>Edit Profile</div>
              </div>
              <div class="add-box">
                <router-link to="/findFriends" class="icon-box">
                  <el-icon color="#000000" size="24">
                    <Plus/>
                  </el-icon>
                </router-link>
                <div>Add Friends</div>
              </div>
            </div>
          </div>
        </div>
        <div class="nav-bottom">
          <router-link to="/profile/overview" replace class="nav-bottom-content" :class="{'isActive':showButton == 0}">
            <el-icon size="24">
              <User/>
            </el-icon>
            <div class="nav-bottom-content-style">Profile overview</div>
          </router-link>
          <router-link to="/profile/reviews" replace class="nav-bottom-content" :class="{'isActive':showButton == 1}">
            <el-icon size="24">
              <PriceTag/>
            </el-icon>
            <div class="nav-bottom-content-style">Reviews</div>
          </router-link>
          <router-link to="/profile/collections" replace class="nav-bottom-content" :class="{'isActive':showButton == 2}">
            <el-icon size="24">
              <CollectionTag/>
            </el-icon>
            <div class="nav-bottom-content-style">Collections</div>
          </router-link>
          <router-link to="/profile/friends" replace class="nav-bottom-content" :class="{'isActive':showButton == 3}">
            <el-icon size="24">
              <Coordinate/>
            </el-icon>
            <div class="nav-bottom-content-style">Friends</div>
          </router-link>
        </div>
      </div>
      <div class="info">
        <router-view  :isEdit="isEdit"></router-view>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, onUnmounted, ref, toRefs} from "vue";
import {Coordinate, User} from "@element-plus/icons-vue";
import UseUserInfo from "@/hooks/UseUserInfo";
import UseUserEdit from "@/hooks/UseUserEdit";
import {UseButtonStore} from "@/stores/UseButtonStore"
import {router} from "@/router";

// 使用编辑hook
let userEdit = UseUserEdit()
let {form, editSwitch, isEdit, submit,editProfile} = userEdit

// 使用用户信息hook
let {userInfo,getUserInfo} = toRefs(UseUserInfo());

// 用户默认头像
let avatar = ref('https://hmleadnews-lgk.oss-cn-beijing.aliyuncs.com/OIP-C.jpg')

// 按钮
let buttonStore = UseButtonStore()
let showButton = computed(()=>buttonStore.button)
function switchButton(num:number){
  buttonStore.setButton(num)
}

onMounted(()=>{
  getUserInfo.value()
})


</script>

<style scoped>
.mid {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
}

.display {
  display: flex;
  justify-content: space-between;
  width: 71%;
}

.nav-box {
  display: flex;
  flex-direction: column;
  width: 23%;
}

.nav-top {
  display: flex;
  flex-direction: column;
  padding: 24px;
  border: 1px solid rgba(235, 235, 235, 1);
}

.content-box {
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  align-items: center;
}

.name {
  font-size: 28px;
  color: #2D2E2F;
  margin: 0px;
  margin-top: 10px;
}

.text {
  font-size: 16px;
  color: #6E7072;
  margin: 0px;
  margin-top: 5px;
}

.item-box {
  display: flex;
  justify-content: space-around;
  height: 72px;
  width: 100%;
  margin-top: 20px;
  font-size: 14px;
}

.edit-box {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

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
  width: 44px;
  height: 44px;
  background-color: rgba(235, 235, 235, 1);
  border-radius: 25px;
  cursor: pointer;
}

.nav-bottom {
  display: flex;
  flex-direction: column;
  height: 319px;
  padding: 16px 0px 8px 0px;
}

.nav-bottom-content {
  display: flex;
  align-items: center;
  height: 50px;
  cursor: pointer;
  color: #2D2E2F;
  border-bottom: 1px solid rgba(235, 235, 235, 1);
  margin-bottom: 20px;
}

.nav-bottom-content:hover {
  background-color: rgba(235, 235, 235, 1);
}

.isActive {
  background-color: rgba(235, 235, 235, 1);
}

.nav-bottom-content-style {
  margin-left: 15px;
  font-size: 14px;
}

.info {
  display: flex;
  flex-direction: column;
  width: 70%;
  margin-right: 40px;
  overflow: auto;
  height: 678px
}
</style>