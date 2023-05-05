package com.easyse.easyse_simple.pojo.DO.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色信息表
 * @TableName system_role
 */
@TableName(value ="system_role")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;

    /**
     * 小组树选择项是否关联显示
     */
    private Integer groupCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date gmtUpdate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;


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
        Role other = (Role) that;
        return (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getRoleName() == null ? other.getRoleName() == null : this.getRoleName().equals(other.getRoleName()))
            && (this.getRoleKey() == null ? other.getRoleKey() == null : this.getRoleKey().equals(other.getRoleKey()))
            && (this.getRoleSort() == null ? other.getRoleSort() == null : this.getRoleSort().equals(other.getRoleSort()))
            && (this.getDataScope() == null ? other.getDataScope() == null : this.getDataScope().equals(other.getDataScope()))
            && (this.getMenuCheckStrictly() == null ? other.getMenuCheckStrictly() == null : this.getMenuCheckStrictly().equals(other.getMenuCheckStrictly()))
            && (this.getGroupCheckStrictly() == null ? other.getGroupCheckStrictly() == null : this.getGroupCheckStrictly().equals(other.getGroupCheckStrictly()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtUpdate() == null ? other.getGmtUpdate() == null : this.getGmtUpdate().equals(other.getGmtUpdate()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getRoleName() == null) ? 0 : getRoleName().hashCode());
        result = prime * result + ((getRoleKey() == null) ? 0 : getRoleKey().hashCode());
        result = prime * result + ((getRoleSort() == null) ? 0 : getRoleSort().hashCode());
        result = prime * result + ((getDataScope() == null) ? 0 : getDataScope().hashCode());
        result = prime * result + ((getMenuCheckStrictly() == null) ? 0 : getMenuCheckStrictly().hashCode());
        result = prime * result + ((getGroupCheckStrictly() == null) ? 0 : getGroupCheckStrictly().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getGmtUpdate() == null) ? 0 : getGmtUpdate().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", roleKey=").append(roleKey);
        sb.append(", roleSort=").append(roleSort);
        sb.append(", dataScope=").append(dataScope);
        sb.append(", menuCheckStrictly=").append(menuCheckStrictly);
        sb.append(", groupCheckStrictly=").append(groupCheckStrictly);
        sb.append(", status=").append(status);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtUpdate=").append(gmtUpdate);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public boolean isMenuCheckStrictly()
    {
        return menuCheckStrictly == 0;
    }


}