package com.easyse.easyse_simple.controller.exampleservice;

import com.easyse.easyse_simple.pojo.DO.example.DesignModeCase;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.DesignModeCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author: zky
 * @date: 2022/12/07
 * @description:
 */
@RestController
@RequestMapping("examples/designcase")
@Api(tags = "设计模式实践案例")
public class DesignCaseController {

    @Autowired
    DesignModeCaseService designModeCaseService;

    @GetMapping("/list")
    @ApiOperation(value = "设计模式列表")
    public ResultVO listDesignMode() {
        List<DesignModeCase> designModeCases = designModeCaseService.list();
        ResultVO resultVO = ResultVO.success("查询设计模式案例成功");
        resultVO.data("designModes", designModeCases);
        return resultVO;
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加设计模式案例，modeName不能为空")
    public ResultVO addDesignMode(@Validated DesignModeCase designModeCase) {
        if (designModeCaseService.addCase(designModeCase) == 0) {
            return ResultVO.failure("添加设计模式案例失败");
        } else {
            return ResultVO.success("添加设计模式案例成功");
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改设计模式案例，modeName不能为空")
    public ResultVO updateDesignMode(@Validated DesignModeCase designModeCase) {
        if (designModeCaseService.updateById(designModeCase)) {
            return ResultVO.failure("设计模式案例修改失败");
        } else {
            return ResultVO.success("设计模式案例修改成功");
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除设计模式案例")
    public ResultVO deleteDesignMode(@PathVariable("id") Long id) {
        if (designModeCaseService.removeById(id)) {
            return ResultVO.failure("删除设计模式案例失败");
        } else {
            return ResultVO.success("删除设计模式案例成功");
        }
    }

    @GetMapping("/details/{id}")
    @ApiOperation(value = "设计模式详情页")
    public ResultVO getCaseById(@PathVariable("id")Long id){
        DesignModeCase designModeCase = designModeCaseService.getById(id);
        if(Objects.isNull(designModeCase)) {
            return ResultVO.failure("当前设计模式已被删除~");
        }
        designModeCase.setContent(HtmlUtils.htmlUnescape(designModeCase.getContent()));
        designModeCase.setTitle(HtmlUtils.htmlUnescape(designModeCase.getTitle()));
        ResultVO resultVO = ResultVO.success("查询案例成功");
        resultVO.data("designModeCase", designModeCase);
        return resultVO;
    }
}