package com.easyse.easyse_simple;

import com.easyse.easyse_simple.mapper.userservice.UserMapper;
import com.easyse.easyse_simple.pojo.DO.example.DesignModeCase;
import com.easyse.easyse_simple.pojo.DO.task.User;
import com.easyse.easyse_simple.pojo.DO.techqa.Comment;
import com.easyse.easyse_simple.pojo.DO.techqa.Techqa;
import com.easyse.easyse_simple.pojo.DO.techqa.Topic;
import com.easyse.easyse_simple.quartz.TechqaRefreshJob;
import com.easyse.easyse_simple.repository.elasticsearch.TechqaRepository;
import com.easyse.easyse_simple.service.ElasticsearchService;
import com.easyse.easyse_simple.service.exampleservice.DesignModeCaseService;
import com.easyse.easyse_simple.service.techqaservice.CommentService;
import com.easyse.easyse_simple.service.techqaservice.TechqaService;
import com.easyse.easyse_simple.service.techqaservice.TopicService;
import com.easyse.easyse_simple.utils.RedisCache;
import com.easyse.easyse_simple.utils.RedisKeyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.easyse.easyse_simple.utils.IntToLong.toLong;

@SpringBootTest
class EasyseSimpleApplicationTests {

    @Autowired
    TechqaService techqaService;

    @Autowired
    TopicService topicService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    TechqaRepository techqaRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DesignModeCaseService designModeCaseService;

    @Test
    void contextLoads() {
        for (int i = 0; i < 1; i++) {
            Techqa techqa = Techqa.builder().content(i + "apple~apple~apple~apple~apple~apple~")
                    .modifiedBy("zky")
                    .createBy("zky")
                    .title("测试")
                    .userId(toLong(1))
                    .build();
            techqaService.save(techqa);
        }
    }

    @Test
    void addTopic() {
        Topic build = Topic.builder().createBy("admin")
               .description("安卓")
               .userId(toLong(0))
               .build();
        topicService.save(build);
    }

    /**
     * 查询测试
     */
    @Test
    void esTest(){
        List<Techqa> techqas = elasticsearchService.searchTechqa("apple", 0, 10);
        techqas.forEach(techqa -> System.out.println(techqa.getContent()));
    }

    /**
     * es
     */
    @Test
    void esAddTest(){
        for (int i = 0; i < 2; i++) {
            Techqa techqa = Techqa.builder().content("apple~apple~apple~apple~apple~apple~")
                    .id(toLong(i))
                    .modifiedBy("zky")
                    .createBy("zky")
                    .title("测试")
                    .gmtCreate(new Date())
                    .gmtModified(new Date())
                    .likeAmount(1)
                    .isDeleted(0)
                    .status(0)
                    .userId(toLong(1))
                    .score(15.00D)
                    .type(0)
                    .commentAmount(10)
                    .build();
            techqaRepository.save(techqa);
        }
    }


    @Test
    public void zhouyuebang(){
        List<Techqa> list = techqaService.list();
        list.forEach(techqa -> {
            String redisKey = RedisKeyUtil.getTechqaScoreKey();
            redisTemplate.opsForSet().add(redisKey, techqa.getId());
        });
        String redisKey = RedisKeyUtil.getTechqaScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            System.out.println("[任务取消] 没有需要刷新的技术问答");
            return ;
        }
        System.out.println("[任务开始] 正在刷新技术问答分数: " + operations.size());
        while (operations.size() > 0) {
            techqaRefreshJob.refresh((Long) operations.pop());
        }
        System.out.println("[任务结束] 技术问答分数刷新完毕");
        techqaRefreshJob.weekRank();
        techqaRefreshJob.monthRank();

    }

    /**
     * 热门标签查询
     */
    @Test
    void searchTest(){
        List<Topic> topics = topicService.getTopics(0L, 0, 10, 1);
        topics.forEach(topic -> System.out.println(topic.getDescription()));
    }

    /**
     * 评论
     */
    @Test
    void commentTest(){
        for (int i = 0; i < 100; i++) {
            Comment build = Comment.builder().content("i + 我为价值观代言")
                    .createBy("admin")
                    .entityId(toLong(1))
                    .targetId(toLong(1))
                    .userId(toLong(1))
                    .entityType(1)
                    .likeCount(i)
                    .build();
            commentService.save(build);
        }
    }

    /**
     * 查询评论
     */
    @Test
    void searchComment() {
        List<Comment> comments = commentService.findComments(0L, 0, 10, 1);
        comments.forEach(comment -> System.out.println(comment.getContent()));
    }

    /**
     * redisCache测试
     */
    @Test
    void cacheTest() {
        String visitTechqa = RedisKeyUtil.getVisitTechqa(toLong(1));
        Object cacheObject = redisCache.getCacheObject(visitTechqa);
        System.out.println(Objects.isNull(cacheObject));

    }
//    @Value("${case.content}")
//    private String content;
//
//    @Value("${case.title}")
//    private String title;
    /**
     * 添加设计模式
     */
//    @Test
//    void addDesignModeCase() {
//        DesignModeCase build = DesignModeCase.builder().content(content)
//                        .title(title)
//                        .userId(toLong(1))
//                        .build();
//        designModeCaseService.addCase(build);
//    }

    /**
     * 设计模式查询
     */
    @Test
    void searchDesignModeCase() {

        DesignModeCase designModeCase = designModeCaseService.getById(toLong(1));
        System.out.println(designModeCase.getContent());
    }


    @Autowired
    TechqaRefreshJob techqaRefreshJob;
    @Test
    void techqaDaliyScoreTest(){
//当前日期字符串
//        redisCache.zAdd(RedisKeyUtil.getDaliyScore(), toLong(1), 55.00);
//        //添加日榜数据  incrementScore方法当对应的key不存在时，会自动创建对应key的集合，已存在则会增加对应value的score
//        redisCache.zAdd(RedisKeyUtil.getDaliyScore(), toLong(2), 50.00);
//        redisCache.zAdd(RedisKeyUtil.getDaliyScore(), toLong(1), 45.00);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ZSetOperations operation = redisTemplate.opsForZSet();

        operation.incrementScore("2022-06-01:dayRank","lyx",30);
        operation.incrementScore("2022-06-02:dayRank","lisi",5);
        operation.incrementScore("2022-06-03:dayRank","zhangsan",10);

        operation.incrementScore("2022-06-04:dayRank","lyx",30);
        operation.incrementScore("2022-06-05:dayRank","lisi",5);
        operation.incrementScore("2022-06-06:dayRank","zhangsan",10);

        operation.incrementScore("2022-06-10:dayRank","lyx",30);
        operation.incrementScore("2022-06-10:dayRank","lisi",20);
        operation.incrementScore("2022-06-10:dayRank","zhangsan",10);

        techqaRefreshJob.weekRank();

        techqaRefreshJob.monthRank();
    }

    @Autowired
    UserMapper userMapper;

    @Test
    void maskTest(){
        User byPhonenumberUser = userMapper.getByPhonenumberUser("17780748762");
        System.out.println(byPhonenumberUser.getPhonenumber());
    }

    @Test
    void techqaTest(){
        List<Techqa> list = techqaService.list();
//        System.out.println(list);
    }
}
