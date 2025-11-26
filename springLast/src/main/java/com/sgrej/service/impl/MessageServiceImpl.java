package com.sgrej.service.impl;

import com.sgrej.domain.pojo.LastMessage;
import com.sgrej.domain.pojo.LoginUser;
import com.sgrej.domain.pojo.Message;
import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.FriendConvertVO;
import com.sgrej.domain.vo.FriendRecentVO;
import com.sgrej.domain.vo.MessageHistoryVO;
import com.sgrej.mapper.LastMessageMapper;
import com.sgrej.mapper.MessageMapper;
import com.sgrej.mapper.UserMapper;
import com.sgrej.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
         @Autowired
         private MessageMapper messageMapper;

         @Autowired
         private LastMessageMapper lastMessageMapper;

         @Autowired
         private UserMapper userMapper;


    /**
     * 聊天历史
     * @return
     */
    @Override
    public List<MessageHistoryVO> getHistory(int friendId) {
        //得到当前用户id
        int userId = getUserId();

        //查找聊天记录；
        List<MessageHistoryVO> messages=messageMapper.getChatRecords(userId,friendId);

        //同时将与该朋友的聊天记录全部设置为已读
        messageMapper.readRecords(userId,friendId);

        return messages;
    }


    @Override
    public int getUnRead() {
        //得到当前用户id
        int userId = getUserId();

        int unRead=messageMapper.getUnRead(userId);
        return unRead;
    }


    /**
     * 聊天室
     * @return
     */
    @Override
    public List<FriendConvertVO> getChatRoom() {
        int userId = getUserId();
        List<LastMessage> lastMessageList = lastMessageMapper.getRecords(userId);
        List<FriendConvertVO> friendConvertVOS = new ArrayList<>();

        if (lastMessageList != null && !lastMessageList.isEmpty()) {
            for (LastMessage lastMessage : lastMessageList) {
                int friendId = lastMessage.getReceiveId() == userId ? lastMessage.getSendId() : lastMessage.getReceiveId();
                String name = userMapper.getFriendName(friendId);

                FriendConvertVO friendRecentVO = new FriendConvertVO();
                BeanUtils.copyProperties(lastMessage, friendRecentVO);
                friendRecentVO.setName(name);
                friendRecentVO.setUserId(userId);
                friendRecentVO.setFriendId(friendId);

                friendConvertVOS.add(friendRecentVO);
            }

            return friendConvertVOS;
        }
        else {
            return null;
        }
    }



    private int getUserId(){
        //得到当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        int userId=loginUser.getUser().getUserId();
        return userId;
    }
}
