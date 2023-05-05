package com.easyse.easyse_simple.pojo.DO.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.io.Serializable;

/**
 * (PracticaltasksTask)实体类
 *
 * @author makejava
 * @since 2022-12-20 19:03:24
 */

@TableName(value ="practicalTasks_task")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable {
    private static final long serialVersionUID = -87353990498903458L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 小组id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;
    /**
     * 忘了
     */
    private String execution;
    /**
     * 忘了
     */
    private String story;
    /**
     * 任务名称
     */
    @NotNull(message = "任务名不能为空")
    private String taskName;
    /**
     * 消耗的时间
     */
    private Float consumed;
    /**
     * 剩余的时间
     */
    private Float lefted;
    /**
     * 截止日期
     */
    private Date deadline;
    /**
     * enum('wait','doing','done','pause','cancel','closed')
     */
    private String sta;
    /**
     * 颜色
     */
    private String color;
    /**
     * 超链接
     */
    private String mailto;
    /**
     * 指派给谁
     */
    private String assignedto;
    /**
     * 时间
     */
    private Date assigneddate;
    
    private String finishedby;
    
    private String finishedlist;
    
    private Date finisheddate;
    
    private String canceledby;
    
    private Date canceleddate;
    
    private String closedby;
    
    private Date closeddate;
    
    private String isDeleted;

    private String checked;

    private String question;

    private String answer;



}

