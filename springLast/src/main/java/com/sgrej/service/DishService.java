// DishService.java
package com.sgrej.service;

import com.sgrej.domain.pojo.Dish;

import java.util.List;

public interface DishService {

    /**
     * 根据商户ID查询菜品列表
     */
    List<Dish> selectDishesByBusinessId(int businessId);

    /**
     * 添加菜品
     */
    void addDish(Dish dish);

    /**
     * 更新菜品
     */
    void updateDish(Dish dish);

    /**
     * 删除菜品
     */
    void deleteDish(Integer id);
}
