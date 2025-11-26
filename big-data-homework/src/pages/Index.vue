<template>
  <div class="container">
    <TopNav class="child"></TopNav>
    <Category class="child"></Category>
    <Popular class="child"></Popular>
    <HighScore class="child"></HighScore>
  </div>
</template>

<script lang="ts" setup>
    import TopNav from "@/components/index/TopNav.vue"
    import Category from "@/components/index/Category.vue";
    import Popular from "@/components/index/Popular.vue";
    import HighScore from "@/components/index/HighScore.vue";
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: #f8f8f8;
}

.child {
  margin-bottom: 29px;
}

</style>