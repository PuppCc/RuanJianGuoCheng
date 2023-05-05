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

import java.util.Date;
import java.io.Serializable;

/**
 * (PracticaltaskTtask)实体类
 *
 * @author makejava
 * @since 2022-12-20 19:01:45
 */

@TableName(value ="practicalTasks_Ttask")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Ttask implements Serializable {
    private static final long serialVersionUID = -72731654034412989L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String Info;
    /**
     * 说明
     */
    private String instruction;
    /**
     * 结束日期
     */
    private Date finisheddate;
    /**
     * 年级
     */
    private String grade;
    /**
     * 班级ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long classId;
    /**
     * 小组ID
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;
    /**
     * 是否上传
     */
    private String upload;
    /**
     * 文件链接
     */
    private String url;

    private String auther;

    private String checked;

    private String question;

    private String answer;
}