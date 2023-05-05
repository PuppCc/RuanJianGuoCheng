package com.easyse.easyse_simple.controller.systemservice;

import com.easyse.easyse_simple.annotations.Log;
import com.easyse.easyse_simple.constants.UserConstants;
import com.easyse.easyse_simple.enums.BusinessType;
import com.easyse.easyse_simple.pojo.DO.system.Menu;
import com.easyse.easyse_simple.pojo.vo.AjaxResult;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.systemservice.MenuService;
import com.easyse.easyse_simple.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.easyse.easyse_simple.utils.SecurityUtils.getUserId;
import static com.easyse.easyse_simple.utils.SecurityUtils.getUsername;


/**
 * @author: zky
 * @date: 2022/12/07
 * @description: 菜单相关
 */
@RestController
@RequestMapping("/system/menu")
@Api(tags = "菜单相关")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    @ApiOperation(value = "获取菜单列表")
    public ResultVO list()
    {
        List<Menu> menus = menuService.list();
        return ResultVO.success("查询成功").data("menus", menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    @ApiOperation(value = "根据菜单编号获取详细信息")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
//    @GetMapping("/treeselect")
//    @ApiOperation(value = "获取菜单下拉列表")
//    public AjaxResult treeselect(Menu menu)
//    {
//        List<Menu> menus = menuService.selectMenuList(menu, getUserId());
//        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
//    }

    /**
     * 加载对应角色菜单列表树
     */
//    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
//    @ApiOperation(value = "加载对应角色菜单列表树")
//    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
//    {
//        List<Menu> menus = menuService.selectMenuList(getUserId());
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
//        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
//        return ajax;
//    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增菜单")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Menu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(getUsername());
        int i = menuService.insertMenu(menu);
        return i < 0 ? AjaxResult.error("插入失败") : AjaxResult.success("新增菜单成功");
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改菜单")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Menu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(getUsername());
        int i = menuService.updateMenu(menu);
        return i < 0 ? AjaxResult.error("修改失败") : AjaxResult.success("修改菜单成功");
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        int i = menuService.deleteMenuById(menuId);
        return i < 0 ? AjaxResult.error("删除失败") : AjaxResult.success("删除成功");
    }

}
