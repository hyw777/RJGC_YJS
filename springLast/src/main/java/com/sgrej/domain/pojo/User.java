package com.sgrej.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
@Setter
@Getter
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;


    private String uid;//用来查找对应的信息

    /**
    * 主键
    */
    @TableId
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
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private int age;

    /**
     * 地址
     */
    private String city;

    /**
     * 已发表评价数量
     */
    private int reviewCount;

    /**
     * "酷"
     */
    private int cool;

    /**
     * 有用的
     */
    private Long useful;
    /**
     * 用户被标记为高级用户的年份
     */
    private String elite;
    /**
     * 注册时间
     */
    private Timestamp yelpingSince;
    /**
     * “有趣的”
     */
    private int funny;

    private String friends;//朋友

}