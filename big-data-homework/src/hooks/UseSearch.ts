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

    async function getResult(page,businessName) {
        search.value.page = page
        search.value.businessName = businessName
        search.value.pageSize = 6
        try {
            let res = await axios.post('/api/business/search',search.value)
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

    return {search,result,getResult,info,useSearch,getCategoriesResult,search2}
}