<template>
  <div class="menu-management">
    <!-- 加载指示器 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner">
        <el-icon class="is-loading" size="48"><Loading /></el-icon>
        <p>加载中...</p>
      </div>
    </div>

    <div class="menu-card">
      <h2 class="card-title">菜品管理</h2>
      
      <!-- 添加菜品按钮 -->
      <div class="add-dish-section">
        <el-button 
          type="primary" 
          @click="openAddDishDialog"
          icon="Plus"
        >
          添加新菜品
        </el-button>
      </div>

      <!-- 菜品列表 -->
      <div class="dishes-list">
        <el-table 
          :data="dishes" 
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="dishName" label="菜品名称" />
          <el-table-column label="菜品图片" width="120">
            <template #default="scope">
              <div class="dish-image-container">
                <img 
                  v-if="scope.row.photoId" 
                  :src="getImagePath(scope.row.photoId)" 
                  :alt="scope.row.dishName"
                  class="dish-image"
                />
                <div v-else class="no-image">无图片</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="价格" width="120">
            <template #default="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="isOnSale" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.isOnSale ? 'success' : 'info'">
                {{ scope.row.isOnSale ? '在售' : '已下架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button 
                size="small" 
                @click="openEditDishDialog(scope.row)"
                type="primary"
                link
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                @click="toggleDishStatus(scope.row)"
                :type="scope.row.isOnSale ? 'warning' : 'success'"
                link
              >
                {{ scope.row.isOnSale ? '下架' : '上架' }}
              </el-button>
              <el-button 
                size="small" 
                @click="deleteDish(scope.row)"
                type="danger"
                link
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 添加/编辑菜品对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      :before-close="handleDialogClose"
    >
      <el-form 
        :model="currentDish" 
        :rules="dishRules" 
        ref="dishFormRef"
        label-width="80px"
      >
        <el-form-item label="菜品名称" prop="dishName">
          <el-input v-model="currentDish.dishName" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number 
            v-model="currentDish.price" 
            :precision="2" 
            :step="0.1" 
            :min="0"
          />
        </el-form-item>
        <el-form-item label="菜品图片">
          <div class="image-upload-container">
            <div 
              class="image-preview" 
              @click="triggerFileInput"
            >
              <input 
                ref="fileInput"
                type="file" 
                accept="image/*" 
                @change="handleFileChange" 
                style="display: none;"
              />
              <div v-if="previewImageUrl" class="preview-wrapper">
                <img :src="previewImageUrl" alt="预览图片" class="preview-image" />
                <el-icon 
                  class="remove-image"
                  @click.stop="removeImage"
                >
                  <CircleClose />
                </el-icon>
              </div>
              <div v-else class="upload-placeholder">
                <el-icon><Plus /></el-icon>
                <span>点击上传图片</span>
              </div>
            </div>
            <div class="image-info" v-if="currentDish.photoId">
              <span>当前图片ID: {{ currentDish.photoId }}</span>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="saveDish"
            :loading="saving"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Loading, Plus, CircleClose } from '@element-plus/icons-vue';
import {UseButtonStore} from "@/stores/UseButtonStore";
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
// 定义菜品类型
interface Dish {
  id: number;
  businessId: number;
  dishName: string;
  price: number;
  photoId: string;
  isOnSale: boolean;
}

// 定义 props
const props = defineProps<{
  businessId?: number
}>()

// 响应式数据
const loading = ref(false);
const saving = ref(false);
const dishes = ref<Dish[]>([]);
const dialogVisible = ref(false);
const isEditMode = ref(false);
const route = useRoute();
const fileInput = ref<HTMLInputElement | null>(null);
const previewImageUrl = ref<string>('');
const imageFile = ref<File | null>(null);

// 当前编辑的菜品
const currentDish = ref<Dish>({
  id: 0,
  businessId: 0,
  dishName: '',
  price: 0,
  photoId: '',
  isOnSale: true
});

// 表单验证规则
const dishRules = {
  dishName: [
    { required: true, message: '请输入菜品名称', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ]
};

// 对话框标题
const dialogTitle = computed(() => {
  return isEditMode.value ? '编辑菜品' : '添加菜品';
});

// 获取商户ID（优先使用props，其次使用路由参数）
const businessId = computed<number>(() => {
  // 优先使用 props
  if (props.businessId !== undefined && props.businessId !== null) {
    return Number(props.businessId);
  }
  
  // 其次从路由参数获取
  const routeBusinessId = route.query.businessId;
  if (routeBusinessId) {
    const parsedId = parseInt(routeBusinessId as string, 10);
    return isNaN(parsedId) ? 0 : parsedId;
  }
  
  return 0;
});

// 获取图片路径
const getImagePath = (photoId: string) => {
  if (!photoId) return '';
  // 根据实际服务器地址调整路径
  return `http://localhost:3000/images/${photoId}.jpg`;
};

// 触发文件选择
const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

const convertToJpg = (file: File): Promise<Blob> => {
  return new Promise((resolve, reject) => {
    const img = new Image();
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    
    img.onload = () => {
      canvas.width = img.width;
      canvas.height = img.height;
      
      if (ctx) {
        ctx.drawImage(img, 0, 0);
        canvas.toBlob(resolve, 'image/jpeg', 0.9);
      } else {
        reject(new Error('无法获取 canvas 上下文'));
      }
    };
    
    img.onerror = reject;
    img.src = URL.createObjectURL(file);
  });
};

// 处理文件选择
const handleFileChange = async (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    let file = input.files[0];
    
    // 检查文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.warning('请选择图片文件');
      return;
    }
    
    // 如果不是 JPG 格式，则转换为 JPG
    if (!file.type.includes('jpeg') && !file.type.includes('jpg')) {
      try {
        const jpgBlob = await convertToJpg(file);
        file = new File([jpgBlob], 
          file.name.replace(/\.[^/.]+$/, '.jpg'),
          { type: 'image/jpeg' }
        );
      } catch (error) {
        ElMessage.error('图片转换失败');
        return;
      }
    }
    
    imageFile.value = file;
    
    // 创建预览URL
    previewImageUrl.value = URL.createObjectURL(file);
  }
};
// 移除图片
const removeImage = () => {
  previewImageUrl.value = '';
  imageFile.value = null;
  
  // 清空文件输入框
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

// 获取菜品列表
const fetchDishes = async () => {
  const bizId = Number(businessId.value);
  console.log('businessId:', bizId);
  console.log('businessId type:', typeof bizId);
  
  // 确保 businessId 是有效数字
  if (!bizId || bizId <= 0 || isNaN(bizId)) {
    ElMessage.warning('未获取到有效商户信息');
    return;
  }
  
  loading.value = true;
  try {
    // 使用确保是数字类型的 bizId
    const response = await axios.get(`/api/dish/list?businessId=${bizId}`);
    dishes.value = response.data.data || [];
  } catch (error: any) {
    console.error('获取菜品列表失败:', error);
    if (error.response?.status === 401 || error.response?.status === 403) {
      console.log('认证失败，可能是 businessId 无效导致');
    }
    ElMessage.error('获取菜品列表失败');
  } finally {
    loading.value = false;
  }
};

// 打开添加菜品对话框
const openAddDishDialog = () => {
  isEditMode.value = false;
  currentDish.value = {
    id: 0,
    businessId: businessId.value,
    dishName: '',
    price: 0,
    photoId: '',
    isOnSale: true
  };
  // 重置图片相关状态
  previewImageUrl.value = '';
  imageFile.value = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
  dialogVisible.value = true;
};

// 打开编辑菜品对话框
const openEditDishDialog = (dish: Dish) => {
  isEditMode.value = true;
  currentDish.value = { ...dish };
  // 设置预览图片
  if (dish.photoId) {
    previewImageUrl.value = getImagePath(dish.photoId);
  } else {
    previewImageUrl.value = '';
  }
  imageFile.value = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
  dialogVisible.value = true;
};

// 上传图片并获取图片ID
const uploadImage = async (): Promise<string> => {
  if (!imageFile.value) return '';
  
  // 生成唯一图片ID
  const photoId = generateUniqueId();
  
  const formData = new FormData();
  formData.append('file', imageFile.value);
  formData.append('imageId', photoId); // 传递图片ID给后端
  
  try {
    // 上传图片到服务器
    const response = await axios.post('http://localhost:3000/photoData/images', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    // 将图片与商户关联
    const params = new FormData();
    params.append('filePath', photoId);
    params.append('bId', String(currentDish.value.businessId));
    await axios.post('/api/image/uploadImage2', params);
    
    return photoId;
  } catch (error) {
    console.error('图片上传失败:', error);
    throw new Error('图片上传失败');
  }
};
const generateUniqueId = (): string => {
  return uuidv4().replace(/-/g, '').substring(0, 16); // 生成16位ID
};
// 保存菜品（添加或更新）
const saveDish = async () => {
  if (!currentDish.value.dishName || currentDish.value.price <= 0) {
    ElMessage.warning('请填写完整的菜品信息');
    return;
  }

  // 确保 businessId 被设置
  if (!currentDish.value.businessId) {
    currentDish.value.businessId = businessId.value;
  }

  saving.value = true;
  try {
    // 保存原始的 photoId 用于判断是否需要删除
    const oldPhotoId = isEditMode.value ? currentDish.value.photoId : '';
    
    // 如果有新图片需要上传
    if (imageFile.value) {
      const photoId = await uploadImage();
      currentDish.value.photoId = photoId;
      
      // 如果是编辑模式且有旧图片，则删除旧图片
      if (isEditMode.value && oldPhotoId) {
        try {
          await deleteImage(oldPhotoId, String(currentDish.value.businessId));
        } catch (error) {
          console.warn('删除旧图片失败:', error);
        }
      }
    }
    
    if (isEditMode.value) {
      // 更新菜品
      await axios.put('/api/dish/update', currentDish.value);
      ElMessage.success('菜品更新成功');
    } else {
      // 添加菜品
      await axios.post('/api/dish/add', currentDish.value);
      ElMessage.success('菜品添加成功');
    }
    
    dialogVisible.value = false;
    await fetchDishes();
  } catch (error) {
    console.error('保存菜品失败:', error);
    ElMessage.error(isEditMode.value ? '菜品更新失败' : '菜品添加失败');
  } finally {
    saving.value = false;
  }
};

// 删除图片
const deleteImage = async (fileName: string, businessId: string) => {
  try {
    await axios.delete('/api/image/deleteImage', {
      params: {
        fileName: fileName,
        businessId: businessId
      }
    });
  } catch (error) {
    console.error('删除图片失败:', error);
    throw new Error('图片删除失败');
  }
};
// 切换菜品状态（上架/下架）
const toggleDishStatus = async (dish: Dish) => {
  try {
    await ElMessageBox.confirm(
      `确定要${dish.isOnSale ? '下架' : '上架'} "${dish.dishName}" 吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }
    );
    
    const updatedDish = { ...dish, isOnSale: !dish.isOnSale };
    await axios.put('/api/dish/update', updatedDish);
    ElMessage.success(`${dish.isOnSale ? '下架' : '上架'}成功`);
    await fetchDishes();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('切换菜品状态失败:', error);
      ElMessage.error('操作失败');
    }
  }
};

// 删除菜品
const deleteDish = async (dish: Dish) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${dish.dishName}" 吗？此操作不可恢复！`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error',
        center: true
      }
    );
    
    // 如果菜品有关联图片，先删除图片
    if (dish.photoId) {
      try {
        await deleteImage(dish.photoId, String(dish.businessId));
      } catch (error) {
        console.warn('删除菜品图片失败:', error);
      }
    }
    
    // 删除菜品记录
    await axios.delete(`/api/dish/delete?id=${dish.id}`);
    ElMessage.success('删除成功');
    await fetchDishes();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除菜品失败:', error);
      ElMessage.error('删除失败');
    }
  }
};

