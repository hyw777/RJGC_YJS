package com.sgrej.domain.vo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BusinessVO2 implements Serializable {

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

    private String goodForKids;//是否对孩子友好

    private String businessAcceptsCreditcards;//是否接受刷卡

    private String bid;//用于获取图片和评论信息

    private List<String> imageList;//图片列表

    private List<ReviewVO> reviewVOList;//有关本商铺的评价


}
