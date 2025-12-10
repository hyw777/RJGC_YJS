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
import java.util.stream.Collectors;

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
        PageHelper.startPage(findFriendDTO.getPageNum(), findFriendDTO.getPageSize());
        Page<User> users = userMapper.findFriend(findFriendDTO);

        List<FindfriendVO> findFriendVOS = users.getResult().stream()
                .map(user -> {
                    FindfriendVO vo = new FindfriendVO();
                    BeanUtils.copyProperties(user, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return new PageResult(users.getTotal(), findFriendVOS);
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
