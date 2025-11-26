import {ref} from "vue";
import axios from "axios";

export default ()=>{
    let friendInfo = ref({
        "userVO": {
            "name": "周爽",
                "age": 0,
                "gender": "女",
                "city": "阮恩杰的心里",
                "email": "2162063785@qq.com",
                "reviewCount": 15,
                "cool": 3,
                "useful": 3,
                "elite": "null",
                "yelpingSince": "2024-05-20T16:00:00.000+00:00",
                "funny": 4,
                "reviewsList": null
        },
        "reviewsList": [],
            "collectionVOList": [],
            "findfriendVOS": []
    })

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