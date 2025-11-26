package com.sgrej.service;

import com.sgrej.domain.vo.FriendConvertVO;
import com.sgrej.domain.vo.MessageHistoryVO;

import java.util.List;

public interface MessageService {

    /**
     * 聊天历史
     * @return
     */
    List<MessageHistoryVO> getHistory(int friendId);

    /**
     * 未读消息总数
     * @return
     */
    int getUnRead();

    /**
     * 聊天室
     * @return
     */
    List<FriendConvertVO> getChatRoom();
}
