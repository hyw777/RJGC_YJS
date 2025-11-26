import { ref } from "vue";
import axios from "@/interceptor/Axios";

export default () => {
    const userInfo = ref({
        "name": "",
        "age": 0,
        "gender": "",
        "city": "",
        "email": "",
        "reviewCount": 0,
        "cool": 0,
        "useful": 0,
        "elite": null,
        "yelpingSince": "",
        "funny": 0,
        "reviewsList": []
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

    async function getUserInfo() {
        try {
            const response = await axios.get('/api/user/pCenter');
            userInfo.value = response.data.data;

            // 注册日期格式调整
            userInfo.value.yelpingSince = formatDateTime(userInfo.value.yelpingSince);

            // 评论日期格式调整
            userInfo.value.reviewsList.forEach((review) => {
                review.date = formatDateTime(review.date);
            });
        } catch (error) {
            console.log(error);
        }
    }

    return { userInfo, getUserInfo };
}
