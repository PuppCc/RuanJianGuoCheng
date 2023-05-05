package com.easyse.easyse_simple.pojo.DO.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.easyse.easyse_simple.annotations.EncryptTransaction;
import com.easyse.easyse_simple.annotations.Mobile;
import com.easyse.easyse_simple.annotations.SensitiveData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author zky
 * @TableName person_user
 */
@TableName(value ="person_user")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
//@SensitiveData
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 座右铭、个人简介
     */
    private String headline;

    /**
     * 班级ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long classId;

    /**
     * 小组ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;

    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
//    @EncryptTransaction
    private String email;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    @Mobile(message = "手机格式出错")
//    @EncryptTransaction
    private String phonenumber;

    /**
     * 0-未激活; 1-已激活;
     */
    private Integer isActived;

    /**
     * 激活码
     */
    private String activationCode;

    /**
     * 头像URL
     */
    private String headerUrl;

    /**
     * 登录的IP
     */
    private String loginIp;

    /**
     * 上一次登录时间
     */
    private Date loginUpdate;

    /**
     * 关注数
     */
    private Integer followeeCount;

    /**
     * 粉丝数
     */
    private Integer followerCount;

    /**
     * 0-正常；1-删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 
     */
    private String createBy;

    /**
     * 最近一次修改的时间
     */
    private Date gmtModified;

    /**
     * 
     */
    private String updateBy;

    /**
     * 注释信息
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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getHeadline() == null ? other.getHeadline() == null : this.getHeadline().equals(other.getHeadline()))
            && (this.getClassId() == null ? other.getClassId() == null : this.getClassId().equals(other.getClassId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPhonenumber() == null ? other.getPhonenumber() == null : this.getPhonenumber().equals(other.getPhonenumber()))
            && (this.getIsActived() == null ? other.getIsActived() == null : this.getIsActived().equals(other.getIsActived()))
            && (this.getActivationCode() == null ? other.getActivationCode() == null : this.getActivationCode().equals(other.getActivationCode()))
            && (this.getHeaderUrl() == null ? other.getHeaderUrl() == null : this.getHeaderUrl().equals(other.getHeaderUrl()))
            && (this.getLoginIp() == null ? other.getLoginIp() == null : this.getLoginIp().equals(other.getLoginIp()))
            && (this.getLoginUpdate() == null ? other.getLoginUpdate() == null : this.getLoginUpdate().equals(other.getLoginUpdate()))
            && (this.getFolloweeCount() == null ? other.getFolloweeCount() == null : this.getFolloweeCount().equals(other.getFolloweeCount()))
            && (this.getFollowerCount() == null ? other.getFollowerCount() == null : this.getFollowerCount().equals(other.getFollowerCount()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getHeadline() == null) ? 0 : getHeadline().hashCode());
        result = prime * result + ((getClassId() == null) ? 0 : getClassId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPhonenumber() == null) ? 0 : getPhonenumber().hashCode());
        result = prime * result + ((getIsActived() == null) ? 0 : getIsActived().hashCode());
        result = prime * result + ((getActivationCode() == null) ? 0 : getActivationCode().hashCode());
        result = prime * result + ((getHeaderUrl() == null) ? 0 : getHeaderUrl().hashCode());
        result = prime * result + ((getLoginIp() == null) ? 0 : getLoginIp().hashCode());
        result = prime * result + ((getLoginUpdate() == null) ? 0 : getLoginUpdate().hashCode());
        result = prime * result + ((getFolloweeCount() == null) ? 0 : getFolloweeCount().hashCode());
        result = prime * result + ((getFollowerCount() == null) ? 0 : getFollowerCount().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
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
        sb.append(", id=").append(id);
        sb.append(", headline=").append(headline);
        sb.append(", classId=").append(classId);
        sb.append(", groupId=").append(groupId);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", sex=").append(sex);
        sb.append(", email=").append(email);
        sb.append(", phonenumber=").append(phonenumber);
        sb.append(", isActived=").append(isActived);
        sb.append(", activationCode=").append(activationCode);
        sb.append(", headerUrl=").append(headerUrl);
        sb.append(", loginIp=").append(loginIp);
        sb.append(", loginUpdate=").append(loginUpdate);
        sb.append(", followeeCount=").append(followeeCount);
        sb.append(", followerCount=").append(followerCount);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}