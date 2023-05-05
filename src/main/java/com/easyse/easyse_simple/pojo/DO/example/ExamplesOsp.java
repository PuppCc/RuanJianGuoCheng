package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 热点开源项目(ExamplesOsp)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:07:32
 */
@TableName(value ="examples_osp")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ExamplesOsp implements Serializable {
    private static final long serialVersionUID = 782331018003502825L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 开源项目标题
     */
    @NotNull(message = "标题不能为空")
    private String title;
    /**
     * 开源项目简介
     */
    @NotNull(message = "简介不能为空")
    private String introduction;
    /**
     * star数
     */
    private Integer stars;
    /**
     * fork数
     */
    private Integer forks;
    /**
     * 编程技术种类
     */

    private String category;
    /**
     * 项目链接
     */
    @NotNull(message = "项目链接不能为空")

    private String repUrl;
    /**
     * 0-正常；1-删除
     */
    private String isDeleted;
    
    private Date gmtCreate;
    
    private String createBy;
    
    private Date gmtModified;
    
    private String modifiedBy;



}

