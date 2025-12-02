<template>
  <div class="overview-wrapper">
    <div class="overview-card">
      <div class="overview-header">
        <div class="avatar">{{ initials }}</div>
        <div class="title-block">
          <div class="title">Profile overview</div>
          <div class="subtitle">用户基本信息与活跃度</div>
        </div>
      </div>

      <div class="info-grid">
        <div class="info-row"><div class="label">Name</div><div class="value">{{ friendInfo.userVO.name || '-' }}</div></div>
        <div class="info-row"><div class="label">Age</div><div class="value">{{ friendInfo.userVO.age ?? '-' }}</div></div>
        <div class="info-row"><div class="label">Gender</div><div class="value">{{ friendInfo.userVO.gender || '-' }}</div></div>
        <div class="info-row"><div class="label">City</div><div class="value">{{ friendInfo.userVO.city || '-' }}</div></div>
        <div class="info-row"><div class="label">Email</div><div class="value">{{ friendInfo.userVO.email || '-' }}</div></div>
        <div class="info-row"><div class="label">Reviews</div><div class="value">{{ friendInfo.userVO.reviewCount ?? 0 }}</div></div>
        <div class="info-row"><div class="label">Useful</div><div class="value">{{ friendInfo.userVO.useful ?? 0 }}</div></div>
        <div class="info-row"><div class="label">Funny</div><div class="value">{{ friendInfo.userVO.funny ?? 0 }}</div></div>
        <div class="info-row"><div class="label">Cool</div><div class="value">{{ friendInfo.userVO.cool ?? 0 }}</div></div>
        <div class="info-row"><div class="label">Elite</div><div class="value">{{ friendInfo.userVO.elite ?? '-' }}</div></div>
        <div class="info-row"><div class="label">Yelping Since</div><div class="value">{{ friendInfo.userVO.yelpingSince || '-' }}</div></div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, ref, toRef, toRefs, watch, watchEffect} from 'vue';
import UseFriendInfo from "@/hooks/UseFriendInfo";

// 获取用户id
let props = defineProps(['userId'])
let userId = computed(() => props.userId)

// 使用朋友信息hook
let {friendInfo,getFriendInfo} = toRefs(UseFriendInfo())


onMounted(()=>{
  getFriendInfo.value(userId.value)
})

const initials = computed(() => {
  const n = friendInfo?.userVO?.name || ''
  return n ? String(n).trim()[0]?.toUpperCase() : '?'
})
</script>

<style scoped>
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

/* New styles for overview card */
.overview-wrapper { display:flex; justify-content:center; }
.overview-card {
  width: 100%;
  max-width: 820px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(16,24,40,0.08);
  padding: 20px 22px;
  box-sizing: border-box;
}
.overview-header { display:flex; align-items:center; gap:16px; margin-bottom:14px }
.avatar {
  width:64px; height:64px; border-radius:50%; background: linear-gradient(135deg,#6EE7B7,#3B82F6);
  color: #fff; display:flex; align-items:center; justify-content:center; font-weight:700; font-size:24px;
  flex: 0 0 64px;
}
.title-block .title { font-size:20px; font-weight:700; color:#111827 }
.title-block .subtitle { font-size:13px; color:#6B7280; margin-top:4px }
.info-grid { display:grid; grid-template-columns: repeat(2, 1fr); gap:12px 24px; margin-top:6px }
.info-row { display:flex; align-items:center; gap:8px; padding:10px 12px; background: #FBFDFF; border-radius:8px }
.label { color:#6B7280; font-size:13px; width:120px }
.value { color:#111827; font-weight:600 }

@media (max-width:640px) {
  .info-grid { grid-template-columns: 1fr; }
  .overview-card { padding:16px }
  .label { width:100px }
}

</style>