package com.sgrej.mapper;

import com.sgrej.domain.pojo.News;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewMapper {

    /**
     * 增加新消息
     * @param news
     */
    @Insert("insert into news(time,content,user_id) values (#{time},#{content},#{userId})")
    void add(News news);

    @Select("select id,time,content,user_id from news where user_id = -1 or user_id = #{userId}")
    List<News> getWithMe(int userId);

    @Delete("delete from news where user_id =  #{userId}")
    void deleteInUser(int userId);

    @Delete("DELETE FROM news WHERE time < DATE_SUB(NOW(), INTERVAL 3 DAY)")
    void deleteInTime();
}
