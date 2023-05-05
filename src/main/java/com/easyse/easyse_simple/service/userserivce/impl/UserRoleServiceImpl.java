package com.easyse.easyse_simple.service.userserivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.userservice.UserRoleMapper;
import com.easyse.easyse_simple.pojo.DO.system.UserRole;
import com.easyse.easyse_simple.service.userserivce.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author: zky
 * @date: 2022/12/14
 * @description:
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {
}
