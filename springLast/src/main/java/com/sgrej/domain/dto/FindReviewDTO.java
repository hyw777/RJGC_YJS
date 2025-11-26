package com.sgrej.domain.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindReviewDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;//开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;//结束时间

    private int pageSize;

    private int pageNum;
}
