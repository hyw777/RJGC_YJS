package com.sgrej.domain.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditUserDTO {
    /**
     * 用户id
     */
    private int userId;

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

    private int reviewCount;//评价总数

    private int cool;

    private int useful;

    private int funny;
}
