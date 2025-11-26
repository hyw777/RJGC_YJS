package com.sgrej.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhotoMapper {

    //查询商家对应的一张图片
    @Select("select min(photo_id) from photos where business_id = #{bid} ")
    String selectImage(String bid);

    //查询商家对应的所有图片
    @Select("select photo_id from photos where business_id = #{bid}")
    List<String> selectImages(String bid);


    @Insert("Insert into photos(business_id,photo_id) values (#{businessId},#{path})")
    void addImage(String path, String businessId);

    @Delete("delete from photos where photo_id = #{fileName} and business_id = #{businessId}")
    void deleteImage(String fileName, String businessId);
}
