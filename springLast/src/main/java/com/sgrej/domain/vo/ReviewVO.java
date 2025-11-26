package com.sgrej.domain.vo;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReviewVO implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    private int reviewId;//评价id

    private String userName;//评价人

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

    /**
     * 商家名
     */
    private String businessName;

}
