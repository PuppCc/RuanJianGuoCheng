package com.easyse.easyse_simple.repository.neo4j;

import com.easyse.easyse_simple.pojo.DO.neo4j.ProgramNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface ProgramNodeRepository extends Neo4jRepository<ProgramNode,Long> {
    ProgramNode findByName(String name);
    ProgramNode deleteByName(String name);
    /**
     * 根据节点名称查找关系
     * @param name
     * @return
     */
    @Query("MATCH c=(cf:ProgramNode)-[r:ProgramRelation]->(ct:ProgramNode) WHERE ct.name=$name OR cf.name=$name RETURN c")
    List<ProgramNode> findRelationByProgramNode(String name);

    @Query("MATCH c=(cf:ProgramNode)-[r:ProgramRelation]->(ct:ProgramNode) RETURN c")
    List<ProgramNode> findAllByProgramNode();

}