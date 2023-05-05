package com.easyse.easyse_simple.service.exampleservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.easyse.easyse_simple.mapper.exampleservice.OspMapper;
import com.easyse.easyse_simple.pojo.DO.example.ExamplesOsp;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.OspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：rc
 * @date ：Created in 2022/11/15 8:22
 * @description：
 */

@Service
public class OspServiceImpl  implements OspService {

    @Autowired
    OspMapper ospMapper;

    @Override
    public ResultVO getOspsKinds() {
        QueryWrapper<ExamplesOsp>  queryWrapper = new QueryWrapper<>();
        queryWrapper.select("category").groupBy("category");
        List<ExamplesOsp>Osps = ospMapper.selectList(queryWrapper);

        List<HashMap<String,Object>> lists = new ArrayList<>();
        for (int i = 0; i < Osps.size(); i++) {
            HashMap<String,Object> map  = new HashMap<>();
            map.put("label",Osps.get(i).getCategory());
            map.put("key",Osps.get(i).getCategory());
            lists.add(map);
        }

        ResultVO resultVO = ResultVO.success("获取开源案例类型成功！");
        HashMap<String, Object> map = new HashMap<>();
        map.put("lists",lists);
        resultVO.setData(map);
        return resultVO;
    }

    @Override
    public ResultVO getOspsByKind(String kind) {
        QueryWrapper<ExamplesOsp>  queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.select("*").eq("category", kind);
        List<ExamplesOsp> osps = ospMapper.selectList(queryWrapper);
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("osps",osps);
        ResultVO resultVO = ResultVO.success("获取开源案例成功！");
        resultVO.setData(resMap);
        return resultVO;
    }

    @Override
    public ResultVO search(String string) {
        QueryWrapper<ExamplesOsp>  queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.select("*").like("title",string);
        List<ExamplesOsp> osps = ospMapper.selectList(queryWrapper);
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("osps",osps);
        ResultVO resultVO = ResultVO.success("搜索成功！");
        resultVO.setData(resMap);
        return resultVO;
    }

    @Override
    public ResultVO getOspsPage(Integer page) {
        //封装分页
        Page<ExamplesOsp> rowPage = new Page(page, 10);
        QueryWrapper<ExamplesOsp>  queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*").eq("is_deleted","0");
        Page<ExamplesOsp> resultPage = ospMapper.selectPage(rowPage, queryWrapper);
        List<ExamplesOsp> osps = resultPage.getRecords();
        HashMap<String,Object> resMap = new HashMap<>();
        resMap.put("osps",osps);
        ResultVO resultVO = ResultVO.success("获取开源案例成功！");
        resultVO.setData(resMap);
        return resultVO;
    }

    @Override
    public ResultVO putOsp(ExamplesOsp osp) {

        int insert = ospMapper.insert(osp);
        if(insert == 1){
            return ResultVO.success("添加开源案例成功！");
        }else {
            return ResultVO.failure("服务器内部错误，添加开源案例失败!");
        }
    }

    @Override
    public ResultVO deleteOsp(Long id) {
        QueryWrapper<ExamplesOsp> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,title").eq("id",id);
        ExamplesOsp examplesOsp = ospMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(examplesOsp)){
            //没查到
            return ResultVO.failure("删除失败!未查询到该条记录！");
        }else{
            //执行删除
            examplesOsp.setIsDeleted("1");
            int i = ospMapper.updateById(examplesOsp);
            if(i==1){
                return ResultVO.success("删除成功！");
            }
        }
        return ResultVO.failure("服务器内部错误!操作失败!");
    }

}
