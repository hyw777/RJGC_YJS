<template>
  <div class="container">
    <LoginHead :status="status"></LoginHead>
    <LoginMid @loginOrRegister="getStatus" class="mid"></LoginMid>
  </div>
</template>

<script lang="ts" setup>
import LoginHead from "@/components/login/LoginHead.vue";
import LoginMid from "@/components/login/LoginMid.vue";
import {ref} from "vue";
let status = ref()

function getStatus (newStatus:number) {
  status.value = newStatus
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: white;
  height: 900px;
}

.mid {
  margin-top: 12vh;
}

</style>