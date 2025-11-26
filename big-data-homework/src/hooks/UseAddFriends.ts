import {ref} from "vue";
import axios from "axios";

export function useAddFriends() {
    let notifications = ref(0)
    let isExpanded = ref(false)
    let applyInfo = ref([])
    let agreeInfo = ref({
        "result": 0,
        "applicantId": 0
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

    async function getNotifications() {
        try {
            let res = await axios.get('/api/friend/unPass')
            notifications.value = res.data.data
        }catch(err) {
            console.log(err)
        }
    }

    function switchExpand() {
        isExpanded.value = !isExpanded.value
    }

    async function getApplyInfo() {
        try {
            let res = await axios.get('/api/friend/apply')
            applyInfo.value = res.data.data
            applyInfo.value.forEach((review) => {
                review.time = formatDateTime(review.time);
            });
        }catch (e) {
            console.log(e)
        }
    }

    async function agree(applicantId) {
        agreeInfo.value.applicantId = applicantId
        agreeInfo.value.result = 1
        try {
            let res = await axios.put('/api/friend/isAgree',agreeInfo.value)
            ElMessage.success('已同意该好友申请')
            location.reload()
        }catch (e) {
            console.log(e)
        }
    }

    async function disagree(applicantId) {
        agreeInfo.value.applicantId = applicantId
        agreeInfo.value.result = 0
        try {
            let res = await axios.put('/api/friend/isAgree',agreeInfo.value)
            ElMessage.success('已拒绝该好友申请')
            location.reload()
        }catch (e) {
            console.log(e)
        }
    }

    return {notifications, getNotifications,isExpanded,switchExpand,applyInfo,getApplyInfo,agreeInfo,agree,disagree};
}