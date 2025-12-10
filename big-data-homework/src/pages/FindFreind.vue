<template>
  <div class="container">
    <NewIndexView :show-background-description="false" style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"/>
    
    <div class="content-wrapper">
      <div class="search-section">
        <h1 class="page-title">Find Friends</h1>
        
        <div class="search-box">
          <div class="search-title">Search Friends By Email</div>
          <div class="search-description">Enter an email address to find your friends on Yelp</div>
          <div class="search-input-wrapper">
            <input
              type="email"
              class="search-input"
              placeholder="Enter email address..."
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
                <p class="user-city" v-if="user.city"><el-icon><Location /></el-icon> {{ user.city }}</p>
                
                <div class="user-stats">
                  <div class="stat-item">
                    <el-icon color="#f37325"><User /></el-icon>
                    <span class="stat-number">{{ user.friendNumbers || 0 }}</span>
                    <span class="stat-label">friends</span>
                  </div>
                  <div class="stat-item">
                    <el-icon color="#f37325"><Star /></el-icon>
                    <span class="stat-number">{{ user.reviewCounts || 0 }}</span>
                    <span class="stat-label">reviews</span>
                  </div>
                </div>
              </div>
              
              <div class="add-friend" @click="showAddArea(user.name, user.userId)">
                <el-icon><Plus /></el-icon>
              </div>
            </div>
          </div>
          
          <div class="pagination-wrapper" v-if="pageResult.total > findFriendDTO.pageSize">
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
          <div class="close-btn" @click="cancel()">
            <el-icon><Close /></el-icon>
          </div>
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
          <button class="btn btn-secondary" @click="cancel()">
            Cancel
          </button>
          <button class="btn btn-primary" @click="send()">
            Send Request
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref, computed} from "vue";
import NewIndexView from '@/components/new-index/NewIndexView.vue'
import axios from "axios";
import { ElMessage } from "element-plus";
const input = ref('')
let pageResult = ref({
  records: [],
  total: 0
});
const findFriendDTO = ref({
  pageSize: 12,
  pageNum: 1,
  email: '',
  userName: ''
});

const display2 = ref(false)
const recName = ref('')
const applyInfo = ref('Your reviews are really great, I\'d love to keep in touch on Yelp.')

const applicationDTO = ref({
  reason: '',
  recipientId: 0,
  name: '',
})
const display = ref(false);

const showAddArea = (recName1, receiveId) => {
  recName.value = recName1
  display2.value = true;
  applicationDTO.value.recipientId = receiveId
  applicationDTO.value.name = recName1
}

const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

const search = () => {
  if (!validateEmail(input.value)) {
    ElMessage.error("Please enter a valid email address");
    return;
  }
  
  display.value = true;
  display2.value = false;
  findFriendDTO.value.email = input.value;
  findFriendDTO.value.userName = '';
  
  axios.post('/api/friend/find', findFriendDTO.value)
    .then((response) => {
      pageResult.value = response.data.data;
      console.log(pageResult.value)
    })
    .catch((error) => {
      console.log(error);
    })
}

const send = () => {
  applicationDTO.value.reason = applyInfo.value;
  console.log(applicationDTO.value);
  axios.post('/api/friend/add', applicationDTO.value)
    .then((response) => {
      display2.value = false;
      ElMessage.success("Friend request sent successfully!");
    })
    .catch((error) => {
      console.log(error);
      ElMessage.error("Failed to send friend request");
    })
}

const cancel = () => {
  display2.value = false;
}
</script>

<style scoped>
.container {
  width: 100vw;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf5 100%);
  padding-bottom: 40px;
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #2d3748;
  margin-bottom: 24px;
  text-align: center;
}

.search-section {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 40px;
  margin-bottom: 32px;
  transition: box-shadow 0.3s ease;
  text-align: center;
}

.search-section:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.search-title {
  color: #e00707;
  font-weight: 700;
  margin-bottom: 8px;
  font-size: 24px;
}

.search-description {
  color: #718096;
  margin-bottom: 24px;
  font-size: 16px;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.search-input-wrapper:focus-within {
  border-color: #e00707;
  box-shadow: 0 0 0 3px rgba(224, 7, 7, 0.1);
}

.search-input {
  flex: 1;
  height: 56px;
  padding: 0 20px;
  border: none;
  outline: none;
  font-size: 16px;
  background-color: #f8fafc;
}

.search-input:focus {
  background-color: #ffffff;
}

.search-button {
  border: none;
  width: 60px;
  height: 56px;
  background-color: #e00707;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-button:hover {
  background-color: #c50606;
}

.results-section {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 40px;
  transition: box-shadow 0.3s ease;
}

.results-section:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.security-info {
  margin-bottom: 32px;
}

.security-card {
  background-color: #fff5f5;
  border: 1px solid #ffecec;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
}

.security-text {
  font-weight: 700;
  color: #e00707;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.security-icon {
  margin-right: 8px;
  font-size: 20px;
}

.security-desc {
  color: #666;
  line-height: 1.6;
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
  padding: 24px;
  border: 1px solid #edf2f7;
  border-radius: 16px;
  transition: all 0.3s ease;
  position: relative;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.user-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-3px);
}

.user-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 20px;
  border: 3px solid #edf2f7;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 20px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 8px 0;
}

.user-city {
  font-size: 15px;
  color: #718096;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
}

.user-city .el-icon {
  margin-right: 6px;
  color: #a0aec0;
}

.user-stats {
  display: flex;
  gap: 24px;
}

.stat-item {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.stat-number {
  margin: 0 6px;
  font-weight: 700;
  color: #2d3748;
}

.stat-label {
  color: #718096;
}

.add-friend {
  position: absolute;
  top: 20px;
  right: 20px;
  color: #0073BB;
  cursor: pointer;
  font-size: 24px;
  transition: color 0.3s ease;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #f0f9ff;
}

.add-friend:hover {
  color: #e00707;
  background: #fef2f2;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 24px;
  border-top: 1px solid #edf2f7;
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
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  position: relative;
}

.modal-header {
  padding: 24px 24px 16px;
  border-bottom: 1px solid #edf2f7;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  color: #2d3748;
  font-size: 24px;
}

.close-btn {
  cursor: pointer;
  font-size: 20px;
  color: #a0aec0;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #e00707;
}

.modal-body {
  padding: 24px;
}

.modal-greeting {
  margin: 0 0 16px 0;
  color: #2d3748;
  font-size: 18px;
  font-weight: 500;
}

.message-input {
  width: 93%;
  height: 120px;
  padding: 16px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  resize: none;
  font-family: inherit;
  font-size: 15px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.message-input:focus {
  outline: none;
  border-color: #e00707;
  box-shadow: 0 0 0 3px rgba(224, 7, 7, 0.1);
}

.modal-footer {
  padding: 16px 24px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  font-weight: 600;
}

.btn-primary {
  background-color: #e00707;
  color: white;
}

.btn-primary:hover {
  background-color: #c50606;
  transform: translateY(-2px);
}

.btn-secondary {
  background-color: #edf2f7;
  color: #4a5568;
}

.btn-secondary:hover {
  background-color: #e2e8f0;
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-section {
    padding: 24px 16px;
  }
  
  .user-grid {
    grid-template-columns: 1fr;
  }
  
  .user-card {
    padding: 20px;
  }
  
  .user-avatar img {
    width: 70px;
    height: 70px;
    margin-right: 16px;
  }
  
  .results-section {
    padding: 24px 16px;
  }
  
  .page-title {
    font-size: 24px;
  }
}
</style>