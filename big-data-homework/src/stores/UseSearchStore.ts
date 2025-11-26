import { defineStore } from 'pinia';

export const UseSearchStore = defineStore({
    id: 'search',
    state: () => ({
        result:{
            "total": 1,
            "records": []
        }
    }),
    actions: {
        setResult(result) {
            this.result = result;
        },
    },
    persist: true
});