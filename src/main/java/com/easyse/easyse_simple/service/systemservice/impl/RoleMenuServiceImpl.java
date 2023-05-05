package com.easyse.easyse_simple.service.systemservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.systemservice.RoleMenuMapper;
import com.easyse.easyse_simple.pojo.DO.system.RoleMenu;
import com.easyse.easyse_simple.service.systemservice.RoleMenuService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【system_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2022-12-07 15:08:51
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
implements RoleMenuService {

}
