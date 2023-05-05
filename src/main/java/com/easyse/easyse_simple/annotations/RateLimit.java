package com.easyse.easyse_simple.annotations;

import java.lang.annotation.*;

/**
 * @author: zky
 * @date: 2022/12/13
 * @description: 接口防刷注解：默认是10秒内只能调用5次
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流的 key
     */
    String key() default "limit:";

    /**
     * 周期,单位是秒
     */
    int cycle() default 10;

    /**
     * 请求次数
     */
    int count() default 5;

    /**
     * 默认提示信息
     */
    String msg() default "请勿重复访问";


}