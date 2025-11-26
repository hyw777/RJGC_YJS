package com.sgrej.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FriendConvertVO {
    private String name;//好友名字

    private int friendId;//好友Id

    private int userId;//自己Id;

    @JsonFormat(timezone = "Asia/Shanghai")
    private Timestamp time; //最后一次聊天时间

    private String context; //最后一次聊天内容
}
