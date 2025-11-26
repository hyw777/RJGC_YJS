<template>
  <div class="title">Collections</div>
  <template v-for="(collection, index) in collections" :key="index">
    <div class="collections">
      <img @click="jump(collection.businessVO1.businessId)" style="cursor: pointer" :src="`http://localhost:8080/images/${collection.businessVO1.image}`" class="img">
      <div class="info-box">
        <div class="name">{{ collection.businessVO1.name }}</div>
        <div class="rate">
          <el-rate v-model="collection.businessVO1.stars" disabled/>
          <div class="reviewCount">({{ collection.businessVO1.reviewCount }}reviews)</div>
        </div>
        <div class="categories">{{ collection.businessVO1.categories }}</div>
        <div class="date">{{ collection.date }}</div>
      </div>
      <el-icon class="icon" size="20" color="#E00707" @click="dialogVisible = true">
        <CollectionTag/>
      </el-icon>
    </div>
    <div class="line"></div>
    <el-dialog
        v-model="dialogVisible"
        title="Warning!"
        width="500"
    >
      <span>Are you sure to delete this collection?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="dialogVisible = false;cancel(collection.businessVO1.businessId)">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </template>
</template>

<script setup lang="ts">
import UseCollection from "@/hooks/UseCollection";
import {onMounted, ref, toRefs} from "vue";
import {CollectionTag} from "@element-plus/icons-vue";

// 使用收藏hook
let {collections, getCollections,cancel,jump} = toRefs(UseCollection())
getCollections.value()

// 对话框展示
let dialogVisible = ref(false)

</script>

<style scoped>
.title {
  color: #2D2E2F;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 40px;
}

.collections {
  display: flex;
}

.img {
  width: 90px;
  height: 90px;
}

.name {
  color: #0073BB;
  font-size: 14px;
  cursor: pointer;
}

.categories {
  color: #0073BB;
  font-size: 12px;
  cursor: pointer;
}

.line {
  height: 1px;
  width: 100%;
  background-color: rgb(235, 235, 235);
  margin-top: 15px;
  margin-bottom: 30px;
}

.info-box {
  display: flex;
  flex-direction: column;
  justify-content: end;
  margin-left: 20px;
}

.rate {
  display: flex;
  color: #2D2E2F;
}

.icon {
  margin-left: auto;
  cursor: pointer;
}

.date {
  color: #999999;
}
</style>