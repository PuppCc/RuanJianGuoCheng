package com.easyse.easyse_simple.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.easyse.easyse_simple.annotations.ApiIdempotent;
import com.easyse.easyse_simple.utils.ServletUtils;
import com.easyse.easyse_simple.utils.TokenUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author: zky
 * @date: 2022/12/13
 * @description: aoi幂等性接口拦截器
 */
@Component
public class ApiIdempotentInterceptor implements HandlerInterceptor {

    private final String TOKEN_NAME = "idempotentToken";

    @Autowired
    private TokenUtilService tokenUtilService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //校验是否有执行方法
        if (!(handler instanceof HandlerMethod)) {
            return true;//若没有对应的方法执行器，就直接放行
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotent annotation = method.getAnnotation(ApiIdempotent.class);
        //若是没有幂等性注解直接放行
        if (annotation != null) {
            // 解析对应的请求头
            String token = request.getHeader(TOKEN_NAME);
            if (ObjectUtils.isEmpty(token)) {
                ServletUtils.renderString(response, "请携带token令牌");
                return false;
            }
            // 若是校验失败直接进行响应
            if (!tokenUtilService.validToken(token, "changlu")) {
                ServletUtils.renderString(response, "重复提交失败！");
                return false;
            }
        }
        return true;
    }
}
