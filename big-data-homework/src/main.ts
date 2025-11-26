// 引入createApp用于创建应用
import { createApp } from 'vue'
// 引入App根组件
import App from '@/App.vue'
// 引入路由器
import {router} from './router'
// 引入Element Plus
import ElementPlus from 'element-plus'
// 引入element图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 引入Pinia
import { createPinia } from 'pinia'
// 引入Pinia持久化插件
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
// 引入axios拦截器实例
import axiosInterceptor from '@/interceptor/Axios'


// 创建一个应用
const app = createApp(App)
// 使用element图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 创建pinia并使用持久化插件
const pinia = createPinia()
// 使用pinia持久化插件
pinia.use(piniaPluginPersistedstate);
// 使用pinia
app.use(pinia)
// 使用路由器
app.use(router)
// 使用Element Plus
app.use(ElementPlus)
// 将axios拦截器实例挂载到全局
app.config.globalProperties.$axios = axiosInterceptor;
// 挂载整个应用到app容器中
app.mount('#app')