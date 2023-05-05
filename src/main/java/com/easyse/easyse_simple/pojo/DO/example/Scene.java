package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.io.Serializable;

/**
 * 场景设计方案(ExamplesScene)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:07:32
 */

@TableName(value ="examples_scene")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Scene implements Serializable {
    private static final long serialVersionUID = 393391409120487527L;

    @TableId(type = IdType.AUTO)
    private String id;
    /**
     * 发布者ID
     */
    private Long userId;
    /**
     * 场景
     */
    private String sceneName;
    /**
     * 案例数量
     */
    private Integer sceneAmount;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Integer getSceneAmount() {
        return sceneAmount;
    }

    public void setSceneAmount(Integer sceneAmount) {
        this.sceneAmount = sceneAmount;
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

