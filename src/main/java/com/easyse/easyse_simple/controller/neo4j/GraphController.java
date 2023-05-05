package com.easyse.easyse_simple.controller.neo4j;

import com.easyse.easyse_simple.pojo.DO.neo4j.ProgramNode;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.service.neo4j.GraphService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/grahp")
@Slf4j
@Api(tags = "图数据库相关")
public class GraphController {


    @Autowired
    private GraphService graphService;


    /**
     *  添加节点，并添加关系，正常情况下应该由文件导入，此处仅是测试
     * @param name 初始节点名称
     * @param id
     * @param nameTo    添加有关系节点的名称
     * @param remark    关系说明
     */
    @PostMapping(path = "/create")
    @ApiOperation(value = "添加节点：name、id、nameTo、remark")
    public ResultVO addNode(
            @RequestParam(name = "name",defaultValue = "node1") String name,
            @RequestParam(name = "id",defaultValue = "0")Long id,
            @RequestParam(name = "nameTo",defaultValue = "node2")String nameTo,
            @RequestParam(name = "remark",defaultValue = "编程技术") String remark){
        log.info("添加节点");
        if(!Objects.isNull(graphService.findByName(name))) {
            return ResultVO.failure("该名字的节点已经存在");
        }
        graphService.addNode(name, id, nameTo, remark);
        return ResultVO.success();
    }

    /**
     *  添加节点，正常情况下应该由文件导入，此处仅是测试
     * @param name 初始节点名称
     */
    @PostMapping(path = "/createsimple")
    @ApiOperation(value = "添加节点：name、id")
    public ResultVO addNodeSimple(
            @RequestParam(name = "name",defaultValue = "node1") String name,
            @RequestParam(name = "id",defaultValue = "0")Long id
            /*@RequestParam(name = "nameTo",defaultValue = "node2")String nameTo,
            @RequestParam(name = "remark",defaultValue = "编程技术") String remark*/){
        log.info("添加节点");
        if(!Objects.isNull(graphService.findByName(name))) {
            return ResultVO.failure("该名字的节点已经存在");
        }
        graphService.addNodeSimple(name, id);
        return ResultVO.success();
    }

    /**
     *  添加节点，并添加关系，正常情况下应该由文件导入，此处仅是测试
     * @param name 初始节点名称
     * @param nameTo    添加有关系节点的名称
     * @param remark    关系说明
     */
    @PostMapping(path = "/createRelation")
    @ApiOperation(value = "添加节点关系：name、nameTo、remark")
    public ResultVO addNodeRelation(
            @RequestParam(name = "name",defaultValue = "node1") String name,
            @RequestParam(name = "nameTo",defaultValue = "node2")String nameTo,
            @RequestParam(name = "remark",defaultValue = "编程技术") String remark){
        log.info("添加节点关系");
        if(Objects.isNull(graphService.findByName(name))) {
            return ResultVO.failure("from节点不存在");
        }
        if(Objects.isNull(graphService.findByName(nameTo))) {
            return ResultVO.failure("to节点不存在");
        }
        graphService.addNodeRelation(name, nameTo, remark);
        return ResultVO.success();
    }


    /**
     * 删除节点
     * @param id 节点ID，非必须，如果不提供，那么默认全删，否则删除相对应的id
     */
    @DeleteMapping(path = "/delete")
    @ApiOperation(value = "删除节点：id、name")
    public ResultVO deleteNode(
            @RequestParam(name = "id",required = false)Long id,
            @RequestParam(name = "name",required = false) String name
            ){
        if (id !=null){
            graphService.deleteNodeById(id);
        }else if(name !=null && id==null){
            graphService.deleteNodeByName(name);
        }else {
            graphService.delete();
        }
        return ResultVO.success();
    }

    /**
     * 根据节点ID，更新节点信息
     * @param id
     * @param name
     * @param age
     */
    @PutMapping(path = "/update")
    @ApiOperation(value = "更新节点：id、name、age")
    public ResultVO updateNode(
            @RequestParam(name = "id",required = false) Long id,
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "age",required = false) Integer age
    ){
        log.info("更新数据：" +  id + " " + name + " " + age);
        graphService.updateNode(id, name,age);
        return ResultVO.success();
    }

    /**
     * 根据名字查找相关的所有节点
     * @param name
     */
    @GetMapping(path = "/find")
    @ApiOperation(value = "根据节点名字查询节点")
    public ResultVO findNode(
            @RequestParam(name = "name",required = true) String name
    ){
        log.info("查找所有的节点" + name);
        List<ProgramNode> programNodes=graphService.queryNodes(name);
        log.info(programNodes.size()+" 返回的数据长度");
        for (ProgramNode programNode: programNodes) {
            log.info("节点名 "+programNode.getName());
        }
        return ResultVO.success().data("nodes",programNodes);
    }

    /**
     * 根据名字查找所有节点
     */
    @GetMapping(path = "/findall")
    @ApiOperation(value = "查询所有节点以及关系")
    public ResultVO findAllNode(){
        log.info("查找所有的节点");
        List<ProgramNode> programNodes=graphService.queryAllNodes();
        log.info(programNodes.size()+" 返回的数据长度");
        for (ProgramNode programNode: programNodes) {
            log.info("节点名 "+programNode.getName());
        }
        return ResultVO.success().data("nodes",programNodes);
    }
}