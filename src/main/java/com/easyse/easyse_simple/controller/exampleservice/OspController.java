package com.easyse.easyse_simple.controller.exampleservice;

import com.easyse.easyse_simple.pojo.DO.example.ExamplesOsp;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.exampleservice.OspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ：rc
 * @date ：Created in 2022/11/14 14:57
 * @description：开源案例
 */

@RestController
@RequestMapping("/examples/osp/")
public class OspController {

    @Autowired
    OspService ospService;

    /**
     * @Description: get:获取开源列表
     * @Param: [java.lang.Integer]
     * @return: com.easyse.model.vo.ResultVO
     * @Author: lpc
     * @Date:2022/11/13 18:35
     */

    @GetMapping("list/{page}")
    public ResultVO getOsps(@PathVariable Integer page){

        if(page > 0 && page < 999){
            return ospService.getOspsPage(page);
        }
        else{
            return ResultVO.failure("页码有误!");
        }
    }

    /**
     * @Description: add:添加开源案例
     * @Param: [com.easyse.model.DO.examples.ExamplesOsp]
     * @return: com.easyse.model.vo.ResultVO
     * @Author: lpc
     * @Date:2022/11/13 19:03
     */

    @PostMapping("")
    public ResultVO getOsps(@Valid ExamplesOsp osp){
        //TODO 接口测试
        return ospService.putOsp(osp);
    }
    
    /**
     * @Description: 删除一个项目
     * @Param: [java.lang.Long] 
     * @return: com.easyse.model.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/11/13 19:17
     */
    
    @DeleteMapping("{id}")
    public ResultVO deleteOsps(@PathVariable Long id){
        //TODO 接口测试
        return ospService.deleteOsp(id);
    }

    /**
     * @Description: 获取开源案例的类型 
     * @Param: [] 
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO 
     * @Author: lpc
     * @Date:2022/12/18 18:32
     */
    
    /**
     * @Description: 获取种类
     * @Param: []
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/18 19:34
     */

    @GetMapping("kinds")
    public ResultVO getKinds(){
            return ospService.getOspsKinds();
    }
    /**
     * @Description: 按种类获取
     * @Param: [java.lang.String]
     * @return: com.easyse.easyse_simple.pojo.vo.ResultVO
     * @Author: lpc
     * @Date:2022/12/18 19:34
     */
    @GetMapping("kinds/{kind}")
    public ResultVO getKind(@PathVariable String kind){
        return ospService.getOspsByKind(kind);
    }

    @GetMapping("search/{string}")
    public ResultVO search(@PathVariable String string){
        return ospService.search(string);
    }



}