<template>
  <div class="container">
    <NewIndexView :show-background-description="false" style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <FriendInfoMid :userId="userId"></FriendInfoMid>
  </div>
</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import FriendInfoMid from "@/components/friendInfo/FriendInfoMid.vue";
import {useRoute} from "vue-router";

let route = useRoute();
let userId = route.query.userId

</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
}
</style>