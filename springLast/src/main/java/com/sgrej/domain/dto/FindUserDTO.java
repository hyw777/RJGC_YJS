package com.sgrej.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindUserDTO {
    /**
     * 邮箱号
     */
    private String email;
    /**
     * 验证码
     */
    private String code;
    /**
     * 新密码
     */
    private String newPassword;
}
