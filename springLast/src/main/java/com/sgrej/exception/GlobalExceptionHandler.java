package com.sgrej.exception;

import com.sgrej.domain.pojo.MessageConstant;
import com.sgrej.domain.pojo.ResponseResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ResponseResult exceptionHandler(RuntimeException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return ResponseResult.error(ex.getMessage());
    }

    /**
     * 处理sql异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ResponseResult exceptionHandler(SQLIntegrityConstraintViolationException ex) {
//        Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return ResponseResult.error(msg);
        }
    else

    {
        return ResponseResult.error(MessageConstant.UNKNOWN_ERROR);
    }
}
}
