package com.sgrej.domain.dto;


import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private int reviewId;//评价id

    private String userId;//用户id

    private int businessId;//商户id

    private String bid;//用于存入评价表中

    private double stars;//评分

    private String text;//评价内容

    private Timestamp date;//评价时间

}
