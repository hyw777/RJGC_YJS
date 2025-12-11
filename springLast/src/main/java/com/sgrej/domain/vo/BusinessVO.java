package com.sgrej.domain.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BusinessVO {
    private int businessId;
    private String name;
    private String address;
    private double stars;
    private int reviewCount;
    private int isOpen;
    private String categories;
    private String bid;
    private double distance;
}
