package com.sgrej.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserBusinessMapper {

    @Select("select user_id from user_business where business_id = #{businessId}")
    int getBossId(int businessId);

    @Delete("delete from user_business where business_id = #{businessId}")
    void delete(int businessId);
}
