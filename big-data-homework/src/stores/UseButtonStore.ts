import { defineStore } from 'pinia';

export const UseButtonStore = defineStore({
    id: 'button',
    state: () => ({
        button: 0,
        bossButton: 0,
        indexButton: 0,
    }),
    actions: {
        setButton(button) {
            this.button = button;
        },
        setBossButton(bossButton) {
            this.bossButton = bossButton;
        },
        setIndexButton(indexButton) {
            this.indexButton = indexButton;
        }
    },
    persist: true
});