package com.easyse.easyse_simple.exceptions;

/**
 * 业务异常
 * 
 * @author ruoyi
 */
public final class UserNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public UserNotFoundException()
    {
    }

    public UserNotFoundException(String message)
    {
        this.message = message;
    }

    public UserNotFoundException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public UserNotFoundException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public UserNotFoundException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}