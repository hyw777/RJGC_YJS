package com.sgrej.mapper;

import org.apache.ibatis.annotations.*;

import static com.baomidou.mybatisplus.core.assist.ISqlRunner.INSERT;

@Mapper
public interface User_RoleMapper {
    int insert(int userId,int roleId,int status);

    /**
     * 封禁/解封用户
     * @param userId
     * @param status
     */
    void setStatus(int userId, int status);

    /**
     * 查询用户状态
     * @param userId
     * @return
     */
    @Select("select status from user_role where user_id = #{userId} and status != -1")
    int getStatus(int userId);

    @Delete("delete from user_role where userId = #{userId} and status = #{status}")
    void delete(int userId,int status);

    @Update("update user_role set role_id = #{role} where user_id = #{userId}")
    void changeRole(int userId, int role);
}
