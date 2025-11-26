package com.sgrej.service;

import com.sgrej.domain.dto.FindReviewDTO;
import com.sgrej.domain.dto.ReviewDTO;
import com.sgrej.domain.dto.ReviewSatisfactionDTO;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.vo.ReviewVO;

public interface ReviewService {

    /**
     * 对商户作出评价
     * @param reviewDTO
     */
    void writeReview(ReviewDTO reviewDTO);

    /**
     * 删除评价
     * @param reviewId
     */
    void deleteReview(int reviewId);

    /**
     * 评价满意度
     * @param rsDTO
     */
    void changeReview(ReviewSatisfactionDTO rsDTO);

    /**
     * 得到所有评论
     * @return
     */
    PageResult getAllReview(FindReviewDTO findReviewDTO);

    /**
     * 做出警告
     */
    void warning(ReviewVO reviewVO);
}
