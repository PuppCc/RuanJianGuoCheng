package com.easyse.easyse_simple.pojo.DO.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easyse.easyse_simple.pojo.DO.DTO.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 菜单权限表
 * @TableName system_menu
 */
@TableName(value ="system_menu")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Serializable{
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    private Integer isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 
     */
    private Date gmtCreate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 
     */
    private Date gmtModified;

    /**
     * 备注
     */
    private String remark;

    /** 子菜单 */
//    private List<Menu> children = new ArrayList<Menu>();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Menu other = (Menu) that;
        return (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getOrderNum() == null ? other.getOrderNum() == null : this.getOrderNum().equals(other.getOrderNum()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getComponent() == null ? other.getComponent() == null : this.getComponent().equals(other.getComponent()))
            && (this.getQuery() == null ? other.getQuery() == null : this.getQuery().equals(other.getQuery()))
            && (this.getIsFrame() == null ? other.getIsFrame() == null : this.getIsFrame().equals(other.getIsFrame()))
            && (this.getIsCache() == null ? other.getIsCache() == null : this.getIsCache().equals(other.getIsCache()))
            && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
            && (this.getVisible() == null ? other.getVisible() == null : this.getVisible().equals(other.getVisible()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPerms() == null ? other.getPerms() == null : this.getPerms().equals(other.getPerms()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getComponent() == null) ? 0 : getComponent().hashCode());
        result = prime * result + ((getQuery() == null) ? 0 : getQuery().hashCode());
        result = prime * result + ((getIsFrame() == null) ? 0 : getIsFrame().hashCode());
        result = prime * result + ((getIsCache() == null) ? 0 : getIsCache().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPerms() == null) ? 0 : getPerms().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", menuId=").append(menuId);
        sb.append(", menuName=").append(menuName);
        sb.append(", parentId=").append(parentId);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", path=").append(path);
        sb.append(", component=").append(component);
        sb.append(", query=").append(query);
        sb.append(", isFrame=").append(isFrame);
        sb.append(", isCache=").append(isCache);
        sb.append(", menuType=").append(menuType);
        sb.append(", visible=").append(visible);
        sb.append(", status=").append(status);
        sb.append(", perms=").append(perms);
        sb.append(", icon=").append(icon);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}