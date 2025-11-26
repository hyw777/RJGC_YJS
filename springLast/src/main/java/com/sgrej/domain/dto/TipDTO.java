package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipDTO {
    private int tipId;//tipID

    private int userId;//用户id

    private int businessId;//商铺id

    private String text;//提示内容

    private String datePosted;//提示时间

}
