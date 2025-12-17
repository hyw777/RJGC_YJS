// Dish.java
package com.sgrej.domain.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Dish {
    private Integer id;
    private String businessId;
    private String dishName;
    private BigDecimal price;
    private String photoId;
    private Boolean isOnSale;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // Getters and Setters
}
