package com.sgrej.mapper;

import com.sgrej.domain.pojo.Dish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据商户ID查询菜品列表
     */
    @Select("select * from merchant_dishes where business_id = #{businessId}")
    List<Dish> selectDishesByBusinessId(int businessId);

    /**
     * 插入新菜品
     */
    @Insert("INSERT INTO merchant_dishes(business_id, dish_name, price, photo_id, is_on_sale) " +
            "VALUES(#{businessId}, #{dishName}, #{price}, #{photoId}, #{isOnSale})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dish dish);

    /**
     * 更新菜品信息
     */
    @Update("UPDATE merchant_dishes SET dish_name = #{dishName}, price = #{price}, " +
            "photo_id = #{photoId}, is_on_sale = #{isOnSale} WHERE id = #{id}")
    void update(Dish dish);

    /**
     * 根据ID删除菜品
     */
    @Delete("DELETE FROM merchant_dishes WHERE id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据ID查询菜品
     */
    @Select("SELECT * FROM merchant_dishes WHERE id = #{id}")
    Dish getById(Integer id);
}
