package com.sgrej.mapper;

import com.github.pagehelper.Page;
import com.sgrej.domain.dto.FindReviewDTO;
import com.sgrej.domain.dto.ReviewDTO;
import com.sgrej.domain.dto.ReviewSatisfactionDTO;
import com.sgrej.domain.pojo.Review;
import com.sgrej.domain.vo.ReviewVO;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ReviewMapper {

    /**
     * 查询用户的评价
     * @param userId
     * @return
     */
    @Select("select review_id,user_id,business_id,stars,useful,funny,cool,text,date,rid from reviews where user_id = #{userId} order by date desc")
    List<Review> searchByUid(String userId);


    @Select("select review_id,user_id,business_id,stars,useful,funny,cool,text,date,rid from reviews where business_id = #{businessId} order by date desc")
    List<Review> searchByBid(String businessId);

    /**
     * 增加评价
     * @param reviewDTO
     */
     void addReview(ReviewDTO reviewDTO);


    /**
     * 删除评价
     * @param reviewId
     */
    @Delete("delete from reviews where review_id = #{reviewId}")
    void deleteReview(int reviewId);

    /**
     * 评价满意度
     * @param rsDTO
     * @return
     */
    int changeReview(ReviewSatisfactionDTO rsDTO);

    @Select("select review_id,user_id,business_id,stars,useful,funny,cool,text,date,rid from reviews where user_id = #{friend} and date >= #{startTime} and date <= #{endTime} order by date desc")
    List<Review> searchByUidAndTime(String friend, Timestamp startTime, Timestamp endTime);

    /**
     * 得到评论数量
     * @param uid
     * @return
     */
    @Select("select count(review_id) from reviews where user_id = #{uid}")
    int getReviewCount(String uid);

    /**
     * 得到所有评论和查找平理论
     * @return
     */
    Page<Review> getAll(Timestamp begin,Timestamp end);

    @Select("select user_id from reviews where review_id = #{reviewId}")
   String getUserIdById(int reviewId);
}
