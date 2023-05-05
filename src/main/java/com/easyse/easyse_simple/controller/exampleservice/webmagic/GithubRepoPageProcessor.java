package com.easyse.easyse_simple.controller.exampleservice.webmagic;

import com.easyse.easyse_simple.mapper.exampleservice.OspMapper;
import com.easyse.easyse_simple.pojo.DO.example.ExamplesOsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author ：rc
 * @date ：Created in 2022/11/14 16:02
 * @description：
 *             这是开源案例的爬虫测试
 */

@Component
public class GithubRepoPageProcessor implements PageProcessor {

    private static GithubRepoPageProcessor that;
    @Autowired
    public OspMapper ospMapper;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

        //案例列表
        ArrayList<ExamplesOsp> osps = new ArrayList<>();


        // 部分二：定义如何抽取页面信息，并保存下来
//        page.addTargetRequests(page.getHtml().links().regex("(https://www\\.baidu\\.com/\\w+/\\w+)").all());
//        page.putField("author", page.getUrl().regex("https://www\\.baidu\\.com/(\\w+)/.*").toString());
//        page.putField("news", page.getHtml().xpath("//*[@id=\"content\"]/div[1]").toString());
//        System.out.println(page);
        //拉取10个项目的信息
        for (Integer i = 1; i <= 10; i++) {
            //创建一个案例
            ExamplesOsp osp = new ExamplesOsp();

            page.putField("first_name",page.getHtml().xpath("//*[@id=\"projectList\"]/div[1]/div["+i+"]/div/h3/a/span[1]/allText()"));
            page.putField("last_name",page.getHtml().xpath("//*[@id=\"projectList\"]/div[1]/div["+i+"]/div/h3/a/span[2]/allText()").get());
            page.putField("content",page.getHtml().xpath("//*[@id=\"projectList\"]/div[1]/div["+i+"]/div/div[1]/p/allText()"));
            page.putField("url",page.getHtml().xpath("//*[@id=\"projectList\"]/div[1]/div["+i+"]/div/h3/a/@href"));

            //设置属性
            osp.setRepUrl(page.getResultItems().get("url").toString());
            osp.setTitle(page.getResultItems().get("first_name").toString()+page.getResultItems().get("last_name").toString());
            osp.setIntroduction(page.getResultItems().get("content").toString());
            osp.setStars(new Random().nextInt(999));
            osp.setForks(new Random().nextInt(666));
            System.out.println(osp);
            that.ospMapper.insert(osp);

        }

/*
        if (page.getResultItems().get("demo1")==null){
            page.setSkip(true);
        }
*/

        // 部分三：从页面发现后续的url地址来抓取

    }

    @Override
    public Site getSite() {
        return site;
    }
    @PostConstruct
    public void init() {
        that=this;
        that.ospMapper = this.ospMapper;

    }

    public static void main(String[] args) {
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.oschina.net/project").thread(5).run();
    }
}