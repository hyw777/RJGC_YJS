<template>
  <el-input
      v-model="message"
      style="width: 240px"
      placeholder="搜索商户名"
  >
    <template #prefix>
      <el-icon class="el-input__icon">
        <search/>
      </el-icon>
    </template>
  </el-input>
</template>

<script setup lang="ts">
import {Calendar, Search} from '@element-plus/icons-vue'
import {ref} from "vue";
let message = ref<string>()
</script>

<style scoped>

</style>