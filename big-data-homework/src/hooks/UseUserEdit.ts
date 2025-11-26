import {computed, type ComputedRef, ref, toRefs} from "vue";
import axios from "axios";
import {router} from "@/router";
import {useAuthStore} from "@/stores/UseAuthStore";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {useEditStore} from "@/stores/UseEditStore";
import useUserInfo from "@/hooks/UseUserInfo";

export default () => {
    let editStore = useEditStore();
    let isEdit = computed(() => editStore.edit) // 是否展示编辑界面
    let {userInfo, getUserInfo} = toRefs(useUserInfo());

    // 编辑数据
    const form = ref({
        name: '',
        age: 0,
        gender: '',
        city: '',
        email: ''
    });

    function editSwitch() {
        editStore.edit = !editStore.edit
    }

    function editProfile() {
        editStore.edit = true
        router.push("/profile/overview")
    }

    function submit() {
        axios.put("/api/user/edit", form.value).then((response) => {
            let authStore = useAuthStore()
            authStore.clearToken()
            authStore.clearUserType()
            ElMessage({
                message: response.data.data,
                type: 'success',
            })
            router.push("/")
        }).catch((error) => {
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }

    async function writeBack(){
        await getUserInfo.value()
        form.value.name = userInfo.value.name
        form.value.age = userInfo.value.age
        form.value.gender = userInfo.value.gender
        form.value.city = userInfo.value.city
        form.value.email = userInfo.value.email
    }

    return {form, isEdit, editSwitch, submit,editProfile,writeBack}
}