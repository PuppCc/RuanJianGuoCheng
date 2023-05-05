package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设计模式种类(ExamplesSceneScene)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:07:32
 */
@TableName(value ="examples_scene_scene")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class SceneScene implements Serializable {
    private static final long serialVersionUID = -75467898430405804L;
    /**
     * 案例ID
     */
    @MppMultiId // 复合主键
    private Long sceneDesignId;

    /**
     * 设计模式种类ID
     */
    @MppMultiId // 复合主键
    private Integer sceneId;


    public Long getSceneDesignId() {
        return sceneDesignId;
    }

    public void setSceneDesignId(Long sceneDesignId) {
        this.sceneDesignId = sceneDesignId;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

}

