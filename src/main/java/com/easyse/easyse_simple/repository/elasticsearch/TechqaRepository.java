package com.easyse.easyse_simple.repository.elasticsearch;


import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: zky
 * @date: 2022/10/24
 * @description:
 */
@Repository
public interface TechqaRepository extends ElasticsearchRepository<Techqa, Long> {

}
