package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileQueryDTO {
    private int PageNum;
    private int PageSize;
    private String profileId;
}
