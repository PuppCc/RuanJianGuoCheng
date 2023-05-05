package com.easyse.easyse_simple.config;

import com.easyse.easyse_simple.quartz.CommentRefreshJob;
import com.easyse.easyse_simple.quartz.TechqaRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author: zky
 * @date: 2022/11/29
 * @description: quartz配置类
 */
@Configuration
public class QuartzConfig {

    /**
     * 刷新帖子分数任务
     * @return
     */
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("CommentRefreshJob", CommentRefreshJob.class);
//        jobDataMap.put("TechqaRefreshJob", TechqaRefreshJob.class);
        factoryBean.setJobClass(TechqaRefreshJob.class);
        factoryBean.setName("TechqaScoreRefreshJob");
        factoryBean.setGroup("easyseJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    /**
     * 刷新帖子分数触发器
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("TechqaScoreRefreshTrigger");
        factoryBean.setGroup("easyseTriggerGroup");
        // 5分钟刷新一次
        factoryBean.setRepeatInterval(1000 * 60 * 5);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }

}
