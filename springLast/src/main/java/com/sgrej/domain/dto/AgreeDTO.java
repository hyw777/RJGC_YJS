package com.sgrej.domain.dto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AgreeDTO {
    int result; //同意为1，不同意为0
    int applicantId; //申请人id
}
