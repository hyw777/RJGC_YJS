package com.sgrej.domain.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePwdDTO {
    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     * 新密码
     */
    private String newPwd;

    /**
     * 确认密码
     */
    private String rePwd;

}
