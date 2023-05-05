package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;

/**
 * 设计模式种类(ExamplesDesignMode)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:07:13
 */
@TableName(value ="Examples_Design_Mode")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class DesignMode implements Serializable {
    private static final long serialVersionUID = -95520266713668367L;
    
    private String id;
    /**
     * 设计模式名字
     */
    @NotNull(message = "设计模式名字不能为空")
    private String modeName;
    /**
     * 案例数量
     */
    private Integer caseAmount;
    /**
     * 0-正常；1-删除
     */
    private String isDeleted;
    
    private Date gmtCreate;
    
    private String createBy;
    
    private Date gmtModified;
    
    private String modifiedBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public Integer getCaseAmount() {
        return caseAmount;
    }

    public void setCaseAmount(Integer caseAmount) {
        this.caseAmount = caseAmount;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}

