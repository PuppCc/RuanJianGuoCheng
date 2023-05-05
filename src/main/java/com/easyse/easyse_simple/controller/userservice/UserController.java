package com.easyse.easyse_simple.controller.userservice;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.easyse.easyse_simple.annotations.Log;
import com.easyse.easyse_simple.annotations.RateLimit;
import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.exceptions.ServiceException;
import com.easyse.easyse_simple.exceptions.UserNotFoundException;
import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.mapper.userservice.UserRoleMapper;
import com.easyse.easyse_simple.pojo.DO.system.RoleMenu;
import com.easyse.easyse_simple.pojo.DO.system.UserRole;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.vo.AjaxResult;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.systemservice.RoleMenuService;
import com.easyse.easyse_simple.service.userserivce.UserRoleService;
import com.easyse.easyse_simple.service.userserivce.UserService;
import com.easyse.easyse_simple.utils.MyBeansUtils;
import com.mchange.v2.beans.BeansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.nio.file.CopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.easyse.easyse_simple.constants.Constants.USER_NOTFOUND;
import static com.easyse.easyse_simple.pojo.vo.AjaxResult.error;
import static com.easyse.easyse_simple.pojo.vo.AjaxResult.success;

/**
 * @author: zky
 * @date: 2022/10/05
 * @description:
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController{

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    UserRoleService userRoleService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询用户")
    public User finduser(@PathVariable("id") Long id) {
        User byId = userService.getById(id);
        return byId;
    }


    @GetMapping("/list")
    @ApiOperation(value = "查询所有用户")
    @RateLimit
    public ResultVO findusers() {
        List<User> users = userMapper.selectList(null);
        ResultVO resultVO = ResultVO.success("查询所有用户成功");
        resultVO.data("users", users);
        return resultVO;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改对应用户")
    public ResultVO updateUser(@PathVariable("id") Long id,
                               @RequestBody User user){
        User oldUser = userService.getById(id);
        if(Objects.isNull(oldUser)) {
            throw new ServiceException(USER_NOTFOUND);
        }
        MyBeansUtils.copyPropertiesIgnoreNull(user, oldUser);
        if(userService.save(oldUser)) {
            return ResultVO.success("修改信息成功");
        } else {
            return ResultVO.failure("修改信息失败");
        }
    }
    //TODO

    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/auth/{id}")
    public AjaxResult insertAuthRole(@PathVariable("id") Long userId, Long[] roleIds)
    {
        if(Objects.isNull(roleIds)) {
            throw new ServiceException("角色信息不能为空");
        }
        LambdaQueryWrapper<UserRole> userRolewp = new LambdaQueryWrapper<>();
        userRolewp.eq(UserRole::getRoleId, userId);
        userRoleService.remove(userRolewp);
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleIds).forEach(aLong -> {
            UserRole userRole = new UserRole(userId, aLong);
            userRoles.add(userRole);
        });
        if(userRoleService.saveBatch(userRoles)) {
            return success("权限修改成功！");
        } else {
            return error("权限修改修改！");
        }
    }

}
