package com.sgrej.domain.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private int sendId;
    private int receiveId;
    private String context;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "Asia/Shanghai")
    private Timestamp time;
    private int status;//0为未读，1为已读
}
