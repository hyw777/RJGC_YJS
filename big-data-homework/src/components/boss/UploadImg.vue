<template>
  <div class="uploadImg">
    <div class="top-box">
      <div class="title">Image Management</div>
      <div class="upload-area" @click="triggerFileInput">
        <input 
          type="file" 
          ref="fileInput" 
          accept="image/*" 
          @change="onFileChange" 
          style="display: none;" 
          multiple
        />
        <div v-if="!previewUrls.length" class="upload-placeholder">
          <el-icon size="48" color="#8c8c8c"><Plus /></el-icon>
          <p>点击选择图片或拖拽图片到此处</p>
          <p class="hint">支持 JPG、PNG、WEBP 格式</p>
        </div>
        <div v-else class="preview-container">
          <div 
            v-for="(preview, index) in previewUrls" 
            :key="index" 
            class="preview-item"
          >
            <img :src="preview" alt="preview" />
            <el-icon 
              size="20" 
              color="#fff" 
              class="remove-icon"
              @click.stop="removePreview(index)"
            >
              <CircleClose />
            </el-icon>
          </div>
        </div>
      </div>
      
      <div class="upload-controls">
        <el-button @click="resetUpload">清空</el-button>
        <el-button 
          type="primary" 
          @click="submitUpload"
          :disabled="!previewUrls.length"
        >
          上传图片
        </el-button>
      </div>
    </div>
    
    <div class="img-container">
      <div 
        class="image-wrapper" 
        v-for="(file, index) in fileList" 
        :key="index"
        v-show="file && filePath(file)"
      >
        <img :src="filePath(file)" class="img-box">
        <el-icon 
          size="50" 
          color="#E00707" 
          class="icon" 
          @click="deleteImg(file, baseInfo.bid)"
        >
          <DeleteFilled />
        </el-icon>
      </div>
    </div>
    
    <!-- 上传进度弹窗 -->
    <el-dialog
      v-model="uploadProgressVisible"
      title="上传进度"
      width="500"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="progress-container">
        <el-progress 
          :percentage="uploadProgress" 
          :stroke-width="18" 
          striped 
          striped-flow
        />
        <p class="progress-text">{{ progressText }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { UseButtonStore } from '@/stores/UseButtonStore';
import { DeleteFilled, CircleClose, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useBaseInfo } from '@/hooks/UseBaseInfo';
import axios from 'axios';
import { useAuthStore } from '@/stores/UseAuthStore';
import { useRoute } from 'vue-router'; // 添加导入

// 定义 props
const props = defineProps<{
  businessId?: string
}>()

const imageUrl = ref('');
const authStore = useAuthStore();
const { baseInfo, getBaseInfo } = useBaseInfo(); // 修复：不再使用 toRefs 包装自定义 hook 返回的对象
const fileList = ref<string[]>([]); // 明确指定类型
const fileInput = ref<HTMLInputElement | null>(null);
const previewUrls = ref<string[]>([]);
const uploadFiles = ref<File[]>([]);
const uploadProgressVisible = ref(false);
const uploadProgress = ref(0);
const progressText = ref('准备上传...');

// 获取路由参数
const route = useRoute();

// 抽取加载数据的函数，方便复用
const loadData = async (businessId: string) => {
  try {
    await getBaseInfo(businessId); // 修复：直接调用函数而不是 .value()
    fileList.value = [...baseInfo.value.imageList]; // 修复类型问题
    console.log(fileList.value)
  } catch (error) {
    console.error("图片列表加载失败:", error);
  }
};

// 监听路由变化，在切换标签时重新加载数据
watch(
  () => props.businessId || route.query.businessId,
  (newBusinessId) => {
    if (newBusinessId) {
      loadData(newBusinessId as string);
    }
  },
  { immediate: true }
);

// 处理上传成功的回调
const handleSuccess = (response: any) => {
  console.log(response.data)
  fileList.value.push(response.data)
};

const filePath = (file: string | null) => {
   if (!file) {
    console.log("图片文件名为空");
    return "";
  }
 
  const isHttpUrl = file.includes("http");

  if (isHttpUrl) {
    return file;
  } else {
    const fullPath = `http://localhost:3000/images/${file}.jpg`;
     console.log("图片文件名:", fullPath);
    return fullPath;
  }
}

function triggerFileInput() {
  if (fileInput.value) {
    fileInput.value.click();
  }
}

function onFileChange(e: Event) {
  const input = e.target as HTMLInputElement;
  const files = input.files;
  
  if (files && files.length > 0) {
    // 清空之前的预览
    previewUrls.value = [];
    uploadFiles.value = [];
    
    // 处理每个选择的文件
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      // 检查文件类型
      if (!file.type.startsWith('image/')) {
        ElMessage({
          message: '请选择图片文件',
          type: 'warning'
        });
        continue;
      }
      
      // 添加到待上传文件列表
      uploadFiles.value.push(file);
      
      // 创建预览URL
      const previewUrl = URL.createObjectURL(file);
      previewUrls.value.push(previewUrl);
    }
  }
}

