package com.sgrej.domain.pojo;


import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Collection {
    private int userId;
    private int businessId;
    private Timestamp date;
}
