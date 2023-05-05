package com.easyse.easyse_simple.pojo.DO.example;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (ExamplesCaseMode)实体类
 *
 * @author makejava
 * @since 2022-11-14 15:06:31
 */
@TableName(value ="Examples_Case_Mode")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ExamplesCaseMode implements Serializable {
    private static final long serialVersionUID = -16782782537671270L;
    /**
     * 案例ID
     */
    private Long designModeCaseId;
    /**
     * 设计模式种类ID
     */
    private Integer designModeId;


    public Long getDesignModeCaseId() {
        return designModeCaseId;
    }

    public void setDesignModeCaseId(Long designModeCaseId) {
        this.designModeCaseId = designModeCaseId;
    }

    public Integer getDesignModeId() {
        return designModeId;
    }

    public void setDesignModeId(Integer designModeId) {
        this.designModeId = designModeId;
    }

}

