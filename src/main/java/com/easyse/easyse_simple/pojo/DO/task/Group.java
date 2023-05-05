package com.easyse.easyse_simple.pojo.DO.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@TableName(value ="practicalTasks_group")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @TableId(type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Integer gradeId;

    private Integer classId;

    private String groupName;

    private String projectName;

    @NotNull(message = "小组长不能为空")
    private Long leaderId;

    private String leader;

    private Integer number;

    private Integer maxNumber;

    private Integer isDeleted;

    private String Invitationcode;

    @TableField(exist = false)
    /** 父级名称 */
    private String parentName;

    @TableField(exist = false)
    /** 子部门 */
    private List<Group> children = new ArrayList<Group>();

    public List<Group> getChildren()
    {
        return children;
    }

    private Integer progress;

    private String gitUrl;

    private String introduction;

}