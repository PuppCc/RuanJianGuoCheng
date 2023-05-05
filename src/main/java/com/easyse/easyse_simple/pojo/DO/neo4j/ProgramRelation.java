package com.easyse.easyse_simple.pojo.DO.neo4j;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName Graph_program_relation
 */
@RelationshipEntity(value = "ProgramRelation")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ProgramRelation implements Serializable {

    public ProgramRelation(ProgramNode from, ProgramNode to, String remark){
        relationFrom = from;
        relationTo = to;
        this.remark = remark;
    }

    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 关系开始节点id
     */
    @StartNode
    private ProgramNode relationFrom;

    /**
     * 关系终止节点id
     */
    @EndNode
    private ProgramNode relationTo;

    /**
     * 关系详情
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 创建者id
     */
    private Long createdBy;

    /**
     * 更新时间
     */
    private Date gmtUpdated;

    /**
     * 更新者id
     */
    private Long updatedBy;


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
        ProgramRelation other = (ProgramRelation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRelationFrom() == null ? other.getRelationFrom() == null : this.getRelationFrom().equals(other.getRelationFrom()))
            && (this.getRelationTo() == null ? other.getRelationTo() == null : this.getRelationTo().equals(other.getRelationTo()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getGmtCreated() == null ? other.getGmtCreated() == null : this.getGmtCreated().equals(other.getGmtCreated()))
            && (this.getCreatedBy() == null ? other.getCreatedBy() == null : this.getCreatedBy().equals(other.getCreatedBy()))
            && (this.getGmtUpdated() == null ? other.getGmtUpdated() == null : this.getGmtUpdated().equals(other.getGmtUpdated()))
            && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRelationFrom() == null) ? 0 : getRelationFrom().hashCode());
        result = prime * result + ((getRelationTo() == null) ? 0 : getRelationTo().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getGmtCreated() == null) ? 0 : getGmtCreated().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getGmtUpdated() == null) ? 0 : getGmtUpdated().hashCode());
        result = prime * result + ((getUpdatedBy() == null) ? 0 : getUpdatedBy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", relationFrom=").append(relationFrom);
        sb.append(", relationTo=").append(relationTo);
        sb.append(", remark=").append(remark);
        sb.append(", gmtCreated=").append(gmtCreated);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", gmtUpdated=").append(gmtUpdated);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append("]");
        return sb.toString();
    }
}