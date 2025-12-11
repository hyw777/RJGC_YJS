package com.sgrej.domain.vo;

import lombok.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BusinessVO1 implements Serializable {

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

    private String bid;//图片信息和评论信息

    private double distance;

    private String image;//图片信息

}
