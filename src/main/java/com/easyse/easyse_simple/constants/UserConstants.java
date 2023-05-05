package com.easyse.easyse_simple.constants;

/**
 * @author: zky
 * @date: 2022/10/05
 * @description: 用户相关的常量
 */
public interface UserConstants {

    /**
     * 男性
     */
    String USER_MALE = "0";

    /**
     * 女性
     */
    String USER_FEMALE = "1";

    /**
     * 系统用户id
     */
    long SYSTEM_USER_ID = 1;

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 匿名用户
     */
    String ANONYMOUSUSER = "anonymousUser";

    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /**
     * 用户未找到
     */
    public static final int USER_NOTFOUND = -1;

    /** 正常状态 */
    public static final String NORMAL = "0";

    /** 异常状态 */
    public static final String EXCEPTION = "1";

    /** 用户封禁状态 */
    public static final String USER_DISABLE = "1";

    /** 角色封禁状态 */
    public static final String ROLE_DISABLE = "1";

    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";

    /** 部门停用状态 */
    public static final String DEPT_DISABLE = "1";

    /** 字典正常状态 */
    public static final String DICT_NORMAL = "0";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /** 是否菜单外链（是） */
    public static final String YES_FRAME = "0";

    /** 是否菜单外链（否） */
    public static final String NO_FRAME = "1";

    /** 菜单类型（目录） */
    public static final String TYPE_DIR = "M";

    /** 菜单类型（菜单） */
    public static final String TYPE_MENU = "C";

    /** 菜单类型（按钮） */
    public static final String TYPE_BUTTON = "F";

    /** Layout组件标识 */
    public final static String LAYOUT = "Layout";

    /** ParentView组件标识 */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink组件标识 */
    public final static String INNER_LINK = "InnerLink";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
}
