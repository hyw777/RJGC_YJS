<template>
  <div class="uploadImg">
    <div class="top-box">
      <div class="title">Image Management</div>
      <el-upload
          :on-success="handleSuccess"
          ref="uploadRef"
          class="upload-demo"
          :auto-upload="false"
          action="/api/image/uploadImage"
          :data="{
            'bId':baseInfo.bid
          }"
          :headers="{
            'Token': authStore.token
          }"
      >
        <template #trigger>
          <el-button type="primary">Select File</el-button>
        </template>

        <div class="upload-controls">
          <el-button type="success" @click="submitUpload">
            Upload to Server
          </el-button>
        </div>
      </el-upload>
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
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, toRefs} from 'vue';
import {UseButtonStore} from '@/stores/UseButtonStore';
import {DeleteFilled} from '@element-plus/icons-vue';
import {ElMessage, type UploadInstance} from 'element-plus';
import {useBaseInfo} from '@/hooks/UseBaseInfo';
import axios from 'axios';
import {useAuthStore} from '@/stores/UseAuthStore';

const imageUrl = ref('');

let authStore = useAuthStore();

let {baseInfo, getBaseInfo} = toRefs(useBaseInfo());

const uploadRef = ref<UploadInstance>();
const fileList = ref([]);

// 处理上传成功的回调
const handleSuccess = (response: any) => {
  console.log(response.data)
  fileList.value.push(response.data)
};


const filePath  =(file: string | null) => {
    // 添加空值检查以防止 Cannot read properties of null 错误
    if (!file || typeof file !== 'string') return '';
    return file.includes('http') ? file : `/api/images/${file}`;
}

const submitUpload = () => {
  
  uploadRef.value!.submit()
}

let buttonStore = UseButtonStore();

async function deleteImg(fileName: string, businessId: string) {
  console.log(fileName + ' ' + businessId);
  let formData = new FormData()
  formData.append('fileName', fileName)
  formData.append('businessId', businessId)
  try {
    let res = await axios.delete('/api/image/deleteImage', {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      data: formData
    })
    let index = fileList.value.findIndex((element => element === fileName))
    fileList.value.splice(index, 1);
    ElMessage({
                message: res.data.data,
                type: 'success',
            })
  } catch (err) {
    console.log(err)
  }
}


onMounted(async () => {
  buttonStore.setBossButton(4);
  await getBaseInfo.value();
  fileList.value = baseInfo.value.imageList;
  console.log(fileList.value)
});
</script>

<style scoped>
.upload-demo {
  margin-top: 20px;
  width: 100%;
}

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

.upload-controls {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
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
</style>