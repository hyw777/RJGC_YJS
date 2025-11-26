package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileDTO {
    private String id;
    private String groupName;
    private int pageNum;
    private int pageSize;
}
