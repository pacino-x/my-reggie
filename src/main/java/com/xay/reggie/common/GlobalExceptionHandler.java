package com.xay.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理,是通过代理实现的
 */

/**
 * RestController.class, Controller.class
 * 加了以上两个注解的类，就会被异常处理器捕获处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler (SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return    R.error(msg);
        }

        return R.error("未知错误");
    }


        /**
         * 异常处理方法,把自定义的保存信息获取
         * @param ex
         * @return
         */
        @ExceptionHandler(CustomException.class)
        public R<String> exceptionHandler(CustomException ex) {
            log.error(ex.getMessage());


            return R.error(ex.getMessage());
        }
}
