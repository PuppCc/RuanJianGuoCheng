package com.easyse.easyse_simple.controller.exampleservice;

import com.easyse.easyse_simple.pojo.DO.example.DesignMode;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.DesignModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zky
 * @date: 2022/12/06
 * @description:
 */
@RequestMapping("examples/designmode")
@Api(tags = "设计模式类别管理")
public class DesignModeController {

    @Autowired
    DesignModeService designModeService;

    @GetMapping("/list")
    @ApiOperation(value = "设计模式列表")
    public ResultVO listDesignMode(){
        List<DesignMode> designModes = designModeService.list();
        ResultVO resultVO = ResultVO.success("查询设计模式类别成功");
        resultVO.data("designModes", designModes);
        return resultVO;
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加设计模式，modeName不能为空")
    public ResultVO addDesignMode(@Validated DesignMode designMode) {
        if(designModeService.save(designMode)) {
            return ResultVO.failure("添加设计模式类别失败");
        } else {
            return ResultVO.success("添加设计模式类别成功");
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改设计模式，modeName不能为空")
    public ResultVO updateDesignMode(@Validated DesignMode designMode) {
        if(designModeService.updateById(designMode)) {
            return ResultVO.failure("添加设计模式类别失败");
        } else {
            return ResultVO.success("添加设计模式类别成功");
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除设计模式")
    public ResultVO deleteDesignMode(@PathVariable("id") Long id){
        if(designModeService.removeById(id)) {
            return ResultVO.failure("删除设计模式失败");
        } else {
            return ResultVO.success("删除设计模式成功");
        }
    }





}
