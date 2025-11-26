package com.sgrej.domain.vo;


import lombok.*;

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

    private String categories;

    private String hours;

    private String bid;

    private String goodForKids;//是否对孩子友好

    private String businessAcceptsCreditcards;//是否接受刷卡

    private String applyTime;

}
