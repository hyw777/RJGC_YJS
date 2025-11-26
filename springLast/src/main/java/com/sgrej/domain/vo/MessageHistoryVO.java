package com.sgrej.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sgrej.domain.pojo.Message;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageHistoryVO {

    private int receiveId;

    @JsonFormat(timezone = "Asia/Shanghai")
    private Timestamp time;

    private String context;


}
