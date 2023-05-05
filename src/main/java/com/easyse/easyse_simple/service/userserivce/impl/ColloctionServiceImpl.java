package com.easyse.easyse_simple.service.userserivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.pojo.DO.person.Colloction;
import com.easyse.easyse_simple.service.userserivce.ColloctionService;
import com.easyse.easyse_simple.mapper.userservice.ColloctionMapper;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【person_colloct】的数据库操作Service实现
* @createDate 2022-12-24 16:11:38
*/
@Service
public class ColloctionServiceImpl extends ServiceImpl<ColloctionMapper, Colloction>
implements ColloctionService{

}
