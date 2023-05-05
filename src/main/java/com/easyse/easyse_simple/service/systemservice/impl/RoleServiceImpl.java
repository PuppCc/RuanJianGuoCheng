package com.easyse.easyse_simple.service.systemservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.systemservice.RoleMapper;
import com.easyse.easyse_simple.pojo.DO.system.Role;
import com.easyse.easyse_simple.service.systemservice.RoleService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【system_role(角色信息表)】的数据库操作Service实现
* @createDate 2022-12-07 15:08:51
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
implements RoleService {

}
