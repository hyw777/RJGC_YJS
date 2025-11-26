package com.sgrej.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.domain.pojo.User;
import com.sgrej.mapper.MenuMapper;
import com.sgrej.mapper.UserMapper;
import com.sgrej.mapper.User_RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class USerDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private User_RoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,userName);
        User user = userMapper.selectOne(queryWrapper);

        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("账号或密码错误");
        }

        int uStatus=userRoleMapper.getStatus(user.getUserId());

        if(uStatus == 1){
            throw new RuntimeException("该账号因涉嫌违规操作已被管理员封禁，请联系管理员解封");
        }

        //查询对应的权限信息
        List<String> list = menuMapper.selectPermsByUserId(user.getUserId());

        //把数据封装成userDetails返回
        return new LoginUser(user,list,null);
    }
}
