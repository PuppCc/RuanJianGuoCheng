package com.easyse.easyse_simple.pojo.DO.neo4j;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName Graph_program_node
 */
@NodeEntity(label = "ProgramNode")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ProgramNode implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 技术名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 特点
     */
    private String feature;

    /**
     * 原理
     */
    private String principle;

    /**
     * 应用场景
     */
    private String applied;

    /**
     * 学习指南
     */
    private String guide;

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
        ProgramNode other = (ProgramNode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
            && (this.getFeature() == null ? other.getFeature() == null : this.getFeature().equals(other.getFeature()))
            && (this.getPrinciple() == null ? other.getPrinciple() == null : this.getPrinciple().equals(other.getPrinciple()))
            && (this.getApplied() == null ? other.getApplied() == null : this.getApplied().equals(other.getApplied()))
            && (this.getGuide() == null ? other.getGuide() == null : this.getGuide().equals(other.getGuide()))
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
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getFeature() == null) ? 0 : getFeature().hashCode());
        result = prime * result + ((getPrinciple() == null) ? 0 : getPrinciple().hashCode());
        result = prime * result + ((getApplied() == null) ? 0 : getApplied().hashCode());
        result = prime * result + ((getGuide() == null) ? 0 : getGuide().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", introduction=").append(introduction);
        sb.append(", feature=").append(feature);
        sb.append(", principle=").append(principle);
        sb.append(", applied=").append(applied);
        sb.append(", guide=").append(guide);
        sb.append(", gmtCreated=").append(gmtCreated);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", gmtUpdated=").append(gmtUpdated);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}