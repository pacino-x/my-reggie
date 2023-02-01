package com.xay.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xay.reggie.common.CustomException;
import com.xay.reggie.entity.Category;
import com.xay.reggie.entity.Dish;
import com.xay.reggie.entity.Setmeal;
import com.xay.reggie.mapper.CategoryMapper;
import com.xay.reggie.service.CategoryService;
import com.xay.reggie.service.DishService;
import com.xay.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果有则抛出业务异常
        if (count1 > 0) {
            //已经关联菜品，需要抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        //查询当前分类是否关联了套餐，如果有则抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        int count2 = setmealService.count();
        if (count2 > 0) {
            //已经关联套餐，需要抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }
        //正常进行删除分类
        super.removeById(id);
    }
}
