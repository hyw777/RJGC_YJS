import { ref } from "vue";
import axios from "axios";

export function useStores() {
    // 存储商家列表信息
    const businessList = ref<Map<string, string>>(new Map());
    
    // 加载商家列表
    async function loadStores() {
        try {
            const response = await axios.get('/api/business/stores');
            if (response.data.code === 200) {
                // 将返回的数据转换为Map格式
                businessList.value = new Map(Object.entries(response.data.data));
                console.log("加载商家列表成功:", Array.from(businessList.value.entries()));
            } else {
                console.error("加载商家列表失败:", response.data.msg);
            }
        } catch (error) {
            console.error("获取商家列表失败:", error);
        }
    }
    
    // 根据businessId获取商家详情
    async function getBusinessInfo(businessId: string) {
        try {
            const response = await axios.get('/api/business/businessInfo', {
                params: {
                    bId: businessId
                }
            });
            if (response.data.code === 200) {
                return response.data.data;
            } else {
                console.error("获取商家详情失败:", response.data.msg);
                return null;
            }
        } catch (error) {
            console.error("获取商家详情失败:", error);
            return null;
        }
    }
    
    return {
        businessList,
        loadStores,
        getBusinessInfo
    };
}