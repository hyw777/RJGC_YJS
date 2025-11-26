package com.sgrej.mapper;

import com.sgrej.domain.dto.ApplicationDTO;
import com.sgrej.domain.pojo.Apply;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApplyMapper {

    @Insert("INSERT INTO application(applicant_id, recipient_id, time, reason,name) " +
            "VALUES (#{applicantId}, #{recipientId}, #{time, jdbcType=TIMESTAMP}, #{reason},#{name}) ")
    void add(ApplicationDTO applicationDTO);


    @Select("select count(id) from application where recipient_id = #{userId}")
    int getUnPass(int userId);

    @Select("select id,applicant_id,recipient_id,reason,time,name from application where recipient_id = #{userId}")
    List<Apply> getInfo(int userId);

    @Delete("delete from application where recipient_id = #{userId} and applicant_id = #{applicantId}")
    void delete(int userId,int applicantId);


}
