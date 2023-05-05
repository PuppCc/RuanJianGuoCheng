package com.easyse.easyse_simple.service.exampleservice.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.exampleservice.SceneSceneMapper;
import com.easyse.easyse_simple.pojo.DO.example.SceneScene;
import com.easyse.easyse_simple.service.exampleservice.SceneSceneService;
import org.springframework.stereotype.Service;

/**
* @author 25405
* @description 针对表【examples_scene_scene(设计模式种类)】的数据库操作Service实现
* @createDate 2022-12-08 15:25:20
*/
@Service
public class SceneSceneServiceImpl extends ServiceImpl<SceneSceneMapper, SceneScene>
implements SceneSceneService {

}
