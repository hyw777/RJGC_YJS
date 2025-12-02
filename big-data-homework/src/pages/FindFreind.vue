<template>
  <div class="container">
    <NewIndexView :show-background-description="false" style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"/>
    
    <div class="content-wrapper">
      <div class="search-section">
        <h1 class="page-title">Find Friends</h1>
        
        <div class="search-methods">
          <div 
            class="method-card" 
            :class="{ 'active': tip === 'find friend with the current email...' }"
            @click="changeTip(1)"
          >
            <el-icon class="method-icon"><Message /></el-icon>
            <span class="method-text">Search With Email</span>
          </div>
          
          <div 
            class="method-card" 
            :class="{ 'active': tip === 'find friend with name...' }"
            @click="changeTip(2)"
          >
            <el-icon class="method-icon"><User /></el-icon>
            <span class="method-text">Search With Name</span>
          </div>
        </div>
        
        <div class="search-box">
          <div class="search-title">Search Friends On Yelp</div>
          <div class="search-input-wrapper">
            <input
              type="text"
              class="search-input"
              :placeholder="placeholderText"
              @keyup.enter="search"
              v-model="input"
            />
            <button class="search-button" @click="search">
              <el-icon size="20px"><Search /></el-icon>
            </button>
          </div>
        </div>
      </div>
      
      <div class="results-section">
        <div class="security-info" v-show="!display">
          <div class="security-card">
            <p class="security-text">
              <el-icon class="security-icon"><Lock /></el-icon>
              Your information is safe.
            </p>
            <p class="security-desc">We don't store your email password. Your address book is used to find friends on Yelp.</p>
          </div>
        </div>
        
        <div class="results-container" v-show="display">
          <div class="user-grid">
            <div 
              class="user-card" 
              v-for="(user,index) in pageResult.records" 
              :key="index"
            >
              <div class="user-avatar">
                <img 
                  src="https://img2.baidu.com/it/u=660058380,2227239641&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=716" 
                  :alt="user.name"
                >
              </div>
              
              <div class="user-info">
                <h3 class="user-name">{{ user.name }}</h3>
                <p class="user-city">{{ user.city }}</p>
                
                <div class="user-stats">
                  <div class="stat-item">
                    <el-icon color="#f37325"><User /></el-icon>
                    <span class="stat-number">{{ user.friendNumbers }}</span>
                    <span class="stat-label">friends</span>
                  </div>
                  <div class="stat-item">
                    <el-icon color="#f37325"><Star /></el-icon>
                    <span class="stat-number">{{ user.reviewCounts }}</span>
                    <span class="stat-label">reviews</span>
                  </div>
                </div>
              </div>
              
              <div class="add-friend" @click="showAddArea(user.name, user.userId)">
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </div>
          
          <div class="pagination-wrapper">
            <el-pagination 
              background 
              v-model:current-page="findFriendDTO.pageNum" 
              layout="prev, pager, next" 
              :page-count="Math.ceil(pageResult.total/findFriendDTO.pageSize)" 
              @current-change="search"
            />
          </div>
        </div>
      </div>
    </div>
    
    <div class="add-friend-modal" v-show="display2">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Add a Friend</h2>
        </div>
        <div class="modal-body">
          <p class="modal-greeting">Hi, {{ recName }}</p>
          <textarea
            class="message-input"
            v-model="applyInfo"
            placeholder="Enter your message here..."
          />
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="send(applicationDTO)">
            Send
          </button>
          <button class="btn btn-secondary" @click="cancel()">
            Cancel
          </button>
        </div>
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
.container {
  width: 100vw;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40px;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 24px;
}

.search-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
  margin-bottom: 32px;
  transition: box-shadow 0.3s ease;
}

.search-section:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.search-methods {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
}

.method-card {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  border: 1px solid #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  flex: 1;
  justify-content: center;
}

.method-card:hover {
  border-color: #e00707;
  background-color: #fff5f5;
}

.method-card.active {
  border-color: #e00707;
  background-color: #fff5f5;
  font-weight: 600;
}

.method-icon {
  font-size: 24px;
  margin-right: 12px;
  color: #666;
}

.method-text {
  font-size: 16px;
  color: #333;
}

.search-box {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #eee;
}

.search-title {
  color: #e00707;
  font-weight: 700;
  margin-bottom: 16px;
  font-size: 18px;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 6px;
  overflow: hidden;
  transition: border-color 0.3s ease;
}

.search-input-wrapper:focus-within {
  border-color: #e00707;
}

.search-input {
  flex: 1;
  height: 42px;
  padding: 0 16px;
  border: none;
  outline: none;
  font-size: 16px;
}

.search-button {
  border: none;
  width: 50px;
  height: 42px;
  background-color: #f8f8f8;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #e00707;
  color: white;
}

.results-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: box-shadow 0.3s ease;
}

.results-section:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.security-info {
  margin-bottom: 32px;
}

.security-card {
  background-color: #fff5f5;
  border: 1px solid #ffecec;
  border-radius: 8px;
  padding: 20px;
}

.security-text {
  font-weight: 700;
  color: #e00707;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.security-icon {
  margin-right: 8px;
  font-size: 20px;
}

.security-desc {
  color: #666;
  line-height: 1.5;
}

.user-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.user-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 12px;
  transition: all 0.3s ease;
  position: relative;
}

.user-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.user-avatar img {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 16px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #629fb2;
  margin: 0 0 4px 0;
}

.user-city {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px 0;
}

.user-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.stat-number {
  margin: 0 4px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  color: #666;
}

.add-friend {
  position: absolute;
  top: 20px;
  right: 20px;
  color: #0073BB;
  cursor: pointer;
  font-size: 20px;
  transition: color 0.3s ease;
}

.add-friend:hover {
  color: #e00707;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.add-friend-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.modal-header {
  padding: 24px 24px 16px;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.modal-body {
  padding: 24px;
}

.modal-greeting {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 16px;
}

.message-input {
  width: 100%;
  height: 120px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  resize: none;
  font-family: inherit;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.message-input:focus {
  outline: none;
  border-color: #e00707;
}

.modal-footer {
  padding: 16px 24px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn {
  padding: 10px 24px;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-primary {
  background-color: #e00707;
  color: white;
}

.btn-primary:hover {
  background-color: #c00606;
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #666;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}


</style>