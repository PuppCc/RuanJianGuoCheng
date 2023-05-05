package com.easyse.easyse_simple;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.easyse.easyse_simple.mapper")
@EnableSwagger2
@EnableMPP  // 用于解决复合主键问题
@EnableAsync
public class EasyseSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyseSimpleApplication.class, args);
    }

}
