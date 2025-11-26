package com.sgrej.domain.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationDTO {

    int applicantId;
    int recipientId;
    Timestamp time;
    String reason;
    String name;
}
