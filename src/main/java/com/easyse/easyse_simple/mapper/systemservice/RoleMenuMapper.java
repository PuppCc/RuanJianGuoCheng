package com.easyse.easyse_simple.mapper.systemservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.system.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 25405
* @description 针对表【system_role_menu(角色和菜单关联表)】的数据库操作Mapper
* @createDate 2022-12-07 15:08:51
* @Entity generator.pojo/DO/system.RoleMenu
*/
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<RoleMenu> roleMenuList);
}
