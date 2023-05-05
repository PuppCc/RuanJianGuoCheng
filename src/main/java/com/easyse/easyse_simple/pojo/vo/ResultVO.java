                                                                           package com.easyse.easyse_simple.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class ResultVO implements Serializable {


    /**
     * 默认成功响应码
     */
    private static final Integer DEFAULT_SUCCESS_CODE = HttpStatus.OK.value();
    /**
     * 默认成功响应信息
     */
    private static final String DEFAULT_SUCCESS_MSG = "操作成功！";
    /**
     * 默认失败响应码
     */
    private static final Integer DEFAULT_FAILURE_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    /**
     * 默认失败响应信息
     */
    private static final String DEFAULT_FAILURE_MSG = "操作失败！";
    @Getter
    private Meta meta;

    @Getter
    private HashMap<String,Object> data;

    public static ResultVO success(){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG);
        return ResultVO;
    }

    public static ResultVO success(String msg){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(DEFAULT_SUCCESS_CODE, msg);
        return ResultVO;
    }

    public static ResultVO success(HttpStatus status, String msg){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(status.value(), msg);
        return ResultVO;
    }

    public static ResultVO failure(){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(DEFAULT_FAILURE_CODE, DEFAULT_FAILURE_MSG);
        return ResultVO;
    }

    public static ResultVO failure(String msg){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(DEFAULT_FAILURE_CODE, msg);
        return ResultVO;
    }

    public static ResultVO failure(HttpStatus httpStatus, String msg){
        ResultVO ResultVO = new ResultVO();
        ResultVO.meta = new Meta(httpStatus.value(), msg);
        return ResultVO;
    }

    public ResultVO build(){
        this.data=new HashMap<>();
        return this;
    }

    public ResultVO build(HashMap<String,Object> map){
        this.data=map;
        return this;
    }

    public ResultVO data(String name,Object o){
        if (this.data==null) {
            data=new HashMap<>();
        }
        this.data.put(name,o);
        return this;
    }


    @Data
    @AllArgsConstructor
    private static class Meta {

        /**
         * 处理结果代码，与 HTTP 状态响应码对应
         */
        private Integer code;

        /**
         * 处理结果信息
         */
        private String msg;
    }

}