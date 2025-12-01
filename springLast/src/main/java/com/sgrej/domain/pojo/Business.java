package com.sgrej.domain.pojo;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Business implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    /**
     * 商铺id - 自增主键
     */
    private Integer businessId;

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
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

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
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

    /**
     * 用于查询对应的评论信息和图片信息
     */
    private String bid;
}
