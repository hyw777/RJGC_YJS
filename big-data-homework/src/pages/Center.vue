<template>
  <div class="container">
    <CenterHead class="head"></CenterHead>
    <div class="back">
      <CenterBody class="body"></CenterBody>
      <CenterFoot class="foot"></CenterFoot>
    </div>

  </div>
</template>

<script lang="ts" setup>
import CenterHead from "@/components/center/CenterHead.vue";
import CenterBody from  "@/components/center/CenterBody.vue";
import CenterFoot from "@/components/center/CenterFoot.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.back {
  background-image: url("https://www.dpfile.com/s/c/user/skins/bluesky/i/bg.2f8b5b39bff80987a7bc0602cf3e315b.jpg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.body {
  margin-top: 75px;
}

.foot {
  margin-top: 20px;
}

</style>