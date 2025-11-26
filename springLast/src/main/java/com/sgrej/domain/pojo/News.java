package com.sgrej.domain.pojo;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class News {
    private int id;
    private Timestamp time;
    private String content;
    private int userId;
}
