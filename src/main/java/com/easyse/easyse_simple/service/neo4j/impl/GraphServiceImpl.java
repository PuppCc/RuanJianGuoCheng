package com.easyse.easyse_simple.service.neo4j.impl;

import com.easyse.easyse_simple.pojo.DO.neo4j.ProgramNode;
import com.easyse.easyse_simple.pojo.DO.neo4j.ProgramRelation;
import com.easyse.easyse_simple.repository.neo4j.ProgramNodeRepository;
import com.easyse.easyse_simple.repository.neo4j.ProgramRelationRepository;
import com.easyse.easyse_simple.service.neo4j.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class GraphServiceImpl implements GraphService {
    @Autowired
    private ProgramRelationRepository programRelationRepository;
    @Autowired
    private ProgramNodeRepository programRepository;

    @Override
    public void deleteNodeById(Long id) {
        programRepository.deleteById(id);
    }

    @Override
    public void deleteNodeByName(String name) {
        programRepository.deleteByName(name);
    }

    @Override
    public void delete() {
        programRepository.deleteAll();
    }

    @Override
    public void addNode(String name, Long id, String nameTo, String remark) {
        ProgramNode programNode=new ProgramNode();
        programNode.setName(name);
        programNode.setUpdatedBy(id);
        programRepository.save(programNode);
        log.info("ProgramNode：" + programNode);
        ProgramNode programNodeTo = programRepository.findByName(nameTo);
        if(Objects.isNull(programNodeTo)){
            programNodeTo = new ProgramNode();
            programNodeTo.setName(nameTo);
            programRepository.save(programNodeTo);
        }
        log.info("ProgramNodeTo：" + programNodeTo);
        programNode = programRepository.findByName(name);
        programNodeTo = programRepository.findByName(nameTo);
        ProgramRelation programRelation = new ProgramRelation(programNode, programNodeTo, remark);
        programRelationRepository.save(programRelation);
        //此处应该返回提示信息
    }

    @Override
    public void addNodeSimple(String name, Long id) {
        ProgramNode programNode=new ProgramNode();
        programNode.setName(name);
        programNode.setUpdatedBy(id);
        programRepository.save(programNode);
    }

    @Override
    public void addNodeRelation(String name, String nameTo, String remark) {
        ProgramNode programNode = programRepository.findByName(name);
        ProgramNode programNodeTo = programRepository.findByName(nameTo);
        ProgramRelation programRelation = new ProgramRelation(programNode, programNodeTo, remark);
        programRelationRepository.save(programRelation);
    }

    /**
     * 根据ID修改节点的值
     * @param id
     * @param name
     * @param age
     */
    @Override
    public void updateNode(Long id, String name, Integer age) {
        log.info("id为：" + id);
        Optional<ProgramNode> ProgramNode = programRepository.findById(id);
        if (ProgramNode.isPresent()) {
            ProgramNode ProgramNode1;
            ProgramNode1 = ProgramNode.get();
            ProgramNode1.setName(name);
            ProgramNode1.setUpdatedBy(id);
            programRepository.save(ProgramNode1);
        } else {
            log.info("目标节点不存在");
        }
    }

    @Override
    public Iterable<ProgramNode> queryNodes() {
        return programRepository.findAll();
    }

    @Override
    public ProgramNode findByName(String name) {
        return programRepository.findByName(name);
    }

    @Override
    public List<ProgramNode> queryNodes(String name) {
        return programRepository.findRelationByProgramNode(name);
    }

    @Override
    public List<ProgramNode> queryAllNodes() {
        return programRepository.findAllByProgramNode();
    }
}