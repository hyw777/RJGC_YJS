package com.sgrej.service;

import com.sgrej.domain.dto.FindFriendDTO;
import com.sgrej.domain.pojo.PageResult;

public interface UserManageService {

    /**
     * 所有用户
     * @return
     */
    PageResult getAllUser(FindFriendDTO findFriendDTO);

    /**
     * 封禁/解封用户
     *
     * @param userId
     * @param status
     */
    void banUser(int userId, int status);
}
