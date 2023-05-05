package com.easyse.easyse_simple.service.systemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.constants.UserConstants;
import com.easyse.easyse_simple.mapper.systemservice.MenuMapper;
import com.easyse.easyse_simple.mapper.systemservice.RoleMapper;
import com.easyse.easyse_simple.mapper.systemservice.RoleMenuMapper;
import com.easyse.easyse_simple.pojo.DO.DTO.SysUser;
import com.easyse.easyse_simple.pojo.DO.system.Menu;
import com.easyse.easyse_simple.pojo.DO.system.Role;
import com.easyse.easyse_simple.pojo.vo.MetaVo;
import com.easyse.easyse_simple.pojo.vo.RouterVo;
import com.easyse.easyse_simple.pojo.vo.TreeSelect;
import com.easyse.easyse_simple.service.systemservice.MenuService;
import com.easyse.easyse_simple.utils.Constants;
import com.easyse.easyse_simple.utils.SecurityUtils;
import com.easyse.easyse_simple.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 25405
* @description 针对表【system_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2022-10-19 10:58:22
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public List<String> selectPermsByUserId(Long id){
        return menuMapper.selectPermsByUserId(id);
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
//    public List<Menu> selectMenuList(Long userId)
//    {
//        return selectMenuList(new Menu(), userId);
//    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
//    @Override
//    public List<Menu> selectMenuList(Menu menu, Long userId)
//    {
//        List<Menu> menuList = null;
//        // 管理员显示所有菜单信息
//        if (SysUser.isAdmin(userId))
//        {
//            menuList = menuMapper.selectMenuList(menu);
//        }
//        else
//        {
//            menu.getParams().put("userId", userId);
//            menuList = menuMapper.selectMenuListByUserId(menu);
//        }
//        return menuList;
//    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

//    /**
//     * 根据用户ID查询菜单
//     *
//     * @param userId 用户名称
//     * @return 菜单列表
//     */
//    @Override
//    public List<Menu> selectMenuTreeByUserId(Long userId)
//    {
//        List<Menu> menus = null;
//        if (SecurityUtils.isAdmin(userId))
//        {
//            menus = menuMapper.selectMenuTreeAll();
//        }
//        else
//        {
//            menus = menuMapper.selectMenuTreeByUserId(userId);
//        }
//        return getChildPerms(menus, 0);
//    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId)
    {
        Role role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
//    @Override
//    public List<RouterVo> buildMenus(List<Menu> menus)
//    {
//        List<RouterVo> routers = new LinkedList<RouterVo>();
//        for (Menu menu : menus)
//        {
//            RouterVo router = new RouterVo();
//            router.setHidden("1".equals(menu.getVisible()));
//            router.setName(getRouteName(menu));
//            router.setPath(getRouterPath(menu));
//            router.setComponent(getComponent(menu));
//            router.setQuery(menu.getQuery());
//            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//            List<Menu> cMenus = menu.getChildren();
//            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
//            {
//                router.setAlwaysShow(true);
//                router.setRedirect("noRedirect");
//                router.setChildren(buildMenus(cMenus));
//            }
//            else if (isMenuFrame(menu))
//            {
//                router.setMeta(null);
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                children.setPath(menu.getPath());
//                children.setComponent(menu.getComponent());
//                children.setName(StringUtils.capitalize(menu.getPath()));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//                children.setQuery(menu.getQuery());
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            }
//            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu))
//            {
//                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
//                router.setPath("/");
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                String routerPath = innerLinkReplaceEach(menu.getPath());
//                children.setPath(routerPath);
//                children.setComponent(UserConstants.INNER_LINK);
//                children.setName(StringUtils.capitalize(routerPath));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            }
//            routers.add(router);
//        }
//        return routers;
//    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
//    @Override
//    public List<Menu> buildMenuTree(List<Menu> menus)
//    {
//        List<Menu> returnList = new ArrayList<Menu>();
//        List<Long> tempList = new ArrayList<Long>();
//        for (Menu dept : menus)
//        {
//            tempList.add(dept.getMenuId());
//        }
//        for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();)
//        {
//            Menu menu = (Menu) iterator.next();
//            // 如果是顶级节点, 遍历该父节点的所有子节点
//            if (!tempList.contains(menu.getParentId()))
//            {
//                recursionFn(menus, menu);
//                returnList.add(menu);
//            }
//        }
//        if (returnList.isEmpty())
//        {
//            returnList = menus;
//        }
//        return returnList;
//    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
//    @Override
//    public List<TreeSelect> buildMenuTreeSelect(List<Menu> menus)
//    {
//        List<Menu> menuTrees = buildMenuTree(menus);
//        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
//    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public Menu selectMenuById(Long menuId)
    {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId)
    {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId)
    {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(Menu menu)
    {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(Menu menu)
    {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId)
    {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(Menu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        Menu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(Menu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu)
    {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu))
        {
            component = menu.getComponent();
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu))
        {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu))
        {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(Menu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(Menu menu)
    {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(Menu menu)
    {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
//    public List<Menu> getChildPerms(List<Menu> list, int parentId)
//    {
//        List<Menu> returnList = new ArrayList<Menu>();
//        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();)
//        {
//            Menu t = (Menu) iterator.next();
//            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
//            if (t.getParentId() == parentId)
//            {
//                recursionFn(list, t);
//                returnList.add(t);
//            }
//        }
//        return returnList;
//    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
//    private void recursionFn(List<Menu> list, Menu t)
//    {
//        // 得到子节点列表
//        List<Menu> childList = getChildList(list, t);
//        t.setChildren(childList);
//        for (Menu tChild : childList)
//        {
//            if (hasChild(list, tChild))
//            {
//                recursionFn(list, tChild);
//            }
//        }
//    }

    /**
     * 得到子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t)
    {
        List<Menu> tlist = new ArrayList<Menu>();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext())
        {
            Menu n = (Menu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Menu> list, Menu t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path)
    {
        return StringUtils.replaceEach(path, new String[] { Constants.HTTP, Constants.HTTPS },
                new String[] { "", "" });
    }

}




