package com.sgrej.domain.vo;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TipsVO implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    private int tipId;//tipId

    private String userName;//评价人

    /**
     * tip内容
     */
    private String text;
    /**
     * 发表时间
     */
    private Timestamp date;
    /**
     * 收到的赞美次数
     */
    private String complimentCount;

    /**
     * 商家名
     */
    private String businessName;

}
