package com.sgrej.domain.pojo;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Tips implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * tipId
     */
    private int tipId;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 商户id
     */
    private int businessId;
    /**
     * tip内容
     */
    private String text;
    /**
     * 发表时间
     */
    private String datePosted;
    /**
     * 收到的赞美次数
     */
    private String complimentCount;

}
