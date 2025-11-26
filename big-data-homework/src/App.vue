<template>
    <RouterView></RouterView>
</template>

<script setup lang="ts">
import {RouterView} from "vue-router";
</script>

<style scoped>

:deep(*) {
  font-family: '微软雅黑', Arial, sans-serif,Inter, 'Helvetica Neue', Helvetica, 'PingFang SC',
  'Hiragino Sans GB', 'Microsoft YaHei' ;
}

:deep(a) {
  text-decoration: none;
}

:deep(.logo) {
  width: 80px;
}

</style>