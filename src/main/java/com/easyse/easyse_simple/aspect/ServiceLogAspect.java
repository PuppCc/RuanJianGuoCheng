package com.easyse.easyse_simple.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: zky
 * @date: 2022/10/26
 * @description: 日志记录1.0：默认会记录所有的用户访问的service服务
 */
@Component
@Aspect
@Slf4j
public class ServiceLogAspect {

    @Pointcut("execution(* com.easyse.*.service.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 用户[IP 地址], 在某个时间访问了 [com.easyse.xxx.service.xxx]
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return ;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info(String.format("用户[%s], 在[%s], 访问了[%s].", ip, time, target));
    }

}
