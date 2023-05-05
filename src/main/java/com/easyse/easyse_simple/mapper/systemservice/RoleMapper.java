package com.easyse.easyse_simple.mapper.systemservice;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.system.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 25405
* @description 针对表【system_role(角色信息表)】的数据库操作Mapper
* @createDate 2022-12-07 15:08:51
* @Entity generator.pojo/DO/system.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<Role> selectRoleList(Role role);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<Role> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<Role> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public Role selectRoleById(Long roleId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    public List<Role> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    public Role checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    public Role checkRoleKeyUnique(String roleKey);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(Role role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(Role role);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] roleIds);

}
