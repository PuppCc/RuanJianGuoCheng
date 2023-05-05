package com.easyse.easyse_simple.pojo.DO.techqa;

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

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName share_comment
 */
@TableName(value ="share_comment")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 进行评论的用户ID
     */
    private Long userId;

    /**
     * 被评论类型（1-技术问答；2-评论）
     */
    @NotNull(message = "评论类型不能为空")
    private Integer entityType;

    /**
     * 被评论实体的ID
     */
    @NotNull(message = "entityId不能为空")
    private Long entityId;

    /**
     * 发布该被评论实体的用户ID
     */
    private Long targetId;

    /**
     * 内容
     */
    @NotNull(message = "评论不能为空")
    private String content;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 0-正常;1-删除;
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
        Comment other = (Comment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getEntityType() == null ? other.getEntityType() == null : this.getEntityType().equals(other.getEntityType()))
            && (this.getEntityId() == null ? other.getEntityId() == null : this.getEntityId().equals(other.getEntityId()))
            && (this.getTargetId() == null ? other.getTargetId() == null : this.getTargetId().equals(other.getTargetId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getEntityType() == null) ? 0 : getEntityType().hashCode());
        result = prime * result + ((getEntityId() == null) ? 0 : getEntityId().hashCode());
        result = prime * result + ((getTargetId() == null) ? 0 : getTargetId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
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
        sb.append(", entityType=").append(entityType);
        sb.append(", entityId=").append(entityId);
        sb.append(", targetId=").append(targetId);
        sb.append(", content=").append(content);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}