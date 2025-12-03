<template>
  <template v-if="!isEdit">
    <div class="profile-card">
      <div class="card-header">
        <div class="avatar-wrapper">
          <div class="avatar">{{ initials }}</div>
        </div>
        <div class="header-info">
          <div class="title">{{ userInfo.name || 'Profile overview' }}</div>
          <div class="subtitle">{{ userInfo.city || '' }}</div>
        </div>
        <div class="actions">
          <el-button type="primary" @click="editSwitch" class="edit-btn" round>
            <el-icon><Edit /></el-icon>
            编辑资料
          </el-button>
        </div>
      </div>

      <div class="card-body">
        <div class="profile-section">
          <h2 class="section-title">基本信息</h2>
          <div class="basic-info">
            <div class="info-item">
              <span class="label">姓名</span>
              <span class="value">{{ userInfo.name || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">年龄</span>
              <span class="value">{{ userInfo.age ?? '-' }} 岁</span>
            </div>
            <div class="info-item">
              <span class="label">性别</span>
              <span class="value">{{ userInfo.gender == 'male' ? '男' : (userInfo.gender=='female' ? '女' : '-') }}</span>
            </div>
            <div class="info-item">
              <span class="label">城市</span>
              <span class="value">{{ userInfo.city || '-' }}</span>
            </div>
          </div>
        </div>
        
        <div class="profile-section">
          <h2 class="section-title">联系信息</h2>
          <div class="contact-info">
            <div class="info-item">
              <span class="label">邮箱</span>
              <span class="value">{{ userInfo.email || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="label">注册时间</span>
              <span class="value">{{ userInfo.yelpingSince || '-' }}</span>
            </div>
          </div>
        </div>
        
        <div class="profile-section">
          <h2 class="section-title">统计数据</h2>
          <div class="stats-info">
            <div class="stat-card">
              <div class="stat-value">{{ userInfo.reviewCount ?? 0 }}</div>
              <div class="stat-label">评论数</div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ userInfo.reviewCount ?? 0 }}</div>
              <div class="stat-label">活跃度</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  <template v-else>
    <div class="edit-back">
      <el-button type="primary" @click="editSwitch" plain>
        <el-icon><Back /></el-icon> 返回
      </el-button>
    </div>
    <div class="edit-card">
      <h2 class="edit-title">编辑个人资料</h2>
      <el-form :model="form" label-width="100px" size="large" class="edit-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name" class="form-item">
              <el-input v-model="form.name" placeholder="请输入姓名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age" class="form-item">
              <el-input v-model="form.age" placeholder="请输入年龄"/>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender" class="form-item">
              <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="male"></el-option>
                <el-option label="女" value="female"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市" prop="city" class="form-item">
              <el-input v-model="form.city" placeholder="请输入城市"/>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="邮箱" prop="email" class="form-item">
          <el-input v-model="form.email" placeholder="请输入邮箱地址"/>
        </el-form-item>
        
        <div class="submit">
          <el-button @click="submit" type="primary" size="medium">保存更改</el-button>
          <el-button @click="writeBack" type="default" size="medium" style="margin-left:12px">重置</el-button>
        </div>
      </el-form>
    </div>
  </template>
</template>

<script lang="ts" setup>
import {computed, onMounted, onUnmounted, ref, toRef, toRefs, watch, watchEffect} from 'vue';
import UseUserEdit from "@/hooks/UseUserEdit";
import UseUserInfo from "@/hooks/UseUserInfo";
import {useEditStore} from "@/stores/UseEditStore";
import { Edit, Back } from '@element-plus/icons-vue';

// 使用编辑store
let editStore = useEditStore();

// 使用用户信息hook
let {userInfo,getUserInfo} = toRefs(UseUserInfo());

// 使用编辑hook
let userEdit = UseUserEdit()
let {form, editSwitch, isEdit, submit,writeBack} = userEdit

onMounted(()=>{
  getUserInfo.value()
  writeBack()
})

onUnmounted(()=>{
  editStore.edit = false
})

const initials = computed(() => {
  const name = userInfo.value?.name || ''
  if (!name) return 'U'
  
  // 处理多字名，提取首字母
  const cleanName = name.trim()
  if (cleanName.includes(' ')) {
    // 如果是复合姓名，取第一个和最后一个单词的首字母
    const words = cleanName.split(' ')
    return (words[0][0] + (words.length > 1 ? words[words.length - 1][0] : '')).toUpperCase()
  }
  return cleanName[0]?.toUpperCase() || 'U'
})

</script>

<style scoped>
.profile-card {
  max-width: 1200px;
  margin: 32px auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: all 0.3s ease;
  max-height: calc(100vh - 350px); /* 设置最大高度 */
  overflow-y: auto; /* 启用垂直滚动 */
}

.card-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #eee;
}

.avatar-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  backdrop-filter: blur(5px);
}

.header-info .title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 8px;
}

.header-info .subtitle {
  font-size: 16px;
  color: #6B7280;
}

.edit-btn {
  margin-left: auto;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 32px;
  margin-top: 32px;
}

.profile-section {
  background: #fafafa;
  border-radius: 12px;
  padding: 24px;
  transition: all 0.3s ease;
}

.profile-section:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.basic-info,
.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.label {
  font-size: 14px;
  color: #6B7280;
  margin-bottom: 4px;
}

.value {
  font-size: 16px;
  font-weight: 500;
  color: #111827;
}

.stats-info {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 200px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

.edit-back {
  margin: 20px 0;
}

.edit-card {
  max-width: 800px;
  margin: 16px auto;
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
}

.edit-title {
  font-size: 24px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 24px 0;
  text-align: center;
}

.edit-form .form-item {
  margin-bottom: 20px;
}

.submit {
  margin-top: 32px;
  text-align: center;
}

@media (max-width: 768px) {
  .profile-card {
    padding: 20px;
    margin: 20px;
  }
  
  .card-header {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
  
  .edit-btn {
    margin-left: 0;
  }
  
  .basic-info,
  .contact-info {
    grid-template-columns: 1fr;
  }
  
  .stats-info {
    flex-direction: column;
  }
  
  .stat-card {
    min-width: unset;
  }
  
  .edit-card {
    padding: 20px;
    margin: 16px;
  }
}
</style>