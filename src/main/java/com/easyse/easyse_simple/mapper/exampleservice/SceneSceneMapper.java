package com.easyse.easyse_simple.mapper.exampleservice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyse.easyse_simple.pojo.DO.example.SceneScene;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25405
* @description 针对表【examples_scene_scene(设计模式种类)】的数据库操作Mapper
* @createDate 2022-12-08 15:25:20
* @Entity pojo.DO.systemservice.SceneScene
*/
@Mapper
public interface SceneSceneMapper extends BaseMapper<SceneScene> {

}
