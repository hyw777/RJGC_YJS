package com.sgrej.domain.pojo;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Apply {
    int id;
    int applicantId;
    int recipientId;
    Timestamp time;
    String reason;
    String name;
}
