package com.easyse.easyse_simple.filter;

import com.easyse.easyse_simple.pojo.DO.DTO.LoginUser;
import com.easyse.easyse_simple.utils.JwtUtil;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import com.easyse.easyse_simple.utils.UserHolder;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

//    @Autowired
//    AuthenticationManager authenticationManager;

    @Autowired
    RedisCache redisCache;

    @Autowired
    UserHolder userHolder;

    @Override
    public void destroy() {
        userHolder.clear();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //token为空 放行
            filterChain.doFilter(request,response);
        //            return;
        }else {
            String userid = "";
            // 解析token
            try {
                Claims claims = JwtUtil.parseJWT(token);
                userid = claims.getSubject();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("token非法！-!");
            }
            // TODO 无法解析bug 待解决
            LoginUser loginUser = redisCache.getCacheObject(RedisKeyUtil.getUserKey(Long.parseLong(userid)));
            // 存入 UserHolder 中，方便后续取出, 可能会出问题
            userHolder.setUser(loginUser.getUser());



            if(Objects.isNull(loginUser)){
                //没获取到用户信息
                throw new RuntimeException("用户未登录!-！");
            }
            // 存入SecurityContextHolder
            // 必须用三个参数的构造函数

            //TODO 获取权限信息封装到authentication

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 放行
            filterChain.doFilter(request,response);
        }



    }
}
