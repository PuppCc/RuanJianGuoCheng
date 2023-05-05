package com.easyse.easyse_simple.mapper.techqaservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 25405
* @description 针对表【share_domain(领域表)】的数据库操作Mapper
* @createDate 2022-10-23 20:02:13
* @Entity com.easyse.Domain
*/
@Mapper
@Component
public interface DomainMapper extends BaseMapper<Domain> {

}




