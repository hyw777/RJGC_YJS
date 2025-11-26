package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusinessResultDTO {
    int businessId;
    int result;
}
