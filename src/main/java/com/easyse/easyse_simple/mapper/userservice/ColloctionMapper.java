package com.easyse.easyse_simple.mapper.userservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.person.Colloction;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25405
* @description 针对表【person_colloct】的数据库操作Mapper
* @createDate 2022-12-24 16:11:38
* @Entity com.easyse.easyse_simple.pojo.DO.person.Colloction
*/
@Mapper
public interface ColloctionMapper extends BaseMapper<Colloction> {


}
