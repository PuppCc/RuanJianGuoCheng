package com.easyse.easyse_simple.pojo.DO.techqa;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

/**
 * 
 * @TableName share_techqa_topic
 */
@TableName(value ="share_techqa_topic")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class TechqaTopic implements Serializable {
    /**
     * 技术问答ID
     */
    @MppMultiId // 复合主键
    private Long techqaId;

    /**
     * 领域ID
     */
    @MppMultiId // 复合主键
    private Long topicId;

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
        TechqaTopic other = (TechqaTopic) that;
        return (this.getTechqaId() == null ? other.getTechqaId() == null : this.getTechqaId().equals(other.getTechqaId()))
            && (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTechqaId() == null) ? 0 : getTechqaId().hashCode());
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", techqaId=").append(techqaId);
        sb.append(", topicId=").append(topicId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}