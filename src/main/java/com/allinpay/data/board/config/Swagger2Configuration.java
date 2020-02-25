package com.allinpay.data.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger在线文档配置<br>
 * 项目启动后可通过地址：http://host:ip/swagger-ui.html 查看在线文档
 * 
 * @author shawn
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

	private Contact contact = new Contact("shawn","localhost:8180/swagger-ui.html", "wangxuan3@allinpay.com");
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				//.apis(RequestHandlerSelectors.basePackage("com.allinpay.liveapp.retail.controller"))
//				.paths(PathSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*")))
				.build();
	}
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("allinpay dashboard API")
                .description("通联数据面板服务")
                .termsOfServiceUrl("https://gitee.com/wangyidao/board-service")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
