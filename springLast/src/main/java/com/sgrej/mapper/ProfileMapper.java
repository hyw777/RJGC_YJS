package com.sgrej.mapper;

import com.sgrej.domain.vo.ProfileInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProfileMapper {
    @Select("select * from user_group")
    List<ProfileInfoVO> getCategories();
}
