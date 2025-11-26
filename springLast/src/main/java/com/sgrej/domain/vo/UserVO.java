package com.sgrej.domain.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.sgrej.domain.pojo.Review;
import com.sgrej.domain.pojo.Tips;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserVO {
    /**
     * 用户名
     */
    private String name;

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

    /**
     * 用户评论
     */
    private List<ReviewVO> reviewsList;


}
