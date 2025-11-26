import { defineStore } from 'pinia';

export const UseImageListStore = defineStore({
    id: 'imageList',
    state: () => ({
        imageList: null
    }),
    actions: {
        setImageList(imageList) {
            this.imageList = imageList;
        },
    },
    persist: true,
});