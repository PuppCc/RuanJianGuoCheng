package com.easyse.easyse_simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author zky
 * @Description Swagger 配置类
 * @EnableSwagger2 开启swagger2
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment envi) {

        return new Docket(DocumentationType.SWAGGER_2)
                // 配置Swagger
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.easyse"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "ICH-DOC",
                "软件工程实践教学辅助平台API文档",
                "1.0",
                "http://ich.sicnu.edu.cn/",
                new Contact(
                        "zky",
                        "http://cs.sicnu.edu.cn/",
                        "2540560264@qq.com"
                ),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }



}