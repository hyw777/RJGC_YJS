import axios from "axios";
import {router} from "@/router";
import {UseButtonStore} from "@/stores/UseButtonStore";

export default ()=>{
    function deleteReviews(reviewId:number) {
        let buttonStore = UseButtonStore()
        axios.delete(`/api/review/deleteReview/${reviewId}`).then((response)=>{
            ElMessage({
                message: response.data.data,
                type: 'success',
            })
            location.reload();
        }).catch((error)=>{
            ElMessage({
                message: error,
                type: 'error',
            })
        })
    }
    return {deleteReviews}
}