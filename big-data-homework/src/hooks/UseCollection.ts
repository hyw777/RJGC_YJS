import {ref} from "vue";
import axios from "axios";
import {router} from "@/router";

export default ()=> {
    let collections = ref([])

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

    function getCollections() {
        axios.get('/api/collect/myCollect').then((response) => {
            collections.value = response.data.data;
            collections.value.forEach((collection)=>{
                collection.date = formatDateTime(collection.date);
            })
        }).catch((error) => {
            console.log(error);
        })
    }
    
    function cancel(businessId) {
        axios.delete(`/api/collect/cancel?businessId=${businessId}`).then((response) => {
            ElMessage({
                message: response.data.data,
                type: 'success',
            });
            location.reload();
        }).catch((error) => {
            ElMessage({
                message: error,
                type: 'error',
            });
        })
    }

    function jump(id) {
        router.push({
            path:'/merchantDetail',
            query:{id:id}
        })
    }

    return {collections, getCollections,cancel,jump};
}