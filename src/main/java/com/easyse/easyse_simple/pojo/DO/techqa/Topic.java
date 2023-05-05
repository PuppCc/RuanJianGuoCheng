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
 * 话题表
 * @TableName share_topic
 */
@TableName(value ="share_topic")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Topic implements Serializable {
    /**
     * 话题ID
     */
    @TableId(type = IdType.AUTO)
    private Long topicId;

    /**
     * 创建者用户ID
     */
    @NotNull(message = "创建者标签的用户不能为空")
    private Long userId;

    /**
     * 话题描述
     */
    @NotNull(message = "标签内容不能为空")
    private String description;

    /**
     * 状态
     */
    private int isDeleted;

    /**
     * 问答数量
     */
    private Integer techqaAmount;

    /**
     * 关注者数量
     */
    private Integer followerAmount;

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
        Topic other = (Topic) that;
        return (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getTechqaAmount() == null ? other.getTechqaAmount() == null : this.getTechqaAmount().equals(other.getTechqaAmount()))
            && (this.getFollowerAmount() == null ? other.getFollowerAmount() == null : this.getFollowerAmount().equals(other.getFollowerAmount()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()))
            && (this.getModifiedBy() == null ? other.getModifiedBy() == null : this.getModifiedBy().equals(other.getModifiedBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getTechqaAmount() == null) ? 0 : getTechqaAmount().hashCode());
        result = prime * result + ((getFollowerAmount() == null) ? 0 : getFollowerAmount().hashCode());
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
        sb.append(", topicId=").append(topicId);
        sb.append(", useridId=").append(userId);
        sb.append(", description=").append(description);
        sb.append(", techqaAmount=").append(techqaAmount);
        sb.append(", followerAmount=").append(followerAmount);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", createBy=").append(createBy);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}