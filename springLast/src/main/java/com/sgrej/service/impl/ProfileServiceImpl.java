package com.sgrej.service.impl;

import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.pojo.User;
import com.sgrej.domain.vo.BusinessVO;
import com.sgrej.domain.vo.BusinessVO1;
import com.sgrej.domain.vo.ProfileInfoVO;
import com.sgrej.domain.vo.UserVO;
import com.sgrej.mapper.BusinessMapper;
import com.sgrej.mapper.PhotoMapper;
import com.sgrej.mapper.ProfileMapper;
import com.sgrej.mapper.UserMapper;
import com.sgrej.service.BusinessService;
import com.sgrej.service.ProfileService;
import com.sgrej.service.UserService;
import com.sgrej.utils.RedisCache;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private UserMapper userMapper;

    //得到画像类别信息
    @Override
    public List<ProfileInfoVO> getProFileCategories() {
        List<ProfileInfoVO> profileInfoVOList = profileMapper.getCategories();
        return profileInfoVOList;
    }

    //得到用户信息
    @Override
    public List<UserVO> getUserDetails(String profileId,int pageNum,int pageSize) {
        //首先从redis中拿到对应的商家id
        Set<Object> cacheSet = redisCache.getCacheSet("group_" + profileId);
        List<UserVO> userVOList=new ArrayList<>();
        for(int i=(pageNum-1)*pageSize; i<cacheSet.size() && i<pageNum*pageSize;i++){
            User user= userMapper.selectById(Integer.parseInt(cacheSet.toArray()[i].toString()));
            UserVO userVO=new UserVO();
            BeanUtils.copyProperties(user,userVO);
            userVOList.add(userVO);
        }
        return userVOList;
    }

        //得到商家信息
        @Override
        public List<BusinessVO1> getBusinessDetails (String profileId,int pageNum,int pageSize){
            //首先从redis中拿到对应的商家id
            Set<Object> cacheSet = redisCache.getCacheSet("group_" + profileId);

//            //再从mysql中根据businessId拿到对应的商家信息并且封装成businessVO1返回给前端
            List<BusinessVO1> businessVO1List=new ArrayList<>();

            for(int i=(pageNum-1)*pageSize; i<cacheSet.size() && i<pageNum*pageSize;i++){
                BusinessVO businessVO = businessMapper.getById(Integer.parseInt(cacheSet.toArray()[i].toString()));
                // 得到每个商家的bid
                String bid = businessVO.getBid(); // 假设BusinessVO有getBid()方法

                // 得到商家对应的第一张图片
                String imageUrl = photoMapper.selectImage(bid);
                //TODO
                // 对得到的图片路径还需要进行拼接
                // 创建一个新的BusinessVO1对象或直接在BusinessVO上设置图片（如果允许）
                BusinessVO1 businessVO1 = new BusinessVO1();
                BeanUtils.copyProperties(businessVO, businessVO1); // 复制属性到BusinessVO1
                businessVO1.setImage(imageUrl); // 假设BusinessVO1有setImage()方法
                businessVO1List.add(businessVO1);
            }

            return businessVO1List;
        }

}
