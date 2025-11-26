import { defineStore } from 'pinia';

export const useEditStore = defineStore({
    id: 'edit',
    state: () => ({
        edit: false,
    }),
    persist: true
});