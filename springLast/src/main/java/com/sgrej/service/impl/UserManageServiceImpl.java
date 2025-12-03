package com.sgrej.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.pojo.User;
import com.sgrej.domain.vo.FindfriendVO;
import com.sgrej.mapper.ReviewMapper;
import com.sgrej.mapper.UserMapper;
import com.sgrej.mapper.User_RoleMapper;
import com.sgrej.service.UserManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserManageServiceImpl implements UserManageService {
       @Autowired
       private UserMapper userMapper;

       @Autowired
       private ReviewMapper reviewMapper;

       @Autowired
       private User_RoleMapper userRoleMapper;

    /**
     * 得到所有用户
     * @return
     */
    @Override
    public PageResult getAllUser(@RequestBody FindFriendDTO findFriendDTO) {
        PageHelper.startPage(findFriendDTO.getPageNum(),findFriendDTO.getPageSize());
        List<FindfriendVO> findFriendVOS=new ArrayList<>();
        Page<User> users = userMapper.findFriend(findFriendDTO);
        for(User user:users.getResult()){
            FindfriendVO findfriendVO=new FindfriendVO();
            //复制基础属性
            BeanUtils.copyProperties(user,findfriendVO);

            //找对应用户的评论总数和朋友总数
            if(user.getFriends()!= null) {
                int friendNumber = user.getFriends().split(", ").length;
                findfriendVO.setFriendNumbers(friendNumber);
            }

            //找到评价数量
            int reviewCount=reviewMapper.getReviewCount(user.getUid());
            findfriendVO.setReviewCount(reviewCount);
            findFriendVOS.add(findfriendVO);


        }
        PageHelper.startPage(findFriendDTO.getPageNum(),findFriendDTO.getPageSize());
        return new PageResult(users.getTotal(),findFriendVOS);
    }


    /**
     * 封禁用户
     *
     * @param userId
     * @param status
     */
    @Override
    public void banUser(int userId, int status) {
        userRoleMapper.setStatus(userId,status);
    }
}
