import {ref} from "vue";
import axios from "axios";
import {useBaseInfo} from "@/hooks/UseBaseInfo";

export function useEditMerchant() {
    let info = ref({
        "businessId": 0,
        "name": "",
        "address": "",
        "city": "",
        "state": "",
        "categories": "",
        "hours": "",
        "goodForKids": "",
        "businessAcceptsCreditcards": ""
    })

    let {baseInfo,getBaseInfo} = useBaseInfo()

    function editInfo() {
        axios.put('/api/business/changeInfo',info.value).then((response) => {
            ElMessage({
                message: response.data.data,
                type: 'success',
            })
            writeBcak()
        }).catch((error) => {
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }

    async function writeBcak() {
        await getBaseInfo()
        info.value.businessId = baseInfo.value.businessId
        info.value.name = baseInfo.value.name;
        info.value.address = baseInfo.value.address;
        info.value.city = baseInfo.value.city;
        info.value.state = baseInfo.value.state;
        info.value.categories = baseInfo.value.categories;
        info.value.hours = baseInfo.value.hours;
        info.value.goodForKids = baseInfo.value.goodForKids;
        info.value.businessAcceptsCreditcards = baseInfo.value.businessAcceptsCreditcards;
    }

    return {info,editInfo,writeBcak}
}