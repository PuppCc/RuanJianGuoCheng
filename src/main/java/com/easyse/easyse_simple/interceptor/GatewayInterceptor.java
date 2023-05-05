//package com.easyse.easyse_simple.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.easyse.easyse_simple.pojo.vo.ResultVO;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//
///**
// * @author: zky
// * @date: 2022/11/08
// * @description: 单体项目暂时不需要
// */
//@Component
//public class GatewayInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
//        String value = httpServletRequest.getHeader("key");
//        if("easyse".equals(value)) {
//            return true;
//        }else {
//            ResultVO result;
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.setContentType("application/json; charset=utf-8");
//            PrintWriter out = null;
//            try {
//
//                result = ResultVO.failure("权限不足，请通过网关访问");
//                out = httpServletResponse.getWriter();
//                out.append(JSON.toJSONString(result));
//            } catch (Exception e) {
//                httpServletResponse.sendError(500);
//            } finally {
//                if (out != null) {
//                    out.close();
//                }
//            }
//            return false;
//        }
//    }
//}
