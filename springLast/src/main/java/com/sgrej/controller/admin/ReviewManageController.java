package com.sgrej.controller.admin;


import com.sgrej.domain.dto.FindReviewDTO;
import com.sgrej.domain.dto.ReviewDTO;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.ReviewVO;
import com.sgrej.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/reviewManage")
public class ReviewManageController {
    @Autowired
    private ReviewService reviewService;


    @PostMapping("/getAll")
    public ResponseResult getAllReviews(@RequestBody FindReviewDTO findReviewDTO){
        log.info("所有评论");
      PageResult pageResult =reviewService.getAllReview(findReviewDTO);
      return ResponseResult.success(pageResult);
    }

    @PostMapping ("/warning")
    public ResponseResult findReviews(@RequestBody ReviewVO reviewVO){
        log.info("做出警告");
        reviewService.warning(reviewVO);
        return ResponseResult.success();
    }
}
