package com.sgrej.domain.pojo;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Business implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 商铺id
     */
    private int businessId;
    /**
     * 商铺名
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 城市
     */
    private String city;
    /**
     * 州
     */
    private String state;
    /**
     * 评分
     */
    private double stars;
    /**
     * 评价总数
     */
    private int reviewCount;
    /**
     * 是否开放
     */
    private int isOpen;
    /**
     * 商户类型
     */
    private String categories;
    /**
     * 营业时间
     */
    private String hours;

    private String bid;//用于查询对应的评论信息和图片信息

    private String goodForKids;

    private String businessAcceptsCreditcards;
}
