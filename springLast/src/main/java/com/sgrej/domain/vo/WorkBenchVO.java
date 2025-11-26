package com.sgrej.domain.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WorkBenchVO {
    private int totalVisitCount;//总访客量

    private int lastDayVisitCount;//今日访客量

    private int newVisits;//新增访客量

    private String xIndex;

    private String yIndex;

}
