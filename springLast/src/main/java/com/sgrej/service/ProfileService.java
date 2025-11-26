package com.sgrej.service;

import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.ProfileInfoVO;
import com.sgrej.domain.vo.UserVO;
import com.sgrej.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ProfileService {
    //得到画像类别信息
    List<ProfileInfoVO> getProFileCategories();

    //得到用户信息
    List<UserVO> getUserDetails(String profileId,int pageNum,int total);


    //得到商家信息
    List<BusinessVO1> getBusinessDetails(String profileId,int pageNum,int pageSize);
}