function removePreview(index: number) {
  // 移除预览URL
  URL.revokeObjectURL(previewUrls.value[index]);
  previewUrls.value.splice(index, 1);
  uploadFiles.value.splice(index, 1);
}

function resetUpload() {
  // 清空预览
  previewUrls.value.forEach(url => URL.revokeObjectURL(url));
  previewUrls.value = [];
  uploadFiles.value = [];
  
  // 清空文件输入框
  if (fileInput.value) {
    fileInput.value.value = '';
  }
}

async function submitUpload() {
  if (uploadFiles.value.length === 0) {
    ElMessage({ message: '请选择图片后再上传', type: 'warning' });
    return;
  }

  uploadProgressVisible.value = true;
  uploadProgress.value = 0;
  progressText.value = '准备上传...';

  try {
    // 逐个上传文件
    for (let i = 0; i < uploadFiles.value.length; i++) {
      let file = uploadFiles.value[i];
      
      // 检查文件是否为jpg格式，如果不是则转换
      if (!file.type.includes('jpeg') && !file.type.includes('jpg')) {
        progressText.value = `正在转换图片格式 (${i + 1}/${uploadFiles.value.length}): ${file.name}`;
        file = await convertToJpg(file);
      }
      
      // 更新进度文本
      progressText.value = `正在上传 (${i + 1}/${uploadFiles.value.length}): ${file.name}`;
      
      // 创建表单数据
      const formData = new FormData();
      formData.append('file', file);

      // 上传图片文件
      const resp = await axios.post('http://localhost:3000/photoData/images', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent) => {
          if (progressEvent.total) {
            const percentCompleted = Math.round(
              (progressEvent.loaded * 100) / progressEvent.total
            );
            // 计算整体进度
            const overallProgress = Math.round(
              ((i * 100) + percentCompleted) / uploadFiles.value.length
            );
            uploadProgress.value = overallProgress;
          }
        }
      });

      // 获取上传后的文件信息
      const uploadedFileInfo = resp.data.files[0]; // 假设一次只上传一个文件
      
      // 去掉文件扩展名
      const filenameWithoutExtension = removeFileExtension(uploadedFileInfo.filename);
      
      // 调用API将文件名和商户ID关联
      const params = new FormData();
      params.append('filePath', filenameWithoutExtension);
      params.append('bId', baseInfo.value.bid);
      console.log(baseInfo.value.bid)
      await axios.post('/api/image/uploadImage2', params);

      // 更新进度
      uploadProgress.value = Math.round(((i + 1) * 100) / uploadFiles.value.length);
    }

    ElMessage({ message: '上传成功', type: 'success' });
    
    // 重新加载图片列表
    await getBaseInfo(baseInfo.value.bid); // 修复：直接调用函数而不是 .value()
    fileList.value = [...baseInfo.value.imageList]; // 修复类型问题

    // 清空文件列表和预览
    uploadFiles.value = [];
    previewUrls.value.forEach(url => URL.revokeObjectURL(url));
    previewUrls.value = [];
    
    // 清空文件输入框
    if (fileInput.value) {
      fileInput.value.value = '';
    }
  } catch (err: any) { // 修复类型问题
    console.error('上传失败:', err);
    ElMessage({ 
      message: '上传失败: ' + (err.response?.data?.message || err.message), 
      type: 'error' 
    });
  } finally {
    // 关闭进度弹窗
    setTimeout(() => {
      uploadProgressVisible.value = false;
      uploadProgress.value = 0;
      progressText.value = '准备上传...';
    }, 500);
  }
}

