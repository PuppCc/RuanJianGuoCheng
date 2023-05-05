package com.easyse.easyse_simple.config;

import com.easyse.easyse_simple.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *  开启授权配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter filter;

//    @Autowired
//    private GateWayFileter gateWayFileter;

    // 密码加密方式
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 管理用户授权和访问
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManagerBean();

    }


    /**
     * 下面类是负责认证相关的配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭csrf
                .csrf().disable()
                // 不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/**","/user/hello","/tasks/**").anonymous()
                // 权限登录的时候需要访问
                .antMatchers("/system/permission/**").anonymous()
                .antMatchers("/system/**").anonymous() // 开发中。 方便调试，设置为匿名访问
                .antMatchers("/techqa/**").anonymous()
                .antMatchers("/techshare/**").anonymous()// 开发中。 方便调试，设置为匿名访问
                // 拥有管理员权限
//                .antMatchers("/system/**").hasAuthority("system")
//                .antMatchers("/techqa/add").hasAuthority("techqa")
                // 除上面外的所有请求全部需要鉴权认证
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources", "/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().authenticated();

        // 添加token过滤器 把它添加在up过滤器之前
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // 请求必须通过网关访问
//        http.addFilterBefore(gateWayFileter,JwtAuthenticationTokenFilter.class);

    }


    // 静态资源配置
    @Override
    public void configure(WebSecurity web) throws Exception {
        //swagger2所需要用到的静态资源，允许访问
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html/**",
                "/webjars/**",
                "/**");
        //  解决静态资源被拦截的问题
        web.ignoring().antMatchers("/*.html", "/css/**","/img/**","/js/**","/*.ico", "/static/*");

    }

}
