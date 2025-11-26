import axios from "axios";
import {ref} from "vue";

export function useActivity() {
    const businesses = ref([])

    async function getNearByBusinesses() {
        try {
            const res = await axios.get('/api/business/nearby')
            if (res.data.data.length > 6) {
                businesses.value = res.data.data.slice(0, 6)
            }else{
                businesses.value = res.data.data
            }
        } catch (e) {
            console.log(e)
        }
    }

    return {businesses, getNearByBusinesses}
}