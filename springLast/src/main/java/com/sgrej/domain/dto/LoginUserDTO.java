package com.sgrej.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO implements Serializable {
    /**
     * 邮箱号
     */
    private String email;
    /**
     * 密码
     */
    private String password;
}
