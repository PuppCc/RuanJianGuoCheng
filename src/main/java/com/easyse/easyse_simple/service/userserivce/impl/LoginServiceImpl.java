package com.easyse.easyse_simple.service.userserivce.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.mapper.userservice.UserRoleMapper;
import com.easyse.easyse_simple.pojo.DO.DTO.LoginUser;
import com.easyse.easyse_simple.pojo.DO.Sms;
import com.easyse.easyse_simple.pojo.DO.system.UserRole;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.system.Logininfor;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.userserivce.LoginService;
import com.easyse.easyse_simple.service.systemservice.LogininforService;
import com.easyse.easyse_simple.service.userserivce.UserRoleService;
import com.easyse.easyse_simple.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.easyse.easyse_simple.constants.UserConstants.*;
import static com.easyse.easyse_simple.utils.Convert.toLong;


@Service
@Slf4j
public class LoginServiceImpl implements LoginService {


    // 网站域名
    @Value("${easyse.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port}")
    private String port;

    @Autowired
    RedisCache redisCache;
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private LogininforService logininforService;

    @Autowired
    private UserHolder userHolder;

    @Override
    public ResultVO doLogin(User user) {
    //authenticate进行用户认证
        //用户名和密码封装成authentication对象 两个参数的构造传入的是username和password
        // 需求有问题，导致这里使用的username其实是邮箱或者手机号
        String username = user.getEmail() == null ? user.getPhonenumber() : user.getEmail();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,user.getPassword());

        Authentication authentication =  authenticationManager.authenticate(authenticationToken);
            //认证失败给出提示信息
        if(Objects.isNull(authentication)) {
            throw new RuntimeException("认证失败!");
        }
        //认证成功 用userid生成一个jwt 存入responseResult返回
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);

        //把完整的用户信息存入redis userid作为key 由于我们没有redis 就暂时放到缓存中
//        HashMap<String,String> map = new HashMap<>();
//        map.put("token",jwt);

        // 放到redis缓存中
        redisCache.setCacheObject(RedisKeyUtil.getUserKey(Long.parseLong(id)), loginUser);
        // System.out.println("jwt:"+jwt);
        log.info(RedisKeyUtil.getUserKey(Long.parseLong(id)) + RedisKeyUtil.SPLIT + loginUser.getUser().getUsername());
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, loginUser.getUser().getId()));
        if(Objects.isNull(userRole)) {
            userRole = UserRole.builder()
                    .roleId(toLong(3))
                    .build();
        }
        return ResultVO.success("登陆成功").data("token", jwt).data("userMsg", loginUser.getUser()).data("roleId", userRole.getRoleId());

    }

    @Override
    public ResultVO doRegister(User user, String authcode) {

        if(!redisCache.getCacheObject(user.getPhonenumber()).equals(authcode)){
            return ResultVO.failure("手机验证码验证失败！");
        }

        ResultVO resultVO;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = user.getPassword();
        //密码加密
        pw = passwordEncoder.encode(pw);
        user.setPassword(pw);
        int insert = userMapper.insert(user);
        if(insert==1){
            return ResultVO.success("注册成功!");
        }else{
            return ResultVO.failure("failed！插入失败，请稍后再试！");
        }
//        return resultVO;

        // 给注册用户发送激活邮件
//        Context context = new Context();
//        context.setVariable("email", user.getEmail());
//        // http://localhost:8080/activation/{{userId}}/{{code}}
//        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
//        context.setVariable("url", url);
//        String content = templateEngine.process( "/mail/activation1.html", context);
//        mailClient.sendMail(user.getEmail(),"激活 easyse 账号", content);
//        // 记录日志
//        recordLogininfor(user.getUsername(), Constants.REGISTER, "注册成功");
//        return  resultVO;
    }



    public String authcode_get(String u_phone) {
        String authcode = "1"+ RandomStringUtils.randomNumeric(5);//生成随机数,我发现生成5位随机数时，如果开头为0，发送的短信只有4位，这里开头加个1，保证短信的正确性
        // 将验证码存入缓存
        redisCache.setCacheObject(u_phone, authcode, 5, TimeUnit.MINUTES);
        // 发送短息
        Sms.messagePost(u_phone, authcode);
        return "发送成功" + authcode;
    }


    /**
     * 激活用户
     * @param userId 用户 id
     * @param code 激活码
     * @return
     */
    public int activation(Long userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getIsActived() == 1) {
            // 用户已激活
            return ACTIVATION_REPEAT;
        }
        else if (user.getActivationCode().equals(code)) {
            // 修改用户状态为已激活
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }
        else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 记录用户登出
     */
    public void logOut(User user){
        recordLogininfor(user.getUsername(), Constants.LOGOUT, "退出成功");
        // 清除UserHolder
        userHolder.clear();;
    }

    /**
     * 使用自定义线程池记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    @Async("logthreadpool")
    public void recordLogininfor(String username, String status, String message)
    {
        Logininfor logininfor = new Logininfor();
        logininfor.setUsername(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        logininforService.save(logininfor);
    }
}
