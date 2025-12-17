import {ref} from "vue";
import axios from "axios";
import {UseSearchStore} from "@/stores/UseSearchStore";

export function useSearch() {
    let info = ref('')
    let searchStore = UseSearchStore()
    let search = ref(
        {
            "page": 1,
            "pageSize": 6,
            "businessName": "",
        }
    )

    let search2 = ref(
        {
            "page": 1,
            "pageSize": 6,
            "type":""
        }
    )

    let result = ref({
        "total": 9,
        "records": [
            {
                "businessId": 0,
                "name": "",
                "address": "",
                "stars": 0,
                "reviewCount": 0,
                "isOpen": 0,
                "categories": "",
                "bid": null,
                "image": null
            }
        ]
    })

    async function getResult(page: number, businessName: string, latitude?: number | null, longitude?: number | null, distance: number = 10) {
        search.value.page = page
        search.value.businessName = businessName
        search.value.pageSize = 6
        
        // 创建传递给后端的参数对象
        const requestData = {
            ...search.value
        }
        
        // 如果提供了地理位置信息，则添加到请求数据中
        if (latitude !== null && latitude !== undefined) {
            requestData.latitude = latitude
        }
        
        if (longitude !== null && longitude !== undefined) {
            requestData.longitude = longitude
        }
        
        requestData.distance = distance

        try {
            console.log(requestData)
            let res = await axios.post('/api/business/search', requestData)
            let data = res.data.data;
            searchStore.setResult(data)
        } catch (err) {
            console.error(err)
        }
    }

    async function getCategoriesResult(page,type) {
        search2.value.page = page
        search2.value.type = type
        search2.value.pageSize = 6
        try {

            let res = await axios.post('/api/business/category',search2.value)
            let data = res.data.data; // 直接使用 res.data.data
            // 检查 records 和 image 是否存在
            if (data.records && data.records.image) {
                let file = data.records.image;
                if (typeof file === 'string' && file.includes('http')) {
                    // 如果已经包含 http，则无需更改
                } else {
                    // 否则，拼接 URL
                    data.records.image = `/api/images/${file}`;
                }
            }
            searchStore.setResult(data)
        }catch (err){
            console.error(err)
        }
    }
    async function getTop5BusinessWith5Stars() {
    try {
        const res = await axios.get('/api/business/top5');
        return res.data.data;
    } catch (err) {
        console.error('获取前5个五星商家失败:', err);
        return [];
    }
}
        // AI recommend state and function
        const aiLoading = ref(false)
        const aiRecommendations = ref<string[]>([])

       

    return {search,result,getResult,info,useSearch,getCategoriesResult,search2, aiLoading, aiRecommendations, getTop5BusinessWith5Stars}
}