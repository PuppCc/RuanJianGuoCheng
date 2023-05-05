package com.easyse.easyse_simple.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: zky2002
 * @Date: 2022/8/15/13:33
 * @Description: 阿里云 OSS 基本配置
 */

@Configuration
// 已修改，从nacos中读取数据
@PropertySource(value = {"classpath:application-aliyun-oss.properties"})
@ConfigurationProperties(prefix = "aliyun")
@Data
@Accessors(chain = true)// 开启链式调用
public class AliyunOssConfig {

    /**
     *  地域节点
     */
    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;

    /**
     * OSS的Bucket名称
     */
    private String bucketName;

    /**
     * Bucket 域名
     */
    private String urlPrefix;

    /**
     * 目标文件夹
     */
    private String fileHost;


    /**
     * 将OSS 客户端交给Spring容器托管
     * @return
     */
    @Bean
    public OSS OSSClient() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }
}
