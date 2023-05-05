package com.easyse.easyse_simple.pojo.DO.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 操作日志记录
 * @TableName system_oper_log
 */
@TableName(value ="system_oper_log")
@Data
@Builder
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class OperLog implements Serializable {
    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    private Long operId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作用户
     */
    private String operName;

    /**
     * 小组项目名称
     */
    private String groupName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private Date operTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OperLog other = (OperLog) that;
        return (this.getOperId() == null ? other.getOperId() == null : this.getOperId().equals(other.getOperId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getBusinessType() == null ? other.getBusinessType() == null : this.getBusinessType().equals(other.getBusinessType()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getOperName() == null ? other.getOperName() == null : this.getOperName().equals(other.getOperName()))
            && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getOperUrl() == null ? other.getOperUrl() == null : this.getOperUrl().equals(other.getOperUrl()))
            && (this.getOperIp() == null ? other.getOperIp() == null : this.getOperIp().equals(other.getOperIp()))
            && (this.getOperLocation() == null ? other.getOperLocation() == null : this.getOperLocation().equals(other.getOperLocation()))
            && (this.getOperParam() == null ? other.getOperParam() == null : this.getOperParam().equals(other.getOperParam()))
            && (this.getJsonResult() == null ? other.getJsonResult() == null : this.getJsonResult().equals(other.getJsonResult()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getErrorMsg() == null ? other.getErrorMsg() == null : this.getErrorMsg().equals(other.getErrorMsg()))
            && (this.getOperTime() == null ? other.getOperTime() == null : this.getOperTime().equals(other.getOperTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOperId() == null) ? 0 : getOperId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getBusinessType() == null) ? 0 : getBusinessType().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getOperName() == null) ? 0 : getOperName().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getOperUrl() == null) ? 0 : getOperUrl().hashCode());
        result = prime * result + ((getOperIp() == null) ? 0 : getOperIp().hashCode());
        result = prime * result + ((getOperLocation() == null) ? 0 : getOperLocation().hashCode());
        result = prime * result + ((getOperParam() == null) ? 0 : getOperParam().hashCode());
        result = prime * result + ((getJsonResult() == null) ? 0 : getJsonResult().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getErrorMsg() == null) ? 0 : getErrorMsg().hashCode());
        result = prime * result + ((getOperTime() == null) ? 0 : getOperTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", operId=").append(operId);
        sb.append(", title=").append(title);
        sb.append(", businessType=").append(businessType);
        sb.append(", method=").append(method);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", operName=").append(operName);
        sb.append(", groupName=").append(groupName);
        sb.append(", operUrl=").append(operUrl);
        sb.append(", operIp=").append(operIp);
        sb.append(", operLocation=").append(operLocation);
        sb.append(", operParam=").append(operParam);
        sb.append(", jsonResult=").append(jsonResult);
        sb.append(", status=").append(status);
        sb.append(", errorMsg=").append(errorMsg);
        sb.append(", operTime=").append(operTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}