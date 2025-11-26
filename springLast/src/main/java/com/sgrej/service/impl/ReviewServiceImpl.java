package com.sgrej.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgrej.domain.dto.*;
import com.sgrej.domain.pojo.*;
import com.sgrej.domain.vo.ReviewVO;
import com.sgrej.mapper.BusinessMapper;
import com.sgrej.mapper.NewMapper;
import com.sgrej.mapper.ReviewMapper;
import com.sgrej.mapper.UserMapper;
import com.sgrej.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private NewMapper newMapper;

    /**
     * 对商户做出评价
     * @param reviewDTO
     */
    @Override
    @Transactional
    public void writeReview(ReviewDTO reviewDTO) {
        //得到当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();

        //得到当前用户的uId
        String uId=loginUser.getUser().getUid();

        /**
         * 转换日期格式
         */
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());

        //增加评论信息
        reviewDTO.setUserId(uId);
        reviewDTO.setDate(time);
        reviewMapper.addReview(reviewDTO);

        //在进行评价后，个人的评价总数也应该要加一
        EditUserDTO editUserDTO=new EditUserDTO();
        editUserDTO.setUserId(userId);
        editUserDTO.setReviewCount(1);
        userMapper.editUser(editUserDTO);

        //对应商户所获得的总评价数也要加一
        Business business=new Business();
        int businessId= reviewDTO.getBusinessId();
        business.setReviewCount(1);
        business.setBusinessId(businessId);
        businessMapper.changeInfo(business);

    }

    /**
     * 删除评价
     * @param reviewId
     */
    @Override
    public void deleteReview(int reviewId) {
        reviewMapper.deleteReview(reviewId);
    }

    /**
     * 评价满意度
     * @param rsDTO
     */
    @Override
    @Transactional
    public void changeReview(ReviewSatisfactionDTO rsDTO) {
        //首先增加这条评价的满意度
        int result=reviewMapper.changeReview(rsDTO);

        if(result == 0){
            throw new RuntimeException("failed");
        }

        //再增加这条评价的用户的满意度
        //得到当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser)authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();

        EditUserDTO editUserDTO=new EditUserDTO();
        BeanUtils.copyProperties(rsDTO,editUserDTO);
        editUserDTO.setUserId(userId);

        userMapper.editUser(editUserDTO);
    }

    /**
     * 得到所有评论
     * @return
     */
    @Override
    public PageResult getAllReview(FindReviewDTO findReviewDTO) {
        PageHelper.startPage(findReviewDTO.getPageNum(), findReviewDTO.getPageSize());
// 设置默认值，例如可以设置为当前日期的前后一段时间
        LocalDateTime defaultBeginTime = LocalDateTime.now().minusYears(6); // 例如，默认开始时间为一年前

        LocalDateTime defaultEndTime = LocalDateTime.now(); // 默认结束时间为当前时间

        LocalDateTime beginTime;

        LocalDateTime endTime;

        if (findReviewDTO.getBegin() != null) {
            beginTime = LocalDateTime.of(findReviewDTO.getBegin(), LocalTime.MIN);
        } else {
            beginTime = defaultBeginTime;
        }

        if (findReviewDTO.getEnd() != null) {
            endTime = LocalDateTime.of(findReviewDTO.getEnd(), LocalTime.MAX);
        } else {
            endTime = defaultEndTime;
        }

        Timestamp begin = Timestamp.valueOf(beginTime);
        Timestamp end = Timestamp.valueOf(endTime);

        Page<Review> reviewPage = reviewMapper.getAll(begin, end);
        List<ReviewVO> reviewVOList = new ArrayList<>();

        for (Review review:reviewPage.getResult()) {
            //每个评论者的名字
            String userName=userMapper.getUserName(review.getUserId());

            //对每个用户的评论进行封装

            //首先找到评论对应的商家名和用户名
                String businessName = businessMapper.selectBusinessName(review.getBusinessId());

                //创建ReviewVO对象用于接收
                ReviewVO reviewVO=new ReviewVO();
                BeanUtils.copyProperties(review,reviewVO);
                reviewVO.setUserName(userName);
                reviewVO.setBusinessName(businessName);

                //将其添加到reviewVoList中
                reviewVOList.add(reviewVO);
        }
        return new PageResult(reviewPage.getTotal(),reviewVOList);
    }


    /**
     * 做出警告
     */
    @Override
    public void warning(ReviewVO reviewVO) {
        //得到用户id
        int reviewId = reviewVO.getReviewId();
        String userId = reviewMapper.getUserIdById(reviewId);
        int toUserId = userMapper.getUserByUid(userId).getUserId();

        //封装警告信息
        News news=new News();
        news.setTime(Timestamp.valueOf(LocalDateTime.now()));
        news.setUserId(toUserId);
        news.setContent("你在"+reviewVO.getDate()+"对店铺《"+reviewVO.getBusinessName()+"》做出的评价:"
        +reviewVO.getText().substring(0, Math.min(reviewVO.getText().length(), 7))+"..."+"违反了社区规定，建议你尽快删除，并且规范自己的发言，否则将面临被封号的风险");

        //写入警告信息
        newMapper.add(news);

    }
}
