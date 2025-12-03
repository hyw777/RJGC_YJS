package com.sgrej.mapper;

import com.sgrej.domain.dto.TipDTO;
import com.sgrej.domain.pojo.Tips;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TipMapper {
    @Select("select * from niche_tips where user_id = #{userId}")
    List<Tips> searchByUid(int userId);

    @Select("select tip_id,user_id,business_id,text,date,compliment_count from tips where business_id = #{businessId}")
    List<Tips> searchByBid(String bId);

    /**
     * 添加tip
     * @param tipDTO
     */
    void addTip(TipDTO tipDTO);

    /**
     * 删除tip
     * @param tipId
     */
    @Delete("delete from niche_tips where tip_id = #{tipId}")
    void delete(int tipId);
}
