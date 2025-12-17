// DishServiceImpl.java
package com.sgrej.service.impl;

import com.sgrej.domain.pojo.Dish;
import com.sgrej.mapper.DishMapper;
import com.sgrej.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Dish> selectDishesByBusinessId(int businessId) {
        return dishMapper.selectDishesByBusinessId(businessId);
    }

    @Override
    public void addDish(Dish dish) {
        dishMapper.insert(dish);
    }

    @Override
    public void updateDish(Dish dish) {
        dishMapper.update(dish);
    }

    @Override
    public void deleteDish(Integer id) {
        dishMapper.deleteById(id);
    }
}
