import {ref} from "vue";
import axios from "axios";

export function useWriteReviews() {
    let isDisplayed = ref(false)
    let request = ref({
        "businessId": "",
        "bid": "",
        "text": "",
        "stars": 0
    })
    let satisfactionData = ref({
        "reviewId": 0,
        "cool": 0,
        "useful": 0,
        "funny": 0
    })

    function switchStatus() {
        isDisplayed.value = !isDisplayed.value
    }

    async function writeReview(businessId,bid) {
        console.log(`businessId = ${businessId}  bid = ${bid}`)
        request.value.businessId = businessId
        request.value.bid = bid
        try {
            let res = await axios.post('/api/review/writeReview',request.value)
            ElMessage({
                message: res.data.data,
                type: 'success',
            })
            location.reload()
        }catch(error) {
            console.log(error)
        }
    }

    async function satisfaction() {
        try {
            let res = await axios.put('/api/review/satisfaction',satisfactionData.value)
            ElMessage({
                message: res.data.data,
                type: 'success',
            })
        }catch(error) {
            console.log(error)
        }
    }

    return {isDisplayed,switchStatus,writeReview,request,satisfactionData,satisfaction}
}