<template>
  <div class="container">

  <NewIndexView style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"/>
  <div class="top">
   <div class="font">
     Find Friends
   </div>
    <div class="by-email" @click="changeTip(1)">
      <el-icon style="font-size: 28px; margin-right: 10px"><Message /></el-icon>
      Search With Email
    </div>
    <div class="by-name" @click="changeTip(2)">
      <el-icon style="font-size: 28px;margin-right: 10px"><User /></el-icon>
      Search With Name
    </div>
    <div class="search-box">
      <div class="title">Search Friends On Yelp</div>
      <div style="display: flex;align-items: center;">
        <input
            type="text"
            class="search-input"
            :placeholder="placeholderText"
            @keyup.enter="search"
            v-model="input"
        />
        <button  class="search-button" @click="search"><el-icon size="20px" ><Search/></el-icon></button>
      </div>

    </div>

  </div>
  <div class="middle">
    <div class="tip2" v-show="!display">
      <p><el-icon style="font-size: 20px;margin-left: 2px"><Lock /></el-icon>Your information is safe.</p>
     <p>We don't store your email password. Your address book is used to find friends on Yelp.</p>
    </div>

    <div class="page-data" v-show="display">
      <div class="each-data" v-for="(user,index) in pageResult.records" :key="index" >
          <img src="https://img2.baidu.com/it/u=660058380,2227239641&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=716" style="width: 90px;height: 90px">
            <p class="name">{{user.name}}</p>
            <div class="city">{{user.city}}</div>
            <div class="friends">
              <el-icon color="#f37325"><User /></el-icon>
              <div class="number">{{user.friendNumbers}}</div>
              <div class="detail">friends</div>
            </div>
            <div class="reviews">
              <el-icon color="#f37325"><Star /></el-icon>
              <div class="number">{{user.reviewCounts}}</div>
              <div class="detail">reviews</div>
            </div>


        <div class="add-friend" @click="showAddArea(user.name,user.userId)"><el-icon><Plus /></el-icon></div>
      </div>

      <div class="add-box" v-show="display2">
        <p class="add-box-tip1"> Add a Friend</p>
        <p class="add-box-tip2">Hi,{{recName}}</p>
        <textarea
            class="add-box-applyInfo"
            v-model="applyInfo"/>

        <button class="add-box-send" @click="send(applicationDTO)">
          Send
        </button>

        <button class="add-box-cancel" @click="cancel()">
          Cancel
        </button>

      </div>

    </div>
    <div class="pageBottom">
      <el-pagination background  v-model:current-page="findFriendDTO.pageNum" layout="prev, pager, next" :page-count=" Math.ceil(pageResult.total/findFriendDTO.pageSize)"    @current-change="search"
      />
    </div>
  </div>

  </div>
</template>

<script lang="ts" setup>
import {ref,computed} from "vue";
import NewIndexView from '@/components/new-index/NewIndexView.vue'
import axios from "axios";

const tip=ref('find friend with the current email...')
const input = ref('')
let pageResult=ref([]);
const findFriendDTO=ref({
  pageSize:12,
  pageNum:1,
  email:'',
  userName:''
});

const display2=ref(false)
const recName=ref('')
const applyInfo=ref('Your reviews are really great, I\'d love to keep in touch on Yelp.')

const applicationDTO=ref({
  reason:applyInfo.value,
  recipientId:0,
  name:'',
})
const display=ref(false);

const showAddArea=(recName1,receiveId)=>{
  recName.value=recName1
  display2.value=true;
  applicationDTO.value.recipientId=receiveId
  applicationDTO.value.name=recName1
}

// 假设你有一个条件来决定placeholder的值
const placeholderText = computed(() => {
  // 这里可以根据tip的值或其他逻辑来返回不同的字符串
  return tip.value;
});

const changeTip = (type)=>{
  if(type == 1){
    tip.value='find friend with the current email...'
  }
  if(type == 2){
    tip.value='find friend with name...'
  }
}

