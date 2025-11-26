package com.sgrej.controller.user;

import com.sgrej.domain.pojo.ResponseResult;
import com.sgrej.domain.vo.FriendConvertVO;
import com.sgrej.domain.vo.MessageHistoryVO;
import com.sgrej.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 聊天记录
     * @return
     */
    @GetMapping("/history")
    public ResponseResult getHistory(Integer friendId){
        log.info("与朋友:{}的聊天记录",friendId);
        List<MessageHistoryVO> messageHistoryVO=messageService.getHistory(friendId);
        return ResponseResult.success(messageHistoryVO);
    }

    /**
     * 未读消息总数
     * @return
     */
    @GetMapping("/unRead")
    public ResponseResult unReadRecords(){
        log.info("未读消息总数");
        int unRead=messageService.getUnRead();
        return ResponseResult.success(unRead);
    }

    @GetMapping("/chatRoom")
    public ResponseResult chatRoom(){
        log.info("聊天室");
        List<FriendConvertVO> friendConvertVOS=messageService.getChatRoom();
        return ResponseResult.success(friendConvertVOS);
    }

}
