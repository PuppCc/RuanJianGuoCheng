package com.easyse.easyse_simple.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zky
 * @date: 2022/12/13
 * @description: 接口幂等性
 */
@Target(ElementType.METHOD)  //使用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时
public @interface ApiIdempotent {

}
