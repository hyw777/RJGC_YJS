import axios from "@/interceptor/Axios";

export default ()=>{
    function sendEmail(email){
        axios.get(`/api/user/sendCode/${email}`).then((respond)=>{
            ElMessage({
                message: '发送成功',
                type: 'success',
            })
        }).catch((error)=>{
            ElMessage({
                message: '发送失败',
                type: 'error',
            })
        })
    }
    return {sendEmail}
}