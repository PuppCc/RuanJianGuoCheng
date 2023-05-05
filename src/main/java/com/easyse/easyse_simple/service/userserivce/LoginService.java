package com.easyse.easyse_simple.service.userserivce;


import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
public interface LoginService {
    ResultVO doLogin(User user);

    ResultVO doRegister(User user, String authcode);

    int activation(Long userId, String code);

    String authcode_get(String u_phone);

    void logOut(User user);

    void recordLogininfor(String username, String status, String message);
}
