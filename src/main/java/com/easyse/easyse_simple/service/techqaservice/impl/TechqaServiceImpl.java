package com.easyse.easyse_simple.service.techqaservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyse.easyse_simple.mapper.techqaservice.TechqaMapper;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.utils.SensitiveFilter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.easyse.easyse_simple.utils.Convert.toLong;


/**
* @author 25405
* @description 针对表【share_techqa(技术问答表
)】的数据库操作Service实现
* @createDate 2022-10-23 20:02:13
*/
@Service
@Slf4j
public class TechqaServiceImpl extends ServiceImpl<TechqaMapper, Techqa>
    implements TechqaService {

    @Autowired
    TechqaMapper techqaMapper;

    @Value("${caffeine.techqas.max-size}")
    private int maxSize;

    @Value("${caffeine.techqas.expire-seconds}")
    private int expireSeconds;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 热帖列表的本地缓存
     * key - offset(每页的起始索引):limit(每页显示多少条数据)
     */
    private LoadingCache<String, List<Techqa>> TechqaListCache;

    /**
     * 帖子总数的本地缓存
     * key - userId(其实就是0,表示查询的是所有用户. 对特定用户的查询不启用缓存）
     */
    private LoadingCache<Long, Integer> techqaRowsCache;

    /**
     * 更新类型
     * @param id
     * @param type
     */
    @Override
    public void updateType(Long id, int type) {
        techqaMapper.updateType(id, type);
    }

    /**
     * 更新状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, int status) {
        techqaMapper.updateStatus(id, status);
    }

    /**
     * 初始化本地缓存 Caffeine
     */
    @PostConstruct
    public void init() {
        // 初始化热帖列表缓存
        TechqaListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<Techqa>>() {
                    // 如果缓存Caffeine中没有数据，告诉缓存如何去数据库中查数据，再装到缓存中
                    @Nullable
                    @Override
                    public List<Techqa> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误");
                        }

                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        // 此处可以再访问二级缓存 Redis

                        log.debug("load post list from DB");
                        return techqaMapper.selectTechqas(toLong(0), offset, limit, 1);
                    }
                });

        // 初始化帖子总数缓存
        techqaRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Long key) throws Exception {
                        log.debug("load post rows from DB");
                        return techqaMapper.selectTechqaRows(key);
                    }
                });
    }


    /**
     * 查询讨论贴的个数
     * @param userId 当传入的 userId = 0 时计算所有用户的帖子总数
     *               当传入的 userId ！= 0 时计算该指定用户的帖子总数
     * @return
     */
    public int findTechqaRows (Long userId) {
        // 查询本地缓存(当查询的是所有用户的帖子总数时)
        if (userId == 0) {
            return techqaRowsCache.get(userId);
        }
        // 查询数据库
        log.debug("load post rows from DB");
        return techqaMapper.selectTechqaRows(userId);
    }

    /**
     * 分页查询讨论帖信息
     *
     * @param userId 当传入的 userId = 0 时查找所有用户的帖子
     *               当传入的 userId != 0 时，查找该指定用户的帖子
     * @param offset 每页的起始索引
     * @param limit  每页显示多少条数据
     * @param orderMode  排行模式(若传入 1, 则按照热度来排序)
     * @return
     */
    public List<Techqa> findTechqas (Long userId, int offset, int limit, int orderMode) {
        // 查询本地缓存(当查询的是所有用户的帖子并且按照热度排序时)
        if (userId == 0 && orderMode == 1) {
            return TechqaListCache.get(offset + ":" + limit);
        }
        // 查询数据库
        log.debug("load post list from DB");
        return techqaMapper.selectTechqas(userId, offset, limit, orderMode);
    }

    /**
     * 添加帖子
     * @param techqa
     * @return
     */
    public int addTechqa(Techqa techqa) {
        if (techqa == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 转义 HTML 标记，防止在 HTML 标签中注入攻击语句
        techqa.setTitle(HtmlUtils.htmlEscape(techqa.getTitle()));
        techqa.setContent(HtmlUtils.htmlEscape(techqa.getContent()));

        // 过滤敏感词
        techqa.setTitle(sensitiveFilter.filter(techqa.getTitle()));
        techqa.setContent(sensitiveFilter.filter(techqa.getContent()));

        return techqaMapper.insert(techqa);
    }

    /**
     * 修改帖子的评论数量
     * @param id 帖子 id
     * @param commentCount
     * @return
     */
    @Override
    public int updateCommentCount(Long id, int commentCount) {
        return techqaMapper.updateCommentCount(id, commentCount);
    }

    @Override
    public int updateScore(Long id, double score) {
        return techqaMapper.updateScore(id, score);
    }
}




