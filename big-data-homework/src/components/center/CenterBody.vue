<template>
  <div class="body">
    <div class="body-left">
      <img
          src="https://p0.meituan.net/ingee/3b614c2d01ec5382444d441bf33620f610001.png%40120w_120h_1e_1c_1l%7Cwatermark%3D0"
          alt="用户头像">
    </div>
    <div class="body-mid">
      <div class="username">
        {{ username }}
      </div>
      <div class="detail">
        <div class="fans">
          <div class="fansDescribe">粉丝</div>
          <div class="fansNum">20</div>
        </div>
        <div class="likes">
          <div class="likesDescribe">获赞</div>
          <div class="likesNum">100</div>
        </div>
      </div>
      <div class="nav">
        <router-link replace to="/comment" active-class="active" class="comment">评论</router-link>
        <router-link replace to="/edit" active-class="active" class="edit">编辑</router-link>
      </div>
    </div>
    <div class="body-right">
      注册时间:&nbsp;&nbsp;&nbsp;{{date}}
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
let date = ref('2002-01-01')
let username = ref('斯蒂芬库里')
</script>

<style scoped>
.body {
  display: flex;
  background-color: white;
  width: 57vw;
  padding: 10px;
}

.body-mid {
  margin-left: 4vw;
  display: flex;
  flex-direction: column;
}

.comment {
  color: #2595B7;
  padding: 5px 30px;
  background-color: #f3f8fb;
}

.edit {
  color: #2595B7;
  padding: 5px 30px;
  background-color: #f3f8fb;
  margin-left: 40px;
}

.active {
  background-color: #75c5de;
  color: white;
}

.body-right {
  margin-left: auto;
  font-size: 18px;
  color: #555555;
}

.likes {
  margin-left: 40px;
}

.fansDescribe {
  color: #555555;
  font-size: 14px;
}

.likesDescribe {
  color: #555555;
  font-size: 14px;
}

.username {
  color: black;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
}

.detail {
  display: flex;
  margin-top: 4px;
}

.nav {
  display: flex;
  margin-top: auto;
}


</style>