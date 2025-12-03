package com.sgrej.domain.dto;

import lombok.*;

import java.io.Serializable;
@Data
@Builder

@AllArgsConstructor
@Getter
@Setter
public class BusinessPageQueryDTO implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    
    public BusinessPageQueryDTO() {}
    
    public BusinessPageQueryDTO(String businessName) {
        this.businessName = businessName;
        this.page = 1;
        this.pageSize = 10;
    }
    
    public BusinessPageQueryDTO(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
    
    private int page;//页号

    private int pageSize;//页面大小

    private String businessName;//商铺名

    private String type;//商铺类型
}