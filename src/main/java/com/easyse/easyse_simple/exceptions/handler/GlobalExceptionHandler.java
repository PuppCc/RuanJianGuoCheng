package com.easyse.easyse_simple.exceptions.handler;


import com.easyse.easyse_simple.exceptions.ServiceException;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: zky
 * @date: 2022/10/19
 * @description: 全局异常处理类
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 结合validation处理参数异常
     * @param e 捕获的MethodArgumentNotValidException异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO validatedHandler(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        log.info(bindingResult.getFieldError().getDefaultMessage());
//        // 直接返回第一个字段存在的错误
//        return ResultVO.failure(bindingResult.getFieldError().getDefaultMessage());

        // 返回所有字段存在的参数错误
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO failure = ResultVO.failure();
        fieldErrors.forEach(fieldError -> {
            log.info(fieldError.getDefaultMessage());
            failure.data(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return failure;
    }


//    @ExceptionHandler({RuntimeException.class})
//    public ResultVO RuntimeExceptionhandler(Exception e, HttpServletRequest request, HttpServletResponse response){
//        log.error("服务器繁忙：" + e.getMessage());
//        return ResultVO.failure("服务器繁忙，请稍后再试！");
//    }

    @ExceptionHandler({ServiceException.class})
    public ResultVO ServiceExceptionhandler(Exception e, HttpServletRequest request, HttpServletResponse response){
        log.error("服务器繁忙：" + e.getMessage());
        return ResultVO.failure(e.getMessage());
    }



}
