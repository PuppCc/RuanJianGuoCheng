package com.easyse.easyse_simple.annotations;


import java.lang.annotation.*;

/**
 * @author: zky
 * @date: 2022/11/11
 * @description: 日志处理，当一个方法被访问需要日志记录时，加上这个注解即可
 * 用于解密请求体数据
 */

@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecryptBody {

}