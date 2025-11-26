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
          <el-button type="primary">select file</el-button>
        </template>

        <el-button style="margin-left: 15px;" class="ml-3" type="success" @click="submitUpload">
          upload to server
        </el-button>
      </el-upload>
    </div>
    <div class="img-container">
      <div class="image-wrapper" v-for="(file, index) in fileList" :key="index" file="filepath(file)">
        <img :src="filePath(file)" class="img-box">
        <el-icon size="50" color="#E00707" class="icon" @click="deleteImg(file,baseInfo.bid)">
          <DeleteFilled/>
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
const handleSuccess = (response) => {
  console.log(response.data)
  fileList.value.push(response.data)
};


const filePath  =(file) => {

    return  file.includes('http') ? file : `/api/images/${file}`;
}

const submitUpload = () => {
  uploadRef.value!.submit()
}

let buttonStore = UseButtonStore();

async function deleteImg(fileName, businessId) {
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
}

.uploadImg {
  display: flex;
  flex-direction: column;
}

.title {
  display: flex;
  font-size: 40px;
  font-weight: 600;
}

.top-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30%;
  height: 70px;
  background-color: #e00707;
  border-radius: 30px;
  margin-top: 30px;
  cursor: pointer;
}


.button-name {
  font-size: 30px;
  color: #FFFFFF;
  margin-left: 10px;
}

.img-container {
  margin-top: 30px;
  display: flex;
  justify-content: start;
  flex-wrap: wrap;
  width: 90%;
  margin-left: 7%;
}

.image-wrapper {
  position: relative;
}

.img-box {
  display: flex;
  position: relative;
  width: 230px;
  height: 230px;
  margin-bottom: 30px;
  margin-right: 30px;
  transition: filter 0.7s ease; /* 添加滤镜过渡效果 */
}

.img-box:hover {
  filter: brightness(0.8); /* 调整亮度，使图片变暗 */
}

.icon {
  display: none;
  position: absolute;
  left: 34%;
  top: 35%;
  cursor: pointer;
}

.icon:hover {
}

.image-wrapper:hover .icon {
  display: block;
}

.image-wrapper:hover .img-box {
  filter: brightness(0.8); /* 调整亮度，使图片变暗 */
}
</style>