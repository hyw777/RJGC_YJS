package com.sgrej.domain.vo;


import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BusinessApplyVO {
    private int businessId;

    private String name;

    private String address;

    private String city;

    private String state;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    private double stars;

    private int reviewCount;

    private int isOpen;

    private String categories;

    private String attributes;

    private String hours;

    private String bid;

    private Timestamp applyTime;

}
