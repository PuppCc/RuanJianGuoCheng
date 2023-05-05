package com.easyse.easyse_simple.service.userserivce;



import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.task.User;

import java.util.List;

/**
* @author 25405
* @description 针对表【common_user】的数据库操作Service
* @createDate 2022-10-05 17:44:32
*/
public interface UserService extends IService<User> {

    List<String> selectPermsByUserId(Long id);

    int updatePwd(User user);

}
