import { defineStore } from 'pinia';

export const useAuthStore = defineStore({
    id: 'main',
    state: () => ({
        token: null,
        userType: null,
        email: '',
        password: '',
        isRemember: false,
    }),
    actions: {
        setToken(token) {
            this.token = token;
        },
        clearToken() {
            this.token = null;
        },
        setUserType(userType) {
            this.userType = userType;
        },
        clearUserType() {
            this.userType = null;
        },
        setEmail(email) {
            this.email = email;
        },
        clearEmail() {
            this.email = '';
        },
        setPassword(password) {
            this.password = password;
        },
        clearPassword() {
            this.password = '';
        },
        setIsRemember() {
            this.isRemember = true;
        },
        clearIsRemember() {
            this.isRemember = false;
        }
    },
    persist: true
});