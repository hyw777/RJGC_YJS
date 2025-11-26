package com.sgrej.service;

import com.sgrej.domain.dto.AgreeDTO;
import com.sgrej.domain.dto.ApplicationDTO;
import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.Apply;
import com.sgrej.domain.pojo.PageResult;
import com.sgrej.domain.vo.FindfriendVO;
import com.sgrej.domain.vo.FriendDetailVO;
import com.sgrej.domain.vo.FriendRecentVO;

import java.util.List;

public interface FriendService {

    /**
     * 朋友最近
     * @return
     */
    FriendRecentVO getFriendRecent();

    /**
     * 找朋友
     * @param findFriendDTO
     * @return
     */
    PageResult findFriend(FindFriendDTO findFriendDTO);

    /**
     * 添加朋友
     * @param applicationDTO
     */
    void addFriend(ApplicationDTO applicationDTO);

    /**
     * 未通过好友数
     * @return
     */
    int unPass();

    /**
     * 好友申请信息
     * @return
     */
    List<Apply> getApplys();

    /**
     * 同意or拒绝?
     * @param agreeDTO
     */
    void isAgree(AgreeDTO agreeDTO);

    /**
     * 我的好友
     * @return
     */
    List<FindfriendVO> getMyFriends();

    /**
     * 删除好友
     * @param uid
     */
    void deleteFriend(String uid);

    /**
     * 好友详情
     * @param userId
     * @return
     */
    FriendDetailVO getFriendDetail(int userId);
}
