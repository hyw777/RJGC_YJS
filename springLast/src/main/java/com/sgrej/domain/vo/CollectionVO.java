package com.sgrej.domain.vo;


import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CollectionVO {
    private BusinessVO1 businessVO1;//商铺

    private String userName;//对应的收藏用户

    private Timestamp date;//收藏时间

}
