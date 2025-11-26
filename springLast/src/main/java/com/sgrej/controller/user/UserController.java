package com.sgrej.controller.user;

import com.sgrej.domain.dto.*;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.domain.pojo.News;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.pojo.User;
import com.sgrej.domain.vo.UserVO;
import com.sgrej.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginUserDTO user){
        log.info("用户登录");
        //登录
        return userService.login(user);
    }

    /**
     * 用户登出
     * @return
     */
    @Operation(summary = "登出")
    @GetMapping("/logout")
    public ResponseResult logout(){

        System.out.println();
        log.info("退出登录");
        userService.logout();
        return ResponseResult.success("登出成功");
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody RegisterUserDTO user) throws ParseException {
        log.info("注册用户");
        userService.register(user);
        return ResponseResult.success("注册成功");
    }

    /**
     * 发送验证码
     */
    @GetMapping("/sendCode/{email}")
    public ResponseResult sendCode(@PathVariable("email") String email){
        log.info("发送验证码");
        userService.sendCode(email);
        return ResponseResult.success();
    }

    /**
     * 编辑个人信息
     */
    @PutMapping("/edit")
    public ResponseResult edit(@RequestBody EditUserDTO editUser){
        log.info("更改个人信息");
        userService.edit(editUser);
        return ResponseResult.success("编辑成功");

    }

    /**
     * 找回密码
     */
    @PostMapping("/findUser")
    public ResponseResult findUser(@RequestBody FindUserDTO findUser){
        log.info("找回密码");
        userService.findUser(findUser);
        return ResponseResult.success("请使用新密码登录");
    }



    /**
     * 注销账号
     */
  /*  @GetMapping("/destory")
    public ResponseResult destroy(){

    }*/

    /**
     * 更改密码
     * @param updatePwdDTO
     * @return
     */
    @PatchMapping("/updatePwd")
    public ResponseResult updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO){
        log.info("修改密码");
        userService.updatePwd(updatePwdDTO);
        return ResponseResult.success("修改成功，请重新登陆");
    }

    /**
     * 个人中心
     * @return
     */
    @GetMapping("/pCenter")
    public ResponseResult pCenter(){
        log.info("个人中心");
        UserVO userVO=userService.getUserVO();
        return ResponseResult.success(userVO);
    }

    /**
     * 新事件
     * @return
     */
    @GetMapping("/getNews")
    public ResponseResult getNews(){
        log.info("新事件");
        List<News> newsList=userService.getNews();
        return ResponseResult.success(newsList);
    }
}
