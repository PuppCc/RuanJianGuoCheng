package com.easyse.easyse_simple.service.userserivce.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.exceptions.ServiceException;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.pojo.DO.DTO.LoginUser;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.service.userserivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
/*
实现用户信息验证类 到数据库中校验用户信息
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 需求错误，这里的username其实是邮箱或者手机号
     * @param username the username identifying the user whose data is required.
     *
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username)   {
        // 查询用户信息
        LambdaQueryWrapper<User> emailqueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> phonequeryWrapper = new LambdaQueryWrapper<>();
        User user = null;
        emailqueryWrapper.eq(User::getEmail,username);
        phonequeryWrapper.eq(User::getPhonenumber,username);
        try {
             user = userMapper.selectOne(emailqueryWrapper);
             if(user == null) {
                 user = userMapper.selectOne(phonequeryWrapper);
             }
        } catch (Exception e ){
            e.printStackTrace();
        }

        //如果没查到这个用户
        if(user == null){
            throw new ServiceException("用户不存在!");
        }

        //查到了这个用户 查询它的权限信息
        //TODO 查询user的权限信息
        List<String> perms = userService.selectPermsByUserId(user.getId());

//        List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        //把数据封装成UserDetails返回
        return new LoginUser(user, perms);
    }
}
