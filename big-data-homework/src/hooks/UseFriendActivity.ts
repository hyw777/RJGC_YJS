import {ref} from "vue";
import axios from "axios";

export function useFriendActivity() {
    const friendData = ref({
        "reviewVOS": [],
        "collectionVOS": []
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

    async function getFriendData() {
        try {
            let res = await axios.get('/api/friend/recent')
            friendData.value = res.data.data
            if(friendData.value.reviewVOS.length > 3){
                friendData.value.reviewVOS = friendData.value.reviewVOS.slice(0,3)
            }
            if(friendData.value.collectionVOS.length > 3){
                friendData.value.collectionVOS = friendData.value.collectionVOS.slice(0,3)
            }
            friendData.value.reviewVOS.forEach((review) => {
                review.date = formatDateTime(review.date);
            });
            friendData.value.collectionVOS.forEach((collection) => {
                collection.date = formatDateTime(collection.date);
            });
        }catch (e) {
            console.log(e)
        }
    }

    return {friendData,getFriendData,formatDateTime}
}