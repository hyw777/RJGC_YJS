<template>
<div class="all">
  <div class="search-box">
    <input
        type="text"
        class="search-input"
        placeholder="search with email or name..."
        @keyup.enter="search"
        v-model="input"
    />
    <button  class="search-button" @click="search"><el-icon size="20px"><Search/></el-icon></button>
  </div>


<!--  用户数据展示框-->
  <div class="item">
        <div class="page-data">
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
              <div class="number">{{user.friendNumbers}}</div>
              <div class="detail">reviews</div>
            </div>
            <el-icon class="view" size="25px" color="grey" @click="seeStatus(user.userId)"><Tools /></el-icon>
          </div>
          <div v-if="display " class="seeBox">
             <div v-if="status == 0">
               The user is normal now
               <button class="back-botton" @click="display = ! display">
                 <el-icon><Back /></el-icon>
               </button>
               <button class="cz-button" @click="changeUserStatus(userId,1)">
                 <el-icon><Lock/></el-icon>
               </button>
             </div>
            <div v-if="status == 1">
              This user is block now
              <button class="back-botton" @click="display = ! display">
                <el-icon><Back /></el-icon>
              </button>
              <button class="cz-button" @click="changeUserStatus(userId,0)">
                <el-icon><Key /></el-icon>
              </button>
            </div>
          </div>
          <div class="pageBottom">
            <el-pagination background v-model:current-page="findFriendDTO.pageNum" layout="prev, pager, next" :page-count=" Math.ceil(pageResult.total/findFriendDTO.pageSize)"    @current-change="search"  style="margin-left: 433px"
            />
          </div>
        </div>
  </div>


</div>
</template>
<script lang="ts" setup>
 import {ref,onMounted} from "vue";
 import axios from "axios";
 onMounted(()=>{search()})

//   数据定义在这里
let input=ref('');
 let pageResult=ref([]);
const findFriendDTO=ref({
   pageSize:10,
   pageNum:1,
   email:'',
   userName:''
 });
let status=ref(-1);
let userId=ref(0);
let display=ref(false);
 // 当前页的数据
const search=()=>{
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
  )}


 const seeStatus=(id)=>{
    axios.get(`/api/userManage/getStatus/${id}`)
    .then(response=>{
    status.value=response.data.data;
    display.value=true;
    userId.value=id;
    })
    .catch(error=>{
    console.log(error);})
 }

 const changeUserStatus=(userId,num)=>{
  console.log(userId);
  axios.put(`/api/userManage/changeStatus`,
      {
          userId:userId,
          status:num
      }).then(response=>{
    display.value = ! display.value
  }).catch(
      error=>{
        console.log(error);}
  )


 }







</script>

<style scoped>
.search-box{
  margin-top: 3%;
  width: 400px;
  height: 50px;
  margin-left: 33%;
  position: absolute;
  background-color: #ffffff;
}
.search-input{
  width: 330px;
  height: 40px;
}
.search-button{
  border: none;
  width: 60px;
  height: 40px;
  background-color:white;
}
.item{
  /* 去掉margin-left，因为我们要使用left属性进行定位 */
  width: 1200px;
  height: 610px;
  position: relative; /* 如果.item不是直接子元素，可以使用absolute或fixed */
  /* 使用top属性将.item放在.search-box下方 */
  top: 130px; /* 因为.search-box的高度是50px，所以这里也设置为50px */
  /* 如果你希望.item有水平偏移，可以添加left属性 */
  left: 5%; /* 与.search-box的margin-left相同，以保持水平对齐 */
  /* 如果需要，可以添加margin-left或transform来调整水平位置 */
  margin-left: 0; /* 去掉之前的margin-left */
  /* 注意：如果.item是.search-box的直接子元素，则position应该为absolute */
}
.page-data{
  display: flex;
  flex-wrap: wrap; /* 允许容器内的项目换行 */
  gap: 10px;
}
.each-data{
  margin-right: 70px;
  width: 520px;
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
.view{
  margin-top: 20px;
  color: #928484;
  top:20px;
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
.seeBox{
  border-radius: 20px;
  font-size: 20px;
  width: 400px;
  height: 70px;
  position: absolute;
  background-color: rgba(241, 241, 241, 0.75);
  left: 280px;
  top:100px;
  text-align: center;
  border: 2px solid;

}
.pageBottom{
  width: 100%;
  height: 6%;
  border-bottom: #e8e5dd 1px solid;
  position: absolute;
  bottom: 0px;
}

.number {
  margin-left: 10px;
  color: #111111;
}
.back-botton{
  border: none;
  font-size: 20px;
  bottom: 0;
  position: absolute;
  left: 20px;
  background-color: transparent;
}
.cz-button{
  border: none;
  color: #191717;
  bottom: 0;
  position: absolute;
  left: 320px;
  background-color: transparent;
  font-size: 30px;
}




</style>