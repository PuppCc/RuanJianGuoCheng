package com.easyse.easyse_simple.service.userserivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.exceptions.ServiceException;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.pojo.DO.DTO.SysUser;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.service.systemservice.MenuService;
import com.easyse.easyse_simple.utils.SecurityUtils;
import com.easyse.easyse_simple.utils.SpringUtils;
import com.easyse.easyse_simple.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.easyse.easyse_simple.constants.UserConstants.USER_NOTFOUND;

/**
* @author 25405
* @description 针对表【common_user】的数据库操作Service实现
* @createDate 2022-10-05 17:44:32
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Autowired
    MenuService systemClient;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        return systemClient.selectPermsByUserId(id);
    }

    @Override
    public int updatePwd(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhonenumber, user.getPhonenumber());
        User one = userMapper.selectOne(wrapper);
        if(Objects.isNull(one)) {
            return USER_NOTFOUND;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        one.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userMapper.updateById(one);
    }

}




