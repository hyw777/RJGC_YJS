<template>
  <div class="container">
    <NewIndexView :show-background-description="false" style="background-image: none; height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <ProfileMid></ProfileMid>
  </div>
</template>

<script lang="ts" setup>
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import ProfileMid from "@/components/profile/ProfileMid.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
}

</style>