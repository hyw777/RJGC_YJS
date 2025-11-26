import {ref} from "vue";
import axios from "axios";

export default ()=>{
    let findData = ref({
        email : '',
        code : '',
        newPassword : ''
    })

    function findPassword(findata){
        axios.post('/api/user/findUser',findata).then(res=>{
            ElMessage({
                message: res.data.data,
                type: 'success',
            })
        }).catch((error)=>{
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }

    return {findData,findPassword}
}