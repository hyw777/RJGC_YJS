package com.sgrej.domain.pojo;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Review implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 评价id
     */
    private int reviewId;

    private String rid;//用于获取评论信息

    /**
     * 用户id
     */
    private String userId;
    /**
     * 商户id
     */
    private String businessId;
    /**
     * 评分
     */
    private double stars;
    /**
     * 有用的
     */
    private int useful;
    /**
     * 有趣的
     */
    private int funny;
    /**
     * 酷
     */
    private int cool;
    /**
     * 评价内容
     */
    private String text;
    /**
     * 评价时间
     */
    private Timestamp date;
}
