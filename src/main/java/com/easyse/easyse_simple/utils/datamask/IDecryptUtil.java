package com.easyse.easyse_simple.utils.datamask;

/**
 * @author zky
 * @create 2022/10/26
 **/
public interface IDecryptUtil {
    /**
     * 解密
     *
     * @param result resultType的实例
     * @return T
     * @throws IllegalAccessException 字段不可访问异常
     */
    <T> T decrypt(T result) throws IllegalAccessException;
}

