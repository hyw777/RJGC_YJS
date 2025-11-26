package com.sgrej.domain.pojo;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Checkin implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 商户id
     */
    private int businessId;
    /**
     * 用户签到时间
     */
    private String dates;
}
