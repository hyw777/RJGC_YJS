import {ref} from "vue";
import axios from "axios";

export default ()=>{
    let friends = ref([])

    function getFriends() {
        axios.get('/api/friend/my').then((response) => {
            friends.value = response.data.data;
        })
    }

    function deleteFriend (uid) {
        axios.delete(`/api/friend/delete/${uid}`).then((response) => {
            ElMessage({
                message: '删除成功',
                type: 'success',
            })
            location.reload()
        }).catch((error)=>{
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }

    return {friends, getFriends,deleteFriend};
}