package com.sgrej.mapper;

import com.sgrej.domain.pojo.Message;
import com.sgrej.domain.vo.MessageHistoryVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 发送新消息
     * @param message1
     */
    @Insert(" INSERT INTO message(send_id, receive_id, context, time, status)  " +
            "        VALUES (#{sendId}, #{receiveId}, #{context}, #{time}, #{status})")
    void add(Message message1);


    /**
     * 查找聊天记录
     * @param userId
     * @param friendId
     * @return
     */
    List<MessageHistoryVO> getChatRecords(int userId, int friendId);

    /**
     * 查看消息
     * @param userId
     * @param friendId
     */
    void readRecords(int userId, int friendId);

    /**
     * 未读消息总数
     * @param userId
     */
    @Select("select count(receive_id) from message where receive_id = #{userId} and status = 0")
    int getUnRead(int userId);

}
