package com.sgrej.service;

import com.sgrej.domain.dto.*;
import com.sgrej.domain.pojo.News;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.pojo.User;
import com.sgrej.domain.vo.UserVO;

import java.util.List;

public interface UserService {


    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult login(LoginUserDTO user);

    /**
     * 登出
     */
    void logout();

    /**
     * 注册用户
     * @param user
     */
    void register(RegisterUserDTO user);

    /**
     * 发送验证码
     * @param email
     */
    void sendCode(String email);

    /**
     * 编辑个人资料
     * @param editUser
     */
    void edit(EditUserDTO editUser);

    /**
     * 找回密码
     * @param findUser
     */
    void findUser(FindUserDTO findUser);

    /**
     * 更改密码
     * @param updatePwdDTO
     */
    void updatePwd(UpdatePwdDTO updatePwdDTO);

    /**
     * 个人中心
     * @return
     */
    UserVO getUserVO();

    /**
     * 新时事件
     * @return
     */
    List<News> getNews();

    /**
     * 根据id得到用户信息
     * @param userId
     * @return
     */
    UserVO getUserVO2(int userId);
}
