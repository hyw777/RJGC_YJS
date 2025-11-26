package com.sgrej.domain.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FindfriendVO {

    private String name;
    private String city;
    private int reviewCounts;
    private int friendNumbers;
    private int userId;
    private String uid;

}
