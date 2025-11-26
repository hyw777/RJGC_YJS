import { defineStore } from 'pinia';

export const UseCollectStore = defineStore({
    id: 'collect',
    state: () => ({
        collect: [],
    }),
    actions: {
        pushCollect(id) {
            this.collect.push(id)
        },
        removeCollect(id) {
            let index = this.collect.findIndex(item => item === id);
            this.collect.splice(index, 1)
        }
    },
    persist: true,
});