// 关闭对话框
const handleDialogClose = (done: Function) => {
  ElMessageBox.confirm('确定要关闭吗？未保存的数据将会丢失')
    .then(() => {
      
      done();
    })
    .catch(() => {
      // 取消关闭
    });
};

// 监听 businessId 变化
watch(businessId, (newBusinessId) => {
  if (newBusinessId) {
    fetchDishes();
  }
});

let buttonStore = UseButtonStore();
// 组件挂载时获取数据
onMounted(() => {
    buttonStore.setBossButton(2);
  if (businessId.value) {
    fetchDishes();
  }
});
</script>

<style scoped>
.el-message-box {
  position: fixed !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
  margin: 0 !important;
}

.menu-management {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100%;
  width: 100%;
  box-sizing: border-box;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-spinner {
  text-align: center;
}

.loading-spinner p {
  margin-top: 16px;
  color: #606266;
  font-size: 16px;
}

.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.menu-card {
  width: 100%;
  max-width: 1200px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
  transition: box-shadow 0.3s ease;
  box-sizing: border-box;
}

.menu-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  color: #343a40;
  margin: 0 0 32px 0;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
  text-align: center;
}

.add-dish-section {
  margin-bottom: 24px;
  text-align: right;
}

.dishes-list {
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dish-image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
}

.dish-image {
  max-width: 80px;
  max-height: 60px;
  border-radius: 4px;
  object-fit: cover;
}

.no-image {
  color: #999;
  font-size: 12px;
}

.image-upload-container {
  width: 100%;
}

.image-preview {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  padding: 16px;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.image-preview:hover {
  border-color: #409eff;
}

.upload-placeholder {
  color: #999;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-placeholder .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.preview-wrapper {
  position: relative;
  display: inline-block;
}

.preview-image {
  max-width: 100%;
  max-height: 200px;
  display: block;
  margin: 0 auto;
}

.remove-image {
  position: absolute;
  top: -8px;
  right: -8px;
  background: #fff;
  border-radius: 50%;
  cursor: pointer;
  width: 20px;
  height: 20px;
  color: #ff4d4f;
}

.image-info {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .menu-management {
    padding: 10px;
  }
  
  .menu-card {
    padding: 20px;
    max-width: 100%;
  }
  
  .add-dish-section {
    text-align: center;
  }
}

:deep(.el-dialog) {
  position: absolute !important;
  top: 50% !important;
  left: 50% !important;
  transform: translate(-50%, -50%) !important;
}

:deep(.el-overlay) {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
}

</style>