package com.easyse.easyse_simple.service.userserivce.impl;


import com.easyse.easyse_simple.pojo.DO.DTO.LoginUser;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.userserivce.ExitService;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
* 实现退出功能
*
* */
@Service
public class ExitServiceImpl implements ExitService {

    @Autowired
    RedisCache redisCache;
//TODO redis配置

    @Override
    public ResultVO logOut() {
        //获取用户框架容器里的y用户id

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginuser = (LoginUser) authentication.getPrincipal();
        String id = loginuser.getUser().getId().toString();

        //删除缓存里的值
        redisCache.deleteObject(RedisKeyUtil.getUserKey(Long.parseLong(id)));

        return null;
//        return new ResultVO(200,"注销成功!-！");
    }
}
