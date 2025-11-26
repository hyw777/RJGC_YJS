package com.sgrej.domain.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApplicationVO {
    private int id;
    private String reason;
    private String name;
}
