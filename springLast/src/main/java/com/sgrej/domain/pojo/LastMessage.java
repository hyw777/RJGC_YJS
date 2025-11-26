package com.sgrej.domain.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LastMessage {
    private int sendId;

    private int receiveId;

    private String context;

    @JsonFormat(timezone = "Asia/Shanghai")
    private Timestamp time;

}
