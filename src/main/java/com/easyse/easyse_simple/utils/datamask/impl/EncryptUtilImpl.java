package com.easyse.easyse_simple.utils.datamask.impl;


import com.easyse.easyse_simple.annotations.EncryptTransaction;
import com.easyse.easyse_simple.utils.datamask.IEncryptUtil;
import com.easyse.easyse_simple.utils.encryption.DBAESUtil;
import org.springframework.stereotype.Component;


import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * @author zky
 * @create 2022/10/26
 **/
@Component
public class EncryptUtilImpl implements IEncryptUtil {

    @Override
    public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
            //取出所有被EncryptTransaction注解的字段
        for (Field field : declaredFields) {
            EncryptTransaction encryptTransaction = field.getAnnotation(EncryptTransaction.class);
            if (!Objects.isNull(encryptTransaction)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                //暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    //加密
                    try {
                        field.set(paramsObject, DBAESUtil.encrypt(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return paramsObject;
    }
}

