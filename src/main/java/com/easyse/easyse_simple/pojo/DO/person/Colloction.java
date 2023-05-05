package com.easyse.easyse_simple.pojo.DO.person;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName person_colloct
 */
@TableName(value ="person_colloct")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Colloction implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 0-技术问答；1-设计模式案例；2-场景设计案例；3-评论；4-评论；5-他人笔记
     */
    @ApiModelProperty(value = "类型：0-技术问答；1-设计模式案例；2-场景设计案例；")
    private String collectType;

    /**
     * 收藏实体的ID
     */
    @ApiModelProperty(value = "实体类型Id")
    private Long targetId;

    /**
     * 0-正常；1-删除
     */
    private String isDeleted;

    /**
     * 
     */
    private Date gmtCreate;

    /**
     * 
     */
    private String createBy;

    /**
     * 
     */
    private Date gmtModified;

    /**
     * 
     */
    private String modifiedBy;

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 0-技术问答；1-设计模式案例；2-场景设计案例；3-评论；4-评论；5-他人笔记
     */
    public String getCollectType() {
        return collectType;
    }

    /**
     * 0-技术问答；1-设计模式案例；2-场景设计案例；3-评论；4-评论；5-他人笔记
     */
    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    /**
     * 收藏实体的ID
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * 收藏实体的ID
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * 0-正常；1-删除
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 0-正常；1-删除
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

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
        Colloction other = (Colloction) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCollectType() == null ? other.getCollectType() == null : this.getCollectType().equals(other.getCollectType()))
            && (this.getTargetId() == null ? other.getTargetId() == null : this.getTargetId().equals(other.getTargetId()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getModifiedBy() == null ? other.getModifiedBy() == null : this.getModifiedBy().equals(other.getModifiedBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCollectType() == null) ? 0 : getCollectType().hashCode());
        result = prime * result + ((getTargetId() == null) ? 0 : getTargetId().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getModifiedBy() == null) ? 0 : getModifiedBy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", collectType=").append(collectType);
        sb.append(", targetId=").append(targetId);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}