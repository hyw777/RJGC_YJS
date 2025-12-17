// DishController.java
package com.sgrej.controller.Business;

import com.sgrej.domain.pojo.Dish;
import com.sgrej.service.DishService;
import com.sgrej.domain.pojo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据商户ID查询菜品列表
     */
    @GetMapping("/list")
    public ResponseResult<List<Dish>> listByBusinessId(@RequestParam int businessId) {
        try {
            List<Dish> dishes = dishService.selectDishesByBusinessId(businessId);
            log.info("查询菜品列表成功，结果为：{}", dishes);
            return ResponseResult.success(dishes);
        } catch (Exception e) {
            log.error("查询菜品列表失败", e);
            return ResponseResult.error("查询菜品列表失败");
        }
    }

    /**
     * 添加菜品
     */
    @PostMapping("/add")
    public ResponseResult<String> addDish(@RequestBody Dish dish) {
        try {
            dishService.addDish(dish);
            return ResponseResult.success("菜品添加成功");
        } catch (Exception e) {
            log.error("添加菜品失败", e);
            return ResponseResult.error("菜品添加失败");
        }
    }

    /**
     * 更新菜品
     */
    @PutMapping("/update")
    public ResponseResult<String> updateDish(@RequestBody Dish dish) {
        try {
            dishService.updateDish(dish);
            return ResponseResult.success("菜品更新成功");
        } catch (Exception e) {
            log.error("更新菜品失败", e);
            return ResponseResult.error("菜品更新失败");
        }
    }

    /**
     * 删除菜品
     */
    @DeleteMapping("/delete")
    public ResponseResult<String> deleteDish(@RequestParam Integer id) {
        try {
            dishService.deleteDish(id);
            return ResponseResult.success("菜品删除成功");
        } catch (Exception e) {
            log.error("删除菜品失败", e);
            return ResponseResult.error("菜品删除失败");
        }
    }
}
