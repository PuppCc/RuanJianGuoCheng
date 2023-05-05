package com.easyse.easyse_simple.service.tasksservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.easyse.easyse_simple.mapper.tasksservice.ClassMapper;
import com.easyse.easyse_simple.pojo.DO.task.Clazz;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.tasksservice.ClazzService;
import com.easyse.easyse_simple.service.tasksservice.FilesService;
import com.easyse.easyse_simple.service.tasksservice.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：rc
 * @date ：Created in 2022/11/9 14:36
 * @description：
 */
@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    GroupService groupService;
    @Autowired
    FilesService filesService;

    @Override
    public ResultVO addClass(Clazz clas) {
        int insert = classMapper.insert(clas);
        if(insert == 1){
            return ResultVO.success("添加班级成功！");
        }
        return ResultVO.failure("添加班级失败!服务器发生未知错误！");
    }


    @Override
    public ResultVO getClassList() {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("Id").select("ClassName").select("Grade");
        List<Clazz> classes = classMapper.selectList(queryWrapper);
        ArrayList<ResultVO> resultVOS = new ArrayList<>();
        HashMap<String,Object> resMap = new HashMap<>();
        if(ObjectUtils.isEmpty(classes)){
            return ResultVO.failure("获取班级列表失败!");
        }else{

            for(Clazz clazz : classes){
                resultVOS.add(groupService.getGroupList(clazz.getId()));
            };

            ResultVO resultVO =ResultVO.success("加载班级列表成功!");
            resMap.put("res",resultVOS);
            resultVO.setData(resMap);
            return resultVO;

        }

    }

    /**
     * @Description: 按年级查所有的班级
     * @Param: [java.lang.String]
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/30 16:44
     */

    @Override
    public ResultVO getClassListByGrade(String grade) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*").eq("Grade",grade);
        List<Clazz> classes = classMapper.selectList(queryWrapper);
        ArrayList<ResultVO> resultVOS = new ArrayList<>();
        if(ObjectUtils.isEmpty(classes)){
            return ResultVO.failure("获取班级列表失败!");
        }else{
            HashMap<String,Object> map = new HashMap<>();
            for(Clazz clazz : classes){
                resultVOS.add(groupService.getGroupList(clazz.getId()));
            };
            ResultVO resultVO =ResultVO.success("加载班级列表成功!");
            map.put("res",resultVOS);
            resultVO.setData(map);
            return resultVO;

        }
    }

    @Override
    public ResultVO deleteClass(Long id) {
        int i = classMapper.deleteById(id);
        if(i==1){
            return ResultVO.success("已成功删除该班级!");
        }
        return ResultVO.failure("删除失败！");
    }
}
