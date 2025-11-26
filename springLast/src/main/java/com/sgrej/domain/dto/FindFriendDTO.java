package com.sgrej.domain.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindFriendDTO {
    private String userName;

    private String email;

    private int pageSize;

    private int pageNum;
}
