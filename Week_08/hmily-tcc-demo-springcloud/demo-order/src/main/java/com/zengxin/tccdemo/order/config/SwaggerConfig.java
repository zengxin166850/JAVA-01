package com.zengxin.tccdemo.order.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * SwaggerConfig
     *
     * @return Docket
     * @author zengxin
     * @date 2020年11月25日 16:46:13
     * @version 1.0.0
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    /**
     * SwaggerConfig
     *
     * @return ApiInfo
     * @author zengxin
     * @date 2020年11月25日 16:46:13
     * @version 1.0.0
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Hmily分布式事务TCC测试").description("应用RestFul Api接口").version("1.0").build();
    }

}
