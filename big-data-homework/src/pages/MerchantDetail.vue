<template>
  <div class="merchant">
    <NewIndexView :show-background-description="false" style="background-image: none;height: 131px;border-bottom: 1px solid rgba(235, 235, 235, 1)"></NewIndexView>
    <MerchantDetailView></MerchantDetailView>
  </div>

</template>

<script setup lang="ts">
import NewIndexView from "@/components/new-index/NewIndexView.vue";
import MerchantDetailView from "@/components/merchant-detail/MerchantDetailView.vue";
</script>

<style scoped>
.merchant {
  display: flex;
  flex-direction: column;
}
</style>