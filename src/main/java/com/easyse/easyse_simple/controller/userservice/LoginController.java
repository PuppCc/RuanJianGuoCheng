package com.easyse.easyse_simple.controller.userservice;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.annotations.Log;
import com.easyse.easyse_simple.constants.UserConstants;
import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.systemservice.LogininforService;
import com.easyse.easyse_simple.service.userserivce.ExitService;
import com.easyse.easyse_simple.service.userserivce.LoginService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.Constants;
import com.easyse.easyse_simple.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Objects;

import static com.easyse.easyse_simple.utils.ServiceConstants.PERSON_NAME;

@RestController
@RequestMapping("/user")
@Api(tags = "登录相关")
public class LoginController implements UserConstants {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ExitService exitService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;


    @PostMapping(value = "/login")
    @ApiOperation("用户登录")
    public ResultVO login(@RequestBody User user){

        // 记录登录信息到数据库
        loginService.recordLogininfor(user.getUsername(), Constants.LOGIN_SUCCESS, "登录成功");

        return loginService.doLogin(user);
    }

    @PostMapping(value = "/hello")
    @Log(title = PERSON_NAME, businessType = BusinessType.OTHER)
    public ResultVO hello(){
        ResultVO resultVO = new ResultVO();
        resultVO.build();
        resultVO = ResultVO.success("你好！");
        return resultVO;
    }

    @GetMapping(value = "/authcode")
//    @Log(title = PERSON_NAME, businessType = BusinessType.OTHER)
    @ApiOperation(value = "发送手机验证码:mode-1：注册、2：重置密码")
    public ResultVO authcode(@RequestParam("phonenumber") String u_phone, @RequestParam("mode")Integer mode) {
        if(mode == 2) {
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhonenumber, u_phone));
            if(Objects.isNull(user)) {
                return ResultVO.failure("该手机号未注册，请先注册！");
            }
        }
        String s = loginService.authcode_get(u_phone);
        return ResultVO.success(s);
    }

    @GetMapping(value = "/auth")
//    @Log(title = PERSON_NAME, businessType = BusinessType.OTHER)
    @ApiOperation(value = "验证手机验证码")
    public ResultVO auth(@RequestParam("phonenumber") String u_phone,
                         @RequestParam("code") String code) {
        if(!redisCache.getCacheObject(u_phone).equals(code)) {
            return ResultVO.failure("验证码错误！");
        }
        return ResultVO.success("验证成功");
    }

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册：authcode为收到对应的校验码")
    public ResultVO register(@RequestBody @Validated User user, @RequestParam("authcode") String authcode){
        if(StringUtils.isNotBlank(user.getPhonenumber())) {
            LambdaQueryWrapper<User> phonenumber = new LambdaQueryWrapper();
            phonenumber.eq(User::getPhonenumber, user.getPhonenumber());
            User phoneUser = userService.getOne(phonenumber);
            if(!Objects.isNull(phoneUser)) {
                return ResultVO.failure("该手机号已注册！");
            }
        }
        if(StringUtils.isNotBlank(user.getEmail())) {
            LambdaQueryWrapper<User> email = new LambdaQueryWrapper<>();
            email.eq(User::getEmail, user.getEmail());
            User emailuser = userService.getOne(email);
            if(!Objects.isNull(emailuser)) {
                return ResultVO.failure("该邮箱已注册！");
            }
        }
        if(Objects.isNull(user.getPhonenumber()) && Objects.isNull(user.getEmail())) {
            return ResultVO.failure("邮箱或手机号不能为空~");
        }
        return loginService.doRegister(user, authcode);
    }


    @PostMapping(value = "/out")
    @ApiOperation(value = "登出")
    public ResultVO logOut(@RequestBody User user){
        // 为了记录登出信息
        loginService.logOut(user);
        return exitService.logOut();
    }

    /**
     * 激活用户
     * @param userId
     * @param code 激活码
     * @return
     * http://localhost:8080/activation/activation/用户id/激活码
     */
    @GetMapping("/activation/{userId}/{code}")
    public ResultVO activation(@PathVariable("userId") Long userId,
                             @PathVariable("code") String code) {
        int result = loginService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            return ResultVO.success("激活成功, 您的账号已经可以正常使用!");
        }
        else if (result == ACTIVATION_REPEAT) {
            return ResultVO.failure("无效的操作, 您的账号已被激活过!");
        }
        else {
            return ResultVO.failure("激活失败, 您提供的激活码不正确!");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/resetpwd")
    @ApiOperation(value = "重置密码")
    public ResultVO reSetPwd(@RequestBody User user){
        int res = userService.updatePwd(user);
        if(res == USER_NOTFOUND) {
            return ResultVO.failure("该用户不存在");
        }
        if(res == 0){
            return ResultVO.failure("修改失败，请稍后再试~");
        }
        return ResultVO.success("修改用户密码成功");

    }
}
