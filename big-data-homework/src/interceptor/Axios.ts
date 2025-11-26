import axios from 'axios';
import {router} from '@/router';
import {useAuthStore} from '@/stores/UseAuthStore';


axios.interceptors.request.use(
    (config) => {
        const authStore = useAuthStore();
        const token = authStore.token;
        if (token) {
            config.headers['token'] = `${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    (response) => {
        if (response && response.data.code == 401) {
            const authStore = useAuthStore();
            authStore.clearToken();
            authStore.clearUserType()
            ElMessage({
                message: response.data.msg,
                type: 'success',
            });
            router.push('/');
        }
        return response;
    }
);

export default axios;
