package com.easyse.easyse_simple.annotations;

import java.lang.annotation.*;

/**
 * @author: zky
 * @date: 2022/11/11
 * @description:
 * 该注解定义在类上
 * 插件通过扫描类对象是否包含这个注解来决定是否继续扫描其中的字段注解
 * 这个注解要配合EncryptTransaction注解
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {

}
