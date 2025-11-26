package com.sgrej.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO implements Serializable {
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private String gender;
    /**
     * 地址
     */
    private String city;
    /**
     * 邮箱
     */
    private String email;
/**
 * 验证码
 */
   private String code;

}
