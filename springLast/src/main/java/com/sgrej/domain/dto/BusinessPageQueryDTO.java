package com.sgrej.domain.dto;

import lombok.*;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusinessPageQueryDTO implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    private int page;//页号

    private int pageSize;//页面大小

    private String businessName;//商铺名

    private String type;//商铺类型
}
