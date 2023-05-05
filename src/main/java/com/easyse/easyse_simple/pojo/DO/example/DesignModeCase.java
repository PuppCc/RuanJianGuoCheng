package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;

/**
 * 设计模式案例(ExamplesDesignModeCase)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:07:32
 */
@TableName(value ="examples_design_mode_case")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class DesignModeCase implements Serializable {

    private static final long serialVersionUID = 395085791953999215L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 发布者ID
     */
    private Long userId;
    /**
     * 案例标题
     */
    @NotNull(message = "标题不能为空")
    private String title;
    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    private String content;
    /**
     * 0-正常；1-删除
     */
    private String isDeleted;

    /**
     * 头像url
     */
    private String headUrl;

    private Date gmtCreate;
    
    private String createBy;
    
    private Date gmtModified;
    
    private String modifiedBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

