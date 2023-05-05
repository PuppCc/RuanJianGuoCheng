package com.easyse.easyse_simple.pojo.DO.task;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName(value ="practicalTasks_class")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Clazz {
    private Long id;

    private String school;

    private String major;

    private String grade;

    private String className;

    private Date gmtCreate;

    private String createBy;

    private Date gmtUpdate;

    private String updateBy;

}