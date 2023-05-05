package com.easyse.easyse_simple.pojo.DO.person;

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
 * 
 * @TableName person_message
 */
@TableName(value ="person_message")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送者ID
     */
    private Long fromId;

    /**
     * 接受者ID
     */
    private Long toId;

    /**
     * 消息通知类型、私信唯一标识
     */
    private String conversationId;

    /**
     * 内容
     */
    private String content;

    /**
     * 0-未读;1-已读;
     */
    private Integer status;

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
        Message other = (Message) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFromId() == null ? other.getFromId() == null : this.getFromId().equals(other.getFromId()))
            && (this.getToId() == null ? other.getToId() == null : this.getToId().equals(other.getToId()))
            && (this.getConversationId() == null ? other.getConversationId() == null : this.getConversationId().equals(other.getConversationId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
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
        result = prime * result + ((getFromId() == null) ? 0 : getFromId().hashCode());
        result = prime * result + ((getToId() == null) ? 0 : getToId().hashCode());
        result = prime * result + ((getConversationId() == null) ? 0 : getConversationId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", conversationId=").append(conversationId);
        sb.append(", content=").append(content);
        sb.append(", status=").append(status);
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