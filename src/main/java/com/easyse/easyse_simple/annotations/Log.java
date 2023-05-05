package com.easyse.easyse_simple.annotations;

import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.enums.OperatorType;

import java.lang.annotation.*;


/**
 * @author: zky
 * @date: 2022/11/11
 * @description: 日志处理，当一个方法被访问需要日志记录时，加上这个注解即可
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
