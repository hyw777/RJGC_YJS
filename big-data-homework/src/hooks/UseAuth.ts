import axios from "@/interceptor/Axios";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/UseAuthStore";
import { ref } from "vue";
import { useAddFriends } from "@/hooks/UseAddFriends";

export const useAuth = function() {
    const authStore = useAuthStore();
    const router = useRouter();
    const { notifications, getNotifications, applyInfo, getApplyInfo } = useAddFriends();
    let loginVisible = ref(false);
    // 用户登录
    let loginData = ref({
        "email": "",
        "password": ""
    });
    let check = ref(false);

    async function login(form) {
        try {
            const response = await axios.post('api/user/login', {
                email: form.email,
                password: form.password,
            });
            if (response.data && response.data.data && response.data.data.token) {
                // 将 token 存储在 store 中
                authStore.setToken(response.data.data.token);
                authStore.setUserType(response.data.data.userType);
                authStore.setUserName(response.data.data.userName);
                authStore.setEmail(form.email);
                if (check.value === true) {
                    authStore.setIsRemember();
                    authStore.setPassword(form.password);
                } else {
                    authStore.clearIsRemember();
                    authStore.clearPassword();
                }

                location.reload()

                router.push('/nearby');
                ElMessage.success('登录成功');
            } else {
                ElMessage({
                    message: response.data.msg,
                    type: 'error',
                });
            }
        } catch (error) {
            ElMessage({
                message: '登录失败',
                type: 'error',
            });
        }
    }

    async function logout() {
        try {
            const res = await axios.get('api/user/logout');
            authStore.clearToken();
            authStore.clearUserType();
            ElMessage({
                message: '登出成功',
                type: 'success',
            });
            router.push('/');
        } catch (err) {
            ElMessage({
                message: err,
                type: 'error',
            });
        }
    }

    function writeBack() {
        loginVisible.value = true;
        loginData.value.email = authStore.email;
        if (authStore.isRemember === true) {
            check.value = true;
            loginData.value.password = authStore.password;
        } else {
            check.value = false;
            loginData.value.password = '';
        }
    }

    return { login, logout, loginData, loginVisible, writeBack, check };
};
