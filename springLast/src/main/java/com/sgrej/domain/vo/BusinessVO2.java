package com.sgrej.domain.vo;

import com.sgrej.domain.pojo.Tips;
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
     * 邮政编码
     */
    private String postalCode;

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
     * 商户属性
     */
    private String attributes;

    /**
     * 营业时间
     */
    private String hours;

    /**
     * 用于获取图片和评论信息的唯一标识
     */
    private String bid;

    /**
     * 图片列表
     */
    private List<String> imageList;

    /**
     * 有关本商铺的评价列表
     */
    private List<ReviewVO> reviewVOList;

    /**
     * 有关本商铺的小贴士列表
     */
    private List<TipsVO> tipList;

}
