package com.sgrej.mapper;

import com.sgrej.domain.pojo.Collection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface CollectMapper{

    @Insert("insert into collection values (#{uid},#{businessId},#{date})")
    void save(int businessId, int uid, Timestamp date);

    @Select("Select * from collection where user_id = #{uid}")
    List<Collection> getCollections(int uid);


    /**
     * 查找最近一个月内的收藏信息
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("Select user_id,business_id,date from collection where user_id = #{userId} and date >= #{startTime} and date <= #{endTime}")
    List<Collection> getCollectionsLimitTime(int userId, Timestamp startTime, Timestamp endTime);

    /**
     * 删除收藏
     * @param businessId
     * @param uid
     */
    @Delete("delete from collection where user_id = #{uid} and business_id = #{businessId}")
    void delete(int businessId, int uid);
}
