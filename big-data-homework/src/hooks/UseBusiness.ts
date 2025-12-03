import {ref} from "vue";
import axios from "axios";

export function useBusiness() {
    let result = ref({
        businessId: 1,
        name: "",
        address: "",
        city: "",
        state: "",
        stars: 0,
        reviewCount: 0,
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

    async function getResult(id) {
        try {
            let res = await axios.get(`/api/business/detail/${id}`)
            result.value = res.data.data
            result.value.reviewVOList.forEach((result) => {
                result.date = formatDateTime(result.date);
            });
            let imageList = result.value.imageList
            for(let i = 0; i < imageList.length; i++) {
                // 检查 records 和 image 是否存在
                if (imageList[i]) {
                    let file = imageList[i];
                    if (typeof file === 'string' && file.includes('http')) {
                        // 如果已经包含 http，则无需更改
                    } else {
                        // 否则，拼接 URL
                        result.value.imageList[i] = `${file}`;
                    }
                }
            }
            console.log(result.value)
        }catch (e){
            console.error(e)
        }
    }

    return {result,getResult}
}