package com.sgrej.domain.vo;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProfileInfoVO {
        // 对应数据库字段id
        private Long id;

        // 对应数据库字段user_group_name
        private String userGroupName;

        // 对应数据库字段condition_json_str
        private String conditionJsonStr;

        // 对应数据库字段condition_comment
        private String conditionComment;

        // 对应数据库字段user_group_num
        private Integer userGroupNum;

        // 对应数据库字段update_type
        private String updateType;

        // 对应数据库字段user_group_comment
        private String userGroupComment;

        // 对应数据库字段update_time
        private Timestamp updateTime;

        // 对应数据库字段create_time
        private Timestamp createTime;
}
