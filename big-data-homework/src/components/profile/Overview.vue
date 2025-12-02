<template>
  <template v-if="!isEdit">
    <div class="profile-card">
      <div class="card-header">
        <div class="avatar">{{ initials }}</div>
        <div class="header-info">
          <div class="title">{{ userInfo.name || 'Profile overview' }}</div>
          <div class="subtitle">{{ userInfo.city || '' }}</div>
        </div>
        <div class="actions">
          <el-button type="text" @click="editSwitch" class="edit-btn">
            <el-icon><Edit/></el-icon>
          </el-button>
        </div>
      </div>

      <div class="card-body">
        <div class="left">
          <h2 class="name">{{ userInfo.name || '-' }}</h2>
          <p class="meta">{{ userInfo.age ?? '-' }}岁 · {{ userInfo.gender == 'male' ? '男' : (userInfo.gender=='female' ? '女' : '-') }}</p>
        </div>
        <div class="right">
          <div class="info-grid">
            <div class="info"><span class="label">邮箱</span><span class="val">{{userInfo.email || '-'}}</span></div>
            <div class="info"><span class="label">评论数</span><span class="val">{{userInfo.reviewCount ?? 0}}</span></div>
            <div class="info"><span class="label">注册时间</span><span class="val">{{userInfo.yelpingSince || '-'}}</span></div>
            <div class="info"><span class="label">活跃度</span><span class="val">{{userInfo.reviewCount ?? 0}} 条点评</span></div>
          </div>
        </div>
      </div>
    </div>
  </template>
  <template v-else>
    <div class="edit-back">
      <el-button type="text" @click="editSwitch"><el-icon><Back/></el-icon> 返回</el-button>
    </div>
    <div class="edit-card">
      <el-form :model="form" label-width="90px" size="large" class="edit-form">
        <el-form-item label="姓名" prop="name" class="form-item">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="年龄" prop="age" class="form-item">
          <el-input v-model="form.age"/>
        </el-form-item>
        <el-form-item label="性别" prop="gender" class="form-item">
          <el-input v-model="form.gender"/>
        </el-form-item>
        <el-form-item label="城市" prop="city" class="form-item">
          <el-input v-model="form.city"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" class="form-item">
          <el-input v-model="form.email"/>
        </el-form-item>
        <div class="submit">
          <el-button @click="submit" type="primary" size="medium">保存</el-button>
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
  const n = userInfo?.name || ''
  return n ? String(n).trim()[0]?.toUpperCase() : 'U'
})
</script>

<style scoped>

.container {
  align-self: center;
  background-color: #f5f5f5;
  font-family: Arial, sans-serif;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 40px;

}
.profile {
  text-align: center;
}
.profile img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 20px;
}
.profile h2 {
  font-size: 24px;
  color: #333333;
  margin: 0;
  margin-bottom: 10px;
}
.profile p {
  font-size: 16px;
  color: #777777;
  margin: 5px 0;
}
.profile-info {
  text-align: left;
  margin-top: 20px;
}
.profile-info div {
  margin-bottom: 10px;
}
.profile-info span {
  font-weight: bold;
  color: #333333;
}

.el-form {
  margin-left: 60px;
  margin-top: 20px;
}

.form-item {
  width: 600px;
}

.title-box {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.title {
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
}

.edit {
  color: #6E7072;
  font-size: 28px;
  cursor: pointer;
  display: flex;
  align-items: end;
}

.back {
  color: #6E7072;
  font-size: 28px;
  align-items: start;
  cursor: pointer;
  margin-left: auto;
}

.el-box {
  display: flex;
}

.submit {
  margin-top: 20px;
}

/* Card styles */
.profile-card { max-width: 1800px; margin: 32px auto; background: #fff; border-radius:12px; box-shadow: 0 8px 24px rgba(16,24,40,0.06); padding: 18px }
.card-header { display:flex; align-items:center; gap:16px }
.avatar { width:72px; height:72px; border-radius:50%; background: linear-gradient(135deg,#ff9999,#fa3d3d); color:#fff; display:flex;align-items:center;justify-content:center;font-size:26px;font-weight:700 }
.header-info .title { font-size:20px; font-weight:700; color:#111827 }
.header-info .subtitle { font-size:13px; color:#6B7280 }
.actions { margin-left:auto }
.card-body { display:flex; gap:20px; margin-top:16px }
.left { flex:1 }
.right { flex:1 }
.name { margin:0; font-size:18px }
.meta { color:#6B7280; margin-top:6px }
.info-grid { display:grid; grid-template-columns:1fr 1fr; gap:10px }
.info { background:#FBFDFF; padding:10px;border-radius:8px; display:flex; justify-content:space-between }
.label { color:#6B7280 }
.val { font-weight:600 }

.edit-back { margin: 10px 0 }
.edit-card { max-width:820px; margin:16px auto; background:#fff; padding:18px; border-radius:10px; box-shadow:0 6px 18px rgba(16,24,40,0.05) }
.edit-form .form-item { margin-bottom:12px }

@media (max-width:1000px) {
  .card-body { flex-direction:column }
  .info-grid { grid-template-columns:1fr }
}

</style>