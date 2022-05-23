package com.dsltyyz.xuper.xuperchain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * <p>
 * swagger 配置
 * </p>
 *
 * @author dsltyyz
 * @since 2020-9-8
 */
@EnableOpenApi
@Configuration
@Profile({"dev"})
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("百度超级链开放API文档")
                .apiInfo(apiInfo("百度超级链开放API文档", "公共", "v1.0.0"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dsltyyz.xuper.xuperchain.openapi"))
                .paths(PathSelectors.regex("/.*"))
                .build();
    }

    private ApiInfo apiInfo(String title, String description, String version) {
        return new ApiInfoBuilder().title(title).description(description).termsOfServiceUrl("https://www.dsltyyz.com").version(version).build();
    }
}