const search=()=>{
  display.value = true;
  display2.value=false;
  if(input.value.includes("@qq.com") )
  {
    findFriendDTO.value.email=input.value;
    findFriendDTO.value.userName='';
  }
  else {
    findFriendDTO.value.userName=input.value;
    findFriendDTO.value.email='';
  }
  axios.post('/api/friend/find',findFriendDTO.value
  ).then((response)=>{
    pageResult.value=response.data.data;
    console.log(pageResult.value)
  }).catch((error)=>{
        console.log(error);
      }
  )
}


const send=()=>{
  applicationDTO.value.reason = applyInfo.value;
  console.log(applicationDTO.value);
  axios.post('/api/friend/add',applicationDTO.value)
      .then((response)=>{
        display2.value=false;
      })
      .catch((error)=>{
        console.log(error)
      })


}

const cancel=()=>{
  display2.value=false;
}


</script>

<style scoped>
.container{
  width: 100vw;
  height: 100vh;
}

.top{
  margin-top: 1%;
  margin-left: 25%;
  width:15%;
  height: 20%;
}

.font{
  font-size: 19px;
  margin-bottom: 20px;
}

.by-email,.by-name{
  display: flex;
  align-items: center;
  height: 50px;
  border: #bfbfbf 1px solid;
  font-size: 14px;
}
.by-name{
  border-top: none;
}

.search-box{
  margin-top: 3%;
  width: 400px;
  height: 50px;
  background-color: #ffffff;
  margin-left: 110%;
  margin-top: -20%;
}
.title{
  color: #e00707;
  margin-top: -18%;
  width: 20vw;
  font-weight: 700;
}
.search-input{
  margin-top: 2%;
  width: 37vh;
  height: 33px;
}
.search-button{
  border: none;
  width: 6vh;
  height: 37px;
  background-color: white;
}



.tip2{
  font-weight: 700;
  color: #e00707;
  margin-left: 30%;
  margin-top: 10%;
}

.page-data{
  display: flex;
  width: 100%;
  flex-wrap: wrap; /* 允许容器内的项目换行 */
  position: relative;
}
.each-data{
  margin-right: 70px;
  width: 29vw;
  height: 90px;
  display: flex;
  margin-bottom: 15px;
  border-top: 2px solid rgba(198, 193, 193, 0.18);
  position: relative;

}



.name{
 margin: 0;
  margin-left: 8px;
  font-size: 19px;
  text-align: left;
  color: #629fb2;
  width: 350px;
}

.city{
  margin-top: 26px;
  text-align: left;
  margin-left:99px;
  font-size: 15px;
  position: absolute;
}
.friends {
  display: flex;
  align-items: center;
  font-size: 14px;
  margin-left: 99px;
  margin-top: 48px;
  position: absolute;
}

.reviews {
  display: flex;
  align-items: center;
  font-size: 14px;
  margin-left: 99px;
  margin-top: 70px;
  position: absolute;
}
.detail {
  margin-left: 10px;
  color: #666666;
}

.add-friend{
  margin-top: 3vh;
  color: #0073BB;
}

.add-box{
  width: 35vw;
  height: 40vh;
  background-color: #ffffff;
  border: #adabab 2px solid;
  margin-left: 35%;
  margin-top: 3%;
  position:absolute;
  opacity: 95%;
}

.add-box-tip1{
  color: red;
  font-size: 20px;
  font-weight: 700;
  padding-bottom: 20px;
  border-bottom: #b4b4b4 1px solid;
  margin-left: 40px;
}

.add-box-tip2{
  margin-left: 50px;
  font-size: 14px;
}

.add-box-applyInfo{
  width: 30vw;
  height: 10vh;
  margin-left: 50px ;
  border: #63acdf 2px solid;
  overflow: auto;
  display: flex;
}

.add-box-send{
  font-size: 15px;
  margin-left: 3vw;
  margin-top: 5vh;
  height: 4vh;
  width: 3vw;
  border: none;
  background-color: red;
  color: white;
}

.add-box-cancel{
  margin-left: 10px;
  font-size: 15px;
  height: 4vh;
  width: 4vw;
  border: none;
  color: #73bcd2;
  background-color: white;
}


.pageBottom{
  display: flex;
  justify-content: center;
  width: 100%;
  height: 6%;
  border-top: #d61515 2px solid;
  position: absolute;
  bottom: 10px;
}


</style>