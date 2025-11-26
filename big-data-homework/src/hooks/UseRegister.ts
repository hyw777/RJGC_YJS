import {ref} from "vue";
import axios from "@/interceptor/Axios";

export default () => {
    let signUpData = ref({
        "name": "",
        "password": "",
        "gender": "",
        "age": "",
        "city": "",
        "email": "",
        "code": ""
    })

    function register(form): void {
        signUpData.value = form
        axios.post("api/user/register", signUpData.value).then(
            (response) => {
                ElMessage({
                message: response.data.msg,
                type: 'success',
            })
                ElMessage({
                    message: response.data.data,
                    type: 'success',
                })
            }
        ).catch((error) => {
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }

    return {signUpData,register}
}