import axios from "@/interceptor/Axios"

/**
 * 评价满意度 API
 */
export function changeReviewSatisfaction(rsDTO) {
    return axios.put('/api/review/satisfaction', rsDTO);
}