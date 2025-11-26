package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewSatisfactionDTO {

    private int reviewId;
    //0表示有，1表示没有
    private int useful;//有用的

    private int funny;//有趣的

    private int cool;//酷的
}