let buttonStore = UseButtonStore();

async function deleteImg(fileName: string, businessId: string) {
  console.log(fileName + ' ' + businessId);
  
  try {
    await ElMessageBox.confirm(
      '确定要删除这张图片吗？此操作无法撤销。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    
    let formData = new FormData()
    formData.append('fileName', fileName)
    formData.append('businessId', businessId)
    
    let res = await axios.delete('/api/image/deleteImage', {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Token': authStore.token
      },
      data: formData
    })
    
    let index = fileList.value.findIndex((element => element === fileName))
    fileList.value.splice(index, 1);
    
    ElMessage({
      message: res.data.data,
      type: 'success',
    })
  } catch (err: any) { // 修复类型问题
    if (err !== 'cancel') {
      console.log(err)
      ElMessage({
        message: '删除失败: ' + (err.message || '未知错误'),
        type: 'error',
      })
    }
  }
}

onMounted(async () => {
  buttonStore.setBossButton(4);
  // 从props或路由参数中获取businessId
  const businessId = props.businessId || route.query.businessId as string;
  if (businessId) {
    await loadData(businessId);
  }
});

// 添加辅助函数：移除文件扩展名
function removeFileExtension(filename: string): string {
  const lastDotIndex = filename.lastIndexOf('.');
  if (lastDotIndex !== -1) {
    return filename.substring(0, lastDotIndex);
  }
  return filename;
}

// 添加辅助函数：将图片转换为jpg格式
function convertToJpg(file: File): Promise<File> {
  return new Promise((resolve, reject) => {
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    const img = new Image();
    
    img.onload = () => {
      canvas.width = img.width;
      canvas.height = img.height;
      
      if (ctx) {
        ctx.drawImage(img, 0, 0);
        
        canvas.toBlob(
          (blob) => {
            if (blob) {
              const jpgFile = new File([blob], file.name.replace(/\.[^/.]+$/, '.jpg'), {
                type: 'image/jpeg',
                lastModified: Date.now(),
              });
              resolve(jpgFile);
            } else {
              reject(new Error('无法转换为JPEG格式'));
            }
          },
          'image/jpeg',
          0.9
        );
      } else {
        reject(new Error('无法创建Canvas上下文'));
      }
    };
    
    img.onerror = () => {
      reject(new Error('无法加载图片'));
    };
    
    const reader = new FileReader();
    reader.onload = (e) => {
      img.src = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  });
}
</script>

<style scoped>
.uploadImg {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.top-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
  width: 100%;
}

.upload-area {
  width: 100%;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.upload-area:hover {
  border-color: #409eff;
  background-color: #f0f8ff;
}

.upload-placeholder {
  color: #8c8c8c;
}

.upload-placeholder .hint {
  font-size: 12px;
  margin-top: 8px;
}

.preview-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
  width: 100%;
}

.preview-item {
  position: relative;
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-icon {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  padding: 2px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.remove-icon:hover {
  background-color: rgba(0, 0, 0, 0.8);
  transform: scale(1.1);
}

.upload-controls {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
}

.progress-container {
  padding: 20px;
}

.progress-text {
  text-align: center;
  margin-top: 15px;
  color: #606266;
}

.img-container {
  margin-top: 30px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 30px;
  width: 90%;
  margin-left: 7%;
}

.image-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.img-box {
  width: 100%;
  height: 230px;
  object-fit: cover;
  border-radius: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.img-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.icon {
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  cursor: pointer;
  opacity: 0;
  transform: scale(0.8);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.icon:hover {
  background-color: rgba(255, 255, 255, 1);
  transform: scale(1.1);
}

.image-wrapper:hover .icon {
  opacity: 1;
  transform: scale(1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .preview-container {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 10px;
  }
  
  .preview-item {
    height: 100px;
  }
  
  .img-container {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
    width: 100%;
    margin-left: 0;
  }
  
  .img-box {
    height: 150px;
  }
}
</style>