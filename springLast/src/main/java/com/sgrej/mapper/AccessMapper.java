package com.sgrej.mapper;

import com.sgrej.domain.dto.AccessDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper
public interface AccessMapper {
    @Insert("INSERT INTO access(business_id, user_id, time)  VALUES (#{businessId}, #{userId}, #{time})")
    void addRecord(AccessDTO accessDTO);

    @Select("select count(business_id) from access where business_id = #{businessId}" +
            " and time >= #{beginTime} and time <= #{endTime}")
    Integer getReviewCount(Timestamp beginTime, Timestamp endTime, int businessId);

    @Select("select count(business_id) from access where business_id = #{businessId}")
    int getTotal(int businessId);
}
