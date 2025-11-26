package com.sgrej.domain.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {


    private String context;
    private int sendId;
    private int receiveId;
}
