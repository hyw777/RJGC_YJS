<template>
  <div class="container">
    <NewIndexView></NewIndexView>
    <!-- <NewIndexTop></NewIndexTop> -->
    <new-index-activity v-if="authStore.token"></new-index-activity>
    <!-- <new-index-categories></new-index-categories> -->
  </div>
</template>

<script lang="ts" setup>
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import NewIndexActivity from "@/components/new-index/NewIndexActivity.vue";
import NewIndexCategories from "@/components/new-index/NewIndexCategories.vue";
import {useAuthStore} from "@/stores/UseAuthStore";
import NewIndexTop from "@/components/new-index/NewIndexTop.vue";

let authStore = useAuthStore()
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
}
</style>