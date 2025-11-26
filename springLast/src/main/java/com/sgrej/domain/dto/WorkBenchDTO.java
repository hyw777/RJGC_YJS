package com.sgrej.domain.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WorkBenchDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;//开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;//结束时间

    private int businessId;


}
