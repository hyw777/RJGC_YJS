<template>
  <div class="collections-container">
    <div class="header-section">
      <h2 class="title">Collections</h2>
      <div class="collections-count">
        {{ collections.length || 0 }} collections
      </div>
    </div>

    <div
      v-if="!collections || collections.length === 0"
      class="empty-state"
    >
      <div class="empty-icon">📚</div>
      <p>No collections found</p>
    </div>

    <div v-else class="collections-list">
      <template v-for="(collection, index) in collections" :key="index">
        <div class="collection-card">
          <div class="collection-image" @click="jump(collection.businessVO1.businessId)">
            <template v-if="collection.businessVO1.image">
              <img
                :src="`http://localhost:3000/images/${collection.businessVO1.image}.jpg`"
                :alt="collection.businessVO1.name"
                class="business-image"
                @error="handleImageError"
              />
            </template>
            <template v-else>
              <div class="initials-avatar">
                {{ getInitials(collection.businessVO1.name) }}
              </div>
            </template>
          </div>
          <div class="collection-info">
            <h3 class="business-name" @click="jump(collection.businessVO1.businessId)">
              {{ collection.businessVO1.name }}
            </h3>
            <div class="business-rating">
              <el-rate
                v-model="collection.businessVO1.stars"
                disabled
                :colors="['#F56C6C', '#E6A23C', '#67C23A']"
                void-color="#DCDFE6"
              />
              <span class="review-count">
                ({{ collection.businessVO1.reviewCount }} reviews)
              </span>
            </div>
            <div class="business-categories">
              {{ collection.businessVO1.categories }}
            </div>
            <div class="collection-date">{{ collection.date }}</div>
          </div>
          <el-icon class="delete-icon" size="20" color="#E00707" @click="showDeleteDialog(index)">
            <CollectionTag />
          </el-icon>
        </div>
        
        <el-dialog
          v-model="dialogVisible"
          title="Warning!"
          width="500"
        >
          <span>Are you sure to delete this collection?</span>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVisible = false">Cancel</el-button>
              <el-button type="danger" @click="confirmDelete()">
                Confirm
              </el-button>
            </div>
          </template>
        </el-dialog>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import UseCollection from "@/hooks/UseCollection";
import { ref, toRefs } from "vue";
import { CollectionTag } from "@element-plus/icons-vue";

// 使用收藏hook
let { collections, getCollections, cancel, jump } = toRefs(UseCollection());
getCollections.value();

// 对话框展示
let dialogVisible = ref(false);
let currentDeleteIndex = ref<number | null>(null);

// 显示删除对话框
const showDeleteDialog = (index: number) => {
  currentDeleteIndex.value = index;
  dialogVisible.value = true;
};

// 确认删除
const confirmDelete = () => {
  if (currentDeleteIndex.value !== null) {
    const businessId = collections.value[currentDeleteIndex.value].businessVO1.businessId;
    cancel.value(businessId);
    dialogVisible.value = false;
    currentDeleteIndex.value = null;
  }
};

// 获取商家名称首字母
const getInitials = (name: string) => {
  if (!name) return "?";
  return name.trim().charAt(0).toUpperCase();
};

// 处理图片加载错误
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement;
  img.src = "https://via.placeholder.com/120x120/f5f5f5/999999?text=No+Image";
};
</script>

<style scoped>
.collections-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  max-height: 600px; /* 添加最大高度 */
  overflow-y: auto; /* 启用垂直滚动 */
}


.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebebeb;
}

.title {
  color: #2d2e2f;
  font-size: 28px;
  font-weight: 700;
  margin: 0;
}

.collections-count {
  color: #666666;
  font-size: 16px;
  background-color: #f5f5f5;
  padding: 5px 15px;
  border-radius: 20px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.collections-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.collection-card {
  display: flex;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #eee;
}

.collection-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.collection-image {
  flex: 0 0 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  cursor: pointer;
}

.business-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.initials-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #6ee7b7, #3b82f6);
  color: white;
  font-size: 28px;
  font-weight: bold;
}

.collection-info {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.business-name {
  color: #0073bb;
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 10px 0;
  cursor: pointer;
}

.business-name:hover {
  text-decoration: underline;
}

.business-rating {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.review-count {
  color: #666;
  font-size: 13px;
}

.business-categories {
  color: #0073bb;
  font-size: 14px;
  margin-bottom: 15px;
  cursor: pointer;
}

.business-categories:hover {
  text-decoration: underline;
}

.collection-date {
  color: #999999;
  font-size: 13px;
  margin-top: auto;
}

.delete-icon {
  position: absolute;
  top: 15px;
  right: 15px;
  cursor: pointer;
}

:deep(.el-rate__icon) {
  font-size: 14px !important;
  margin-right: 1px;
}

@media (max-width: 768px) {
  .initials-avatar {
    font-size: 32px;
  }

  .collections-container {
    padding: 15px;
  }

  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    margin-bottom: 20px;
  }

  .collection-card {
    flex-direction: column;
  }

  .collection-image {
    flex: 0 0 150px;
  }

  .collection-info {
    padding: 15px;
  }

  .business-rating {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .delete-icon {
    position: absolute;
    top: 10px;
    right: 10px;
  }
}

@media (max-width: 480px) {
  .initials-avatar {
    font-size: 28px;
  }
  
  .collections-container {
    padding: 10px;
  }

  .collection-image {
    flex: 0 0 120px;
  }

  .business-name {
    font-size: 16px;
  }
}
</style>