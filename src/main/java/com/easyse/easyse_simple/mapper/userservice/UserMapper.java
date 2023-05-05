package com.easyse.easyse_simple.mapper.userservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.annotations.EncryptTransaction;
import com.easyse.easyse_simple.pojo.DO.DTO.SysUser;
import com.easyse.easyse_simple.pojo.DO.task.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author 25405
* @description 针对表【common_user】的数据库操作Mapper
* @createDate 2022-10-05 17:44:32
* @Entity com.zky.serviceuser.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    void updateStatus(Long userId, int isActived);

    User getByPhonenumberUser(@EncryptTransaction @Param("phonenumber") String phonenumber);

}




