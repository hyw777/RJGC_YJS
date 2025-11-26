import {ref} from "vue";
import axios from "axios";

export function useBaseInfo() {
    let baseInfo = ref({
        businessId: 1,
        name: '',
        address: "",
        city: "",
        state: "",
        stars: 0,
        reviewCount: 1,
        isOpen: 0,
        categories: "",
        hours: "",
        goodForKids: "",
        businessAcceptsCreditcards: "",
        bid: "",
        imageList: [],
        reviewVOList: []
    })

    function formatDateTime(dateTime) {
        const date = new Date(dateTime);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    async function getBaseInfo() {
        try {
            const response = await axios.get('/api/business/baseInfo');
            baseInfo.value = response.data.data;
            baseInfo.value.reviewVOList.forEach((review) => {
                review.date = formatDateTime(review.date);
            });
            //路径处理
            // baseInfo.value.imageList.map(file => {
            //     return file.includes('http') ? file : `/api/images/${file}`;
            // });
            // for(let i = 0;i<baseInfo.value.imageList.length;i++){
            //     baseInfo.value.imageList[i] = baseInfo.value.imageList[i].includes('http') ? baseInfo.value.imageList[i] : `/api/images/${baseInfo.value.imageList[i]}`;
            // }
        } catch (error) {
            console.error(error);
        }
    }

    return {baseInfo,getBaseInfo}
}