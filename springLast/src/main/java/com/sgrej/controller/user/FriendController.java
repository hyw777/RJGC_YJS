package com.sgrej.controller.user;

import com.sgrej.domain.dto.AgreeDTO;
import com.sgrej.domain.dto.ApplicationDTO;
import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.Apply;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.FindfriendVO;
import com.sgrej.domain.vo.FriendDetailVO;
import com.sgrej.domain.vo.FriendRecentVO;
import com.sgrej.domain.vo.FindfriendVO;
import com.sgrej.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    /**
     * 朋友最近
     * @return
     */
    @GetMapping("/recent")
    public ResponseResult friendRecent(){
        log.info("朋友最近");
       FriendRecentVO friendRecentVO= friendService.getFriendRecent();
       return ResponseResult.success(friendRecentVO);
    }

    /**
     * 找朋友
     * @param findFriendDTO
     * @return
     */
    @PostMapping("/find")
    public ResponseResult findFriend(@RequestBody FindFriendDTO findFriendDTO){
        log.info("找朋友");
        PageResult friendVOS =friendService.findFriend(findFriendDTO);
        return ResponseResult.success(friendVOS);
    }

    @PostMapping("/add")
    public ResponseResult addFriend(@RequestBody ApplicationDTO applicationDTO){
        log.info("添加朋友");
        friendService.addFriend(applicationDTO);
        return ResponseResult.success();
    }


    /**
     * 未通过好友数量
     * @return
     */
    @GetMapping("/unPass")
    public ResponseResult unPass(){
        log.info("未通过好友数");
        int unPass=friendService.unPass();
        return ResponseResult.success(unPass);
    }


    /**
     * 好友申请信息
     * @return
     */
    @GetMapping("/apply")
    public ResponseResult apply(){
        log.info("好友申请信息");
        List<Apply> applys=friendService.getApplys();
        return ResponseResult.success(applys);
    }

    /**
     * 同意还是拒绝
     * @param agreeDTO
     * @return
     */
    @PutMapping("/isAgree")
    public ResponseResult isAgree(@RequestBody AgreeDTO agreeDTO){
       log.info("同意还是拒绝:{}",agreeDTO);
        friendService.isAgree(agreeDTO);
       return ResponseResult.success();
    }

    /**
     * 我的好友
     * @return
     */
    @GetMapping("/my")
    public ResponseResult myFriends(){
        log.info("我的好友");
        List<FindfriendVO> findfriendVOS=friendService.getMyFriends();
       return ResponseResult.success(findfriendVOS);
    }

    /**
     * 删除好友
     * @param uid
     * @return
     */
    @DeleteMapping("/delete/{uid}")
    public ResponseResult deleteFriend(@PathVariable String uid){
        log.info("删除好友：{}",uid);
        friendService.deleteFriend(uid);
        return ResponseResult.success();
    }

    /**
     * 好友详情
     * @param userId
     * @return
     */
    @GetMapping("/details/{userId}")
    public ResponseResult details(@PathVariable int userId){
        log.info("好友详情:{}",userId);
        FriendDetailVO friendDetailVO=friendService.getFriendDetail(userId);
        log.info("好友详情:{}",friendDetailVO);
        return ResponseResult.success(friendDetailVO);
    }

}
