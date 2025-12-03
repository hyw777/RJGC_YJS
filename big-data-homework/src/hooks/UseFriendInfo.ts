import {ref} from "vue";
import axios from "axios";

export default ()=>{
    // 设置合理的默认值结构
    let friendInfo = ref({
        userVO: {
            name: '',
            age: 0,
            gender: '',
            city: '',
            email: '',
            reviewCount: 0,
            cool: 0,
            useful: 0,
            elite: '',
            yelpingSince: '',
            funny: 0
        },
        reviewsList: [],
        collectionVOList: [],
        findfriendVOS: []
    });

    function formatDateTime(dateTime) {
        const date = new Date(dateTime);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    function getFriendInfo(userId) {
        axios.get(`/api/friend/details/${userId}`).then(res => {
            friendInfo.value = res.data.data;
            console.log("好友信息：",res);
            // 注册日期格式调整
            friendInfo.value.userVO.yelpingSince = formatDateTime(friendInfo.value.userVO.yelpingSince);

            // 评论日期格式调整
            friendInfo.value.reviewsList.forEach((review) => {
                review.date = formatDateTime(review.date);
            });
        }).catch((error)=>{
            console.log(error)
        })
    }
    return {friendInfo,getFriendInfo}
}