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

    async function getBaseInfo(businessId?: string) {
        try {
            // 总是使用带参数的接口
            const url = '/api/business/businessInfo';
            
            // 必须提供businessId参数
            if (!businessId) {
                console.warn("未提供businessId参数");
                return;
            }
            
            const response = await axios.get(url, {
                params: { bId: businessId }
            });
            
            // 确保数据存在且不是null
            if (response.data && response.data.data) {
                baseInfo.value = response.data.data;
                
                // 设置bid字段（与businessId相同）
                baseInfo.value.bid = businessId;
                
                // 确保reviewVOList存在且是数组
                if (baseInfo.value.reviewVOList && Array.isArray(baseInfo.value.reviewVOList)) {
                    baseInfo.value.reviewVOList.forEach((review: any) => {
                        review.date = formatDateTime(review.date);
                    });
                } else {
                    // 如果reviewVOList不存在或不是数组，则初始化为空数组
                    baseInfo.value.reviewVOList = [];
                }
                
                // 确保imageList存在且是数组
                if (!baseInfo.value.imageList || !Array.isArray(baseInfo.value.imageList)) {
                    baseInfo.value.imageList = [];
                }
                
                //路径处理
                console.log("baseInfo:"+baseInfo.value.bid)
                let imageList = baseInfo.value.imageList
                for(let i = 0; i < imageList.length; i++) {
                    // 检查 records 和 image 是否存在
                    if (imageList[i]) {
                        let file = imageList[i];
                        if (typeof file === 'string' && file.includes('http')) {
                            // 如果已经包含 http，则无需更改
                        } else {
                            // 否则，拼接 URL
                            baseInfo.value.imageList[i] = `${file}`;
                        }
                    }
                }
            } else {
                console.warn("获取到的商户信息为空");
            }
        } catch (e) {
            console.error("获取商户信息失败:", e);
        }
    }

    return {baseInfo,getBaseInfo}
}