package com.sgrej.controller.user;

import com.sgrej.domain.dto.ReviewDTO;
import com.sgrej.domain.dto.ReviewSatisfactionDTO;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@Slf4j
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /**
     * 评价商户
     */
    @PostMapping("/writeReview")
    public ResponseResult writeReview(@RequestBody ReviewDTO reviewDTO){
           reviewService.writeReview(reviewDTO);
           return ResponseResult.success("评价成功");
    }

    /**
     * 删除评价
     * @param reviewId
     * @return
     */
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseResult deleteReview(@PathVariable("reviewId") int reviewId){
        log.info("删除评价");
        reviewService.deleteReview(reviewId);
        return ResponseResult.success("删除成功");
    }

    /**
     * useful,cool,funny
     */
    @PutMapping("/satisfaction")
    public ResponseResult changeReview(@RequestBody ReviewSatisfactionDTO rsDTO){
        log.info("评价满意度");
        System.out.println(rsDTO.toString());
        reviewService.changeReview(rsDTO);
        return ResponseResult.success("ok");
    }

}
