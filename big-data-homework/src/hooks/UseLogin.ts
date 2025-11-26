import {ref} from 'vue';
import type {Ref} from 'vue'
import axios from "axios";

export default () => {
    const userInfo = ref({
        name: '',
        password: '',
        sex: '',
        age: '',
        address: '',
        email: '',
        code: '',
        confirm: ''
    })

    function login(): void {
        axios.post("http://localhost:8080/user/login", {
            "email": userInfo.value.email,
            "password": userInfo.value.password
        }).then((response) => {
            console.log(response.data)
        }).catch((error) => {
            console.log(error.data)
        })
    }

    function register(): void {
        axios.post("http://localhost:8080/user/register", {
                "name": userInfo.value.name,
                "password": userInfo.value.password,
                "sex": userInfo.value.sex,
                "age": userInfo.value.age,
                "address": userInfo.value.address,
                "email": userInfo.value.email,
                "code": userInfo.value.code,
        }).then((response) => {
            console.log(response.data);
        }).catch((error) => {
            console.log(error.data)
        })
    }

    return {userInfo, login}
}