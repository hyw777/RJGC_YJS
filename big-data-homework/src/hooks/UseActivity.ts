import axios from "axios";
import {ref} from "vue";

export function useActivity() {
    const businesses = ref([])

    async function getNearByBusinesses(latitude?: number, longitude?: number) {
        try {
            // 根据是否提供了位置信息决定请求参数
            const params = latitude && longitude 
                ? { lat: latitude, lng: longitude }
                : {};
                
            const res = await axios.get('/api/business/nearby', { params })
            if (res.data.data.length > 6) {
                businesses.value = res.data.data.slice(0, 6)
            } else {
                businesses.value = res.data.data
            }
            console.log('从后端获取的商户信息:', businesses.value); // 添加日志输出
            return businesses.value; // 返回获取的数据
        } catch (e) {
            console.log(e)
        }
    }

    return { businesses, getNearByBusinesses }
}