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
        imageList: [] as string[], // 明确指定类型
        reviewVOList: [] as any[] // 明确指定类型
    })

    function formatDateTime(dateTime: string | number | Date) {
        const date = new Date(dateTime);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

async function getBaseInfo(id?: any) {
    try {
        let res = await axios.get(`/api/business/detail/${id}`)
        
        // 添加数据验证
        if (res.data && res.data.data) {
            baseInfo.value = res.data.data
            
            // 确保 reviewVOList 存在且为数组
            if (baseInfo.value.reviewVOList && Array.isArray(baseInfo.value.reviewVOList)) {
                baseInfo.value.reviewVOList.forEach((review) => {
                    if (review && review.date) {
                        review.date = formatDateTime(review.date);
                    }
                });
            }
            
            // 确保 imageList 存在且为数组
            if (baseInfo.value.imageList && Array.isArray(baseInfo.value.imageList)) {
                let imageList = baseInfo.value.imageList
                for(let i = 0; i < imageList.length; i++) {
                    if (imageList[i]) {
                        let file = imageList[i];
                        if (typeof file === 'string' && file.includes('http')) {
                            // 如果已经包含 http，则无需更改
                        } else {
                            baseInfo.value.imageList[i] = `${file}`;
                        }
                    }
                }
            }
        } else {
            console.warn("API returned empty or invalid data");
        }
        
        console.log(baseInfo.value)
    } catch (e) {
        console.error(e)
    }
}

    return {baseInfo,getBaseInfo}
}