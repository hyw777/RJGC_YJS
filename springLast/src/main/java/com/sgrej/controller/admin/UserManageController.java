package com.sgrej.controller.admin;


import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.dto.UpdateUser;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.mapper.User_RoleMapper;
import com.sgrej.service.UserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userManage")
@Slf4j
public class UserManageController {
    @Autowired
    private User_RoleMapper userRoleMapper;

    @Autowired
    private UserManageService userManageService;

    /**
     * 得到所有用户信息
     * @return
     */
    @PostMapping("/getInfo")
    public ResponseResult getUserInfo(@RequestBody FindFriendDTO findFriendDTO){
        log.info("得到所有用户信息");
        PageResult pageResult= userManageService.getAllUser(findFriendDTO);
        return ResponseResult.success(pageResult);
    }

    /**
     * 封禁/解封用户
     * @param updateUser
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult banUser(@RequestBody UpdateUser updateUser){
        log.info("封禁/解封用户:",updateUser);
        userManageService.banUser(updateUser.getUserId(),updateUser.getStatus());
        return ResponseResult.success();
    }

    @GetMapping("/getStatus/{userId}")
    public ResponseResult getStatus(@PathVariable int userId){
        log.info("查询用户{}状态",userId);
        return ResponseResult.success(userRoleMapper.getStatus(userId));
    }

}
