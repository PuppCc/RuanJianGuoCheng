package com.easyse.easyse_simple.mapper.exampleservice;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.example.Scene;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25405
* @description 针对表【examples_scene(场景设计方案)】的数据库操作Mapper
* @createDate 2022-12-08 15:25:20
* @Entity pojo.DO.systemservice.Scene
*/
@Mapper
public interface SceneMapper extends BaseMapper<Scene> {


}
