package com.easyse.easyse_simple.service;


import com.easyse.easyse_simple.pojo.DO.Page;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.vo.ResultVO;
import com.easyse.easyse_simple.repository.elasticsearch.TechqaRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索相关
 */
@Service
public class ElasticsearchService {

    @Autowired
    private TechqaRepository techqaRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    /**
     * 将数据插入 Elasticsearch 服务器
     * @param techqa
     */
    public void saveTechqa(Techqa techqa) {
        techqaRepository.save(techqa);
    }

    /**
     * 将数据从 Elasticsearch 服务器中删除
     * @param id
     */
    public void deleteTechqa(Long id) {
        techqaRepository.deleteById(id);
    }

    /**
     * 分页搜索
     * @param keyword 搜索的关键词
     * @param current 当前页码（这里的 Page 是 Spring 提供的，而非我们自己实现的那个）
     * @param limit 每页显示多少条数据
     * @return
     */
    public List<Techqa> searchTechqa(String keyword, int current, int limit) {

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("gmtCreate").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        SearchHits<Techqa> searchHits = elasticsearchRestTemplate.search(searchQuery, Techqa.class);
        if (searchHits.getTotalHits() <= 0) {
            return new ArrayList<Techqa>();
        }

        // 处理命中的数据
        List<Techqa> list = new ArrayList<>();
        for (SearchHit<Techqa> hit : searchHits) {
            Techqa techqa = hit.getContent();

            // 处理高亮显示的内容
            HighlightField title = (HighlightField) hit.getHighlightFields().get("title");
            if (title != null) {
                techqa.setTitle(title.getFragments()[0].toString());
            }

            HighlightField content = (HighlightField) hit.getHighlightFields().get("content");
            if (content != null) {
                techqa.setContent(content.getFragments()[0].toString());
            }

            list.add(techqa);
        }

        return list;
    }

}
