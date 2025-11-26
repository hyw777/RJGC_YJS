package com.sgrej.mapper;

import com.sgrej.domain.dto.MessageDTO;
import com.sgrej.domain.pojo.LastMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LastMessageMapper {

    /**
     * 更新与好友的最后一条消息记录
     * @param messageDTO
     * @return
     */
    int update(MessageDTO messageDTO);

    /**
     * 不存在就添加与好友的最后一条聊天记录
     * @param messageDTO
     */
    void add(MessageDTO messageDTO);

    /**
     * 与好友的聊天记录
     * @param userId
     * @return
     */
    List<LastMessage> getRecords(int userId);
}
