package com.sgrej.domain.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccessDTO {

    private int businessId;

    private int userId;

    private Timestamp time;

}
