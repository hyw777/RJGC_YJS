// 创建一个路由器,并暴露出去

// 引入路由器构造函数以及路由器工作模式构造函数
import {createRouter, createWebHistory} from "vue-router"

// 引入可能要呈现的组件
import NewIndex from "@/pages/NewIndex.vue"
import Profile from "@/pages/Profile.vue"
import Login from "@/pages/Login.vue";
import Overview from "@/components/profile/Overview.vue";
import {useAuthStore} from "@/stores/UseAuthStore";
import {UseButtonStore} from "@/stores/UseButtonStore";
import {toRefs} from "vue";
import Reviews from "@/components/profile/Reviews.vue";
import Collections from "@/components/profile/Collections.vue";
import Friends from "@/components/profile/Friends.vue";
import FriendInfo from "@/pages/FriendInfo.vue";
import FOverview from "@/components/friendInfo/FOverview.vue";
import FReviews from "@/components/friendInfo/FReviews.vue";
import FCollections from "@/components/friendInfo/FCollections.vue";
import FFriends from "@/components/friendInfo/FFriends.vue";
import Chat from "@/pages/Chat.vue";
import {useEditStore} from "@/stores/UseEditStore"
import Boss from "@/pages/Boss.vue";
import WorkBench from "@/components/boss/WorkBench.vue";
import EditInfo from "@/components/boss/EditInfo.vue";
import MerchantDetail from "@/pages/MerchantDetail.vue";
import MerchantView from "@/components/merchant-detail/MerchantView.vue";
import Information from "@/components/boss/Information.vue";
import adminPage from "@/pages/Left2.vue";
import BusinessApplyManage from "@/components/admin/BusinessApplyManage.vue";
import UserManage from "@/components/admin/UserManage2.vue";
import UploadImg from "@/components/boss/UploadImg.vue";
import Search from "@/pages/Search.vue";
import ReviewManage from "@/components/admin/ReviewManage.vue";
import CategoriesSearch from "@/pages/CategoriesSearch.vue";
import ProfileCenter from "@/components/admin/ProfileCenter.vue";
import NearBy from "@/components/new-index/NearBy.vue";
import Friend from "@/components/new-index/Friend.vue";
import News from "@/components/new-index/News.vue";
import ImageDisplay from "@/pages/ImageDisplay.vue";
import FindFreind from "@/pages/FindFreind.vue";

// 创建路由器并暴露出去
export const router = createRouter({
    history: createWebHistory(),// 路由器工作模式
    routes: [// 一个个路由规则
        {
            path: '/',
            component: NewIndex,
            children: [
                {
                    path: '/nearby',
                    component: NearBy
                },
                {
                    path: '/friend',
                    component: Friend
                },
                {
                    path: '/news',
                    component: News
                },
            ]
        },
        {
            path:'/findFriends',
            component:FindFreind,
        },
        {
          path: '/merchantView',
          component: MerchantView
        },
        {
            path: '/imageDisplay',
            component: ImageDisplay
        },
        {
            name: 'CategoriesSearch',
            path: '/categoriesSearch',
            component: CategoriesSearch
        },
        {
            name: 'Search',
            path: '/search',
            component: Search
        },
        {
            path:'/adminPage',
            component:adminPage,
            redirect:'/adminPage/userManage',
            children:[
                {
                    path:'userManage',
                    component:UserManage
                },
                {
                    path:'businessApplyManage',
                    component:BusinessApplyManage
                },
                {
                    path:'reviewManage',
                    component:ReviewManage
                },
                {
                    path:'profileCenter',
                    component:ProfileCenter
                },
            ]
        },
        {
            path: '/merchantDetail',
            component: MerchantDetail
        },
        {
            path: '/boss',
            component: Boss,
            redirect: '/boss/information',
            children: [
                {
                    path: '/boss/information',
                    component: Information
                },
                {
                    path: '/boss/editInfo',
                    component: EditInfo
                },
                {
                    path: '/boss/workbench',
                    component: WorkBench
                },
                {
                    path: '/boss/uploadImg',
                    component: UploadImg
                }
            ]
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/chat',
            component: Chat
        },
        {
            path: '/friendInfo',
            component: FriendInfo,
            redirect: '/friendInfo/overview',
            children: [
                {
                    path: '/friendInfo/overview',
                    component: FOverview
                },
                {
                    path: '/friendInfo/reviews',
                    component: FReviews
                },
                {
                    path: '/friendInfo/collections',
                    component: FCollections
                },
                {
                    path: '/friendInfo/friends',
                    component: FFriends
                },
            ]
        },
        {
            path: '/profile',
            redirect: '/profile/overview',
            component: Profile,
            children: [
                {
                    path: '/profile/overview',
                    component: Overview
                },
                {
                    path: '/profile/reviews',
                    component: Reviews
                },
                {
                    path: '/profile/collections',
                    component: Collections
                },
                {
                    path: '/profile/friends',
                    component: Friends
                },
            ]
        },
    ]
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    let buttonStore = UseButtonStore()
    let editStore = useEditStore()

    if(to.path == '/' && authStore.token){
        router.push('/nearby')
    }

    if (to.path == '/profile/overview' || to.path == '/friendInfo/overview') {
        buttonStore.setButton(0);
    } else if (to.path == '/profile/reviews' || to.path == '/friendInfo/reviews') {
        buttonStore.setButton(1);
    } else if (to.path == '/profile/collections' || to.path == '/friendInfo/collections') {
        buttonStore.setButton(2);
    } else if (to.path == '/profile/friends' || to.path == '/friendInfo/friends') {
        buttonStore.setButton(3);
    } else {
        buttonStore.setButton(0);
        editStore.edit = false
    }


    let {token} = toRefs(authStore);
    // 检查是否需要认证
    if (to.path !== '/imageDisplay' && to.path !== '/categoriesSearch' && to.path !== '/search' && to.path !== '/' && token.value == null) {
        // 如果未登录，则重定向到首页
        next('/');
    } else {
        // 否则允许跳转
        next();
    }
});