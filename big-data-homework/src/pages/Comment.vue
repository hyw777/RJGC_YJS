<template>
  <div class="body">
    <p>斯蒂芬·库里（Stephen Curry），全名沃德尔·斯蒂芬·库里二世（Wardell Stephen Curry II），1988年3月14日出生于美国俄亥俄州阿克伦，是一名美国职业篮球运动员，现效力于NBA的金州勇士队，司职控球后卫。库里被广泛认为是NBA历史上最伟大的射手之一，对现代篮球的风格产生了深远影响。</p>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";

let username = ref('斯蒂芬库里')
</script>

<style scoped>
.body {
  display: flex;
  background-color: white;
  width: 1055px;
  padding: 10px;
}
</style>