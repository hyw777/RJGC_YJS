package com.sgrej.mapper;


import com.github.pagehelper.Page;
import com.sgrej.domain.dto.BusinessPageQueryDTO;
import com.sgrej.domain.pojo.Business;
import com.sgrej.domain.vo.BusinessVO;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface BusinessMapper {

    /**
     * 获取前5个星级为5的商家
     * @return
     */
    @Select("SELECT business_id, name, address, city, state, postal_code, latitude, longitude, stars, review_count, is_open, categories, attributes, hours, created_at, updated_at, bid " +
            "FROM business " +
            "WHERE stars = 5 AND is_open = 1 " +
            "ORDER BY review_count DESC " +
            "LIMIT 5")
    List<BusinessVO> getTop5BusinessWith5Stars();

    /**
     * 根据id查询商户名
     * @param businessId
     * @return
     */
    @Select("select name from business where bid =  #{businessId} and is_open != -1")
    String selectBusinessName(String businessId);


    //分页查询，用于首页展示结果和搜索展示结果
    Page<BusinessVO> pageQuery(BusinessPageQueryDTO businessPageQueryDTO);

    //查找商户详细信息
    @Select("SELECT business_id, name, address, city, state, postal_code, latitude, longitude, stars, review_count, is_open, categories, attributes, hours, created_at, updated_at, bid FROM business WHERE business_id = #{businessId}")
    Business selectDetailById(int bId);


    @Select("select business_id from user_business where user_id = #{uerId}")
    int selectByUId(int userId);

    /**
     * 修改商户基本信息
     * @param business
     */
    void changeInfo(Business business);

    /**
     * 查询附近商家
     * @param city
     * @return
     */
    List<BusinessVO> getNearBy(String city);

    /**
     * 根据id查询商铺信息
     * @param bId
     */
    @Select(" select business_id,name,address,stars,review_count,is_open,categories,bid from business " +
            "where business_id = #{bId}  and is_open != -1" )
    BusinessVO getById(int bId);

    /**
     * 注册商家
     *
     * @param business
     * @return
     */
    int register(Business business);

    @Insert("insert into user_business(user_id,business_id) values (#{userId},#{businessId})")
    void bindBusiness(int userId, int businessId);

    @Select("select business_id,name,address,city,state,postal_code,latitude,longitude,stars,review_count,is_open,categories,attributes,hours,created_at,updated_at,bid" +
            "  from business where is_open = -1 and business_id ")
    List<Business> getApplys();

    @Delete("delete from business where business_id = #{businessId}")
    void delete(int businessId);


    @Update("update business set is_open = 1 where business_id = #{busineeId}")
    void online(int businessId);

    @Insert("insert into hot_search(content,`time`) values (#{businessName},#{time})")
    void saveSearchContent(String businessName, Timestamp time);

    List<BusinessVO> selectAllBusinesses(@Param("start") int start, @Param("pageSize") int pageSize);

    int countAllBusinesses();

    void updateBusinessStatus(@Param("businessId") Integer businessId, @Param("isOpen") Integer isOpen);

}
