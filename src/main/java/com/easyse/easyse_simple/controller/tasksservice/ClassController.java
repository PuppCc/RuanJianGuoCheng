package com.easyse.easyse_simple.controller.tasksservice;

import com.easyse.easyse_simple.pojo.DO.task.Clazz;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.tasksservice.ClazzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ：rc
 * @date ：Created in 2022/10/27 9:44
 * @description：用户班级-年级
 */

@Api(tags = "班级相关")
@RestController
@RequestMapping(value = "/tasks/class")
public class ClassController  {
    @Autowired
    ClazzService clazzService;

    /**
     * @Description:  按年级查看班级列表
     * @Param: [java.lang.String]
     * @return: com.easyse.model.vo.ResultVO
     * @Author: lpc
     * @Date:2022/11/6 19:31
     */
    @ApiOperation(value = "按年级查看班级列表")
    @GetMapping("/list/{grade}")
    public ResultVO getClazzListByGarde(@PathVariable String grade){
        return clazzService.getClassListByGrade(grade);
    }

    /**
     * @Description:    //查看班级列表
     * @Param: []
     * @return: com.easyse.model.vo.ResultVO
     * @Author: lpc
     * @Date:2022/11/6 19:08
     */
    @ApiOperation(value = "查看班级列表")
    @GetMapping("/list")
    public ResultVO getClazzList(){
        return clazzService.getClassList();
    }




    //TODO 查看班级成员状况


   /**
    * @Description:    新增班级
    * @Param: [com.easyse.model.DO.Class]
    * @return: com.easyse.model.vo.ResultVO
    * @Author: lpc
    * @Date:2022/11/6 19:21
    */

   @ApiOperation(value = "新增班级")
    @PostMapping("")
    public ResultVO addClass(@Valid @RequestBody Clazz clas){
        return clazzService.addClass(clas);
    }

    /**
     * @Description:   删除班级
     * @Param: [java.lang.Long]
     * @return: com.easyse.model.vo.ResultVO
     * @Author: lpc
     * @Date:2022/11/9 19:40
     */
    @ApiOperation(value = "删除班级")
    @DeleteMapping("/{id}")
    public ResultVO deeleteClass(@PathVariable Long id){
        return clazzService.deleteClass(id);
    }
}
