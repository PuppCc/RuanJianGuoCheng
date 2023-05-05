package com.easyse.easyse_simple.service.neo4j;


import com.easyse.easyse_simple.pojo.DO.neo4j.ProgramNode;

import java.util.List;

public interface GraphService {

    void deleteNodeById(Long id);

    void deleteNodeByName(String name);

    void delete();

    void addNode(String name,Long id,String nameTo,String remark);

    void addNodeSimple(String name,Long id);

    void updateNode(Long id,String name,Integer age);

    void addNodeRelation(String name,String nameTo,String remark);


    Iterable<ProgramNode>  queryNodes();

    ProgramNode findByName(String name);

    List<ProgramNode> queryNodes(String name);

    List<ProgramNode> queryAllNodes();

}