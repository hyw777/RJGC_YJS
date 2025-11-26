<template>
  <div class="container">
    <RegisterHead></RegisterHead>
    <RegisterMid class="mid"></RegisterMid>
  </div>
</template>

<script lang="ts" setup>
import RegisterHead from "@/components/register/RegisterHead.vue";
import RegisterMid from "@/components/register/RegisterMid.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: #f8f8f8;
}

.mid {
  margin-top: 29px;
}

</style>