package com.easyse.easyse_simple.interceptor;

import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;




/**
 * @author: zky
 * @date: 2022/12/13
 * @description: 防刷限流的拦截器
 */
@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> redisLuaScript;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果请求的是方法，则需要做校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取目标方法上是否有指定注解
            RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
            if (rateLimit == null) {
                //说明目标方法上没有 RateLimit 注解
                return true;
            }
            // 代码执行到此，说明目标方法上有 RateLimit 注解，所以需要校验这个请求是不是在刷接口
            // 获取请求IP地址
            String ip = getIpAddr(request);
            // 请求url路径
            String uri = request.getRequestURI();
            //存到redis中的key
            String key = "RateLimit:" + ip + ":" + uri;

            // 1.0
            // 缓存中存在key，在限定访问周期内已经调用过当前接口
//            if (redisTemplate.hasKey(key)) {
//                // 访问次数自增1
//                redisTemplate.opsForValue().increment(key, 1);
//                // 超出访问次数限制
//                if (redisTemplate.opsForValue().get(key) > rateLimit.count()) {
//                    throw new ServiceException(rateLimit.msg());
//                }
//                // 未超出访问次数限制，不进行任何操作，返回true
//            } else {
//                // 第一次设置数据,过期时间为注解确定的访问周期
//                redisTemplate.opsForValue().set(key, 1, rateLimit.cycle(), TimeUnit.SECONDS);
//            }

            //将key转成List类型
            List<String> keys = Collections.singletonList(key);

            // 2.0 调用Lua脚本
            Long number = redisTemplate.execute(redisLuaScript, keys, rateLimit.count(), rateLimit.cycle());

            if (number != null && number.intValue() != 0 && number.intValue() <= rateLimit.count()) {
                log.warn(ip + " " + rateLimit.cycle() + "秒内访问第：" + number.toString() + " 次" + getCurrentTime());
                return true;
            }
            throw new ServiceException(rateLimit.msg());
        }
        // 如果请求的不是方法，直接放行
        return true;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取请求的归属IP地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
