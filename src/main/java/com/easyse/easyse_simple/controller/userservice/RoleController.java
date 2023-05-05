package com.easyse.easyse_simple.controller.userservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.annotations.Log;
import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.exceptions.ServiceException;
import com.easyse.easyse_simple.pojo.DO.system.Role;
import com.easyse.easyse_simple.pojo.DO.system.RoleMenu;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.vo.AjaxResult;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.systemservice.PermissionService;
import com.easyse.easyse_simple.service.systemservice.RoleMenuService;
import com.easyse.easyse_simple.service.systemservice.RoleService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.MyBeansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.easyse.easyse_simple.constants.Constants.USER_NOTFOUND;
import static com.easyse.easyse_simple.pojo.vo.AjaxResult.error;
import static com.easyse.easyse_simple.pojo.vo.AjaxResult.success;

/**
 * @author: zky
 * @date: 2022/12/14
 * @description:
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    RoleMenuService roleMenuService;

//    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    @RateLimit
    public ResultVO list()
    {
        List<Role> roles = roleService.list();
        ResultVO success = ResultVO.success("查询角色列表成功！");
        success.data("roles", roles);
        return success;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改对应角色")
    public ResultVO updateUser(@PathVariable("id") Long id,
                               @RequestBody Role role){
        Role oldRole = roleService.getById(id);
        if(Objects.isNull(role)) {
            throw new ServiceException("对应角色不存在");
        }
        MyBeansUtils.copyPropertiesIgnoreNull(role, oldRole);
        if(roleService.save(oldRole)) {
            return ResultVO.success("修改角色信息成功");
        } else {
            return ResultVO.failure("修改角色信息失败");
        }
    }

    @Log(title = "角色权限管理", businessType = BusinessType.GRANT)
    @PutMapping("/auth/{id}")
    public AjaxResult insertAuthRole(@PathVariable("id") Long roleId, Long[] menuIds)
    {
        if(Objects.isNull(menuIds)) {
            throw new ServiceException("菜单信息不能为空");
        }
        LambdaQueryWrapper<RoleMenu> roleMenuwp = new LambdaQueryWrapper<>();
        roleMenuwp.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(roleMenuwp);
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(aLong -> {
            RoleMenu roleMenu = new RoleMenu(roleId, aLong);
            roleMenus.add(roleMenu);
        });
        if(roleMenuService.saveOrUpdateBatch(roleMenus)) {
            return success("权限修改成功！");
        } else {
            return error("权限修改修改！");
        }

    }


}
