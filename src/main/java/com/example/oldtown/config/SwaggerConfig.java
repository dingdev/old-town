package com.example.oldtown.config;

import com.example.oldtown.common.config.BaseSwaggerConfig;
import com.example.oldtown.common.domain.SwaggerProperties;
// import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.simpleframework.xml.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


import java.util.HashSet;
import java.util.Set;

/**
 * Swagger API文档相关配置(总)
 * Created by dyp on 2020/10/26
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends BaseSwaggerConfig {
    // @Value("${swagger.host}")
    // private String SWAGGER_HOST;
    // @Value("${swagger.protocol}")
    // private String SWAGGER_PROTOCOL;
    private final String MODULES_PACKAGE = "com.example.oldtown.modules";
    private final Boolean ENABLE_SECURITY = true;

    private ApiInfo apiInfo(String moduleName) {
        return new ApiInfoBuilder()
                .title("南浔古镇项目后端接口文档_"+moduleName)
                .description("使用说明: 左上角的下拉框可以在不同模块的接口文档之间切换; 接口baseUrl为上面的Base URL; 鉴权时先调用各自的登录接口(比如系统用户的登录或者小程序用户的登录),复制获取的message里的字符串,假定为XXX,在左上角Authorize中填入'Bearer XXX',点击空白处后,"
                        + "即可根据登录账户的权限,调用其他需要相关权限的接口 ; 点击Models可查看模型,点击调试可以测试接口 ; 带文件参数的接口可以用postman测试 ; 增加或更新本人xxx时不必传入userId ; 时间类参数输入示例为2020/09/06 18:04:39\n" +
                        "图片音频等文件的存储不带文件的域名端口前缀(比如在增加通用设施时,直接在pictureUrl处放入增加通用设施文件后返回的/old-town/com/place/xxx即可,取到通用设施的图片地址后半段后,拼上前端存储下来的通用信息相关里的文件前缀,即可访问图片)")
                .contact(new Contact("dyp", null, null))
                .version("20201201[1.3]")
                .build();
    }

    @Bean
    public Docket createRestApi1() {
        String moduleName = "1_系统和通用模块";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // .protocols(swaggerProperties.getProtocol())
                // .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(moduleName))
                .select()
                .apis(basePackage(MODULES_PACKAGE+".sys;"+MODULES_PACKAGE+".com"))
                .paths(PathSelectors.any())
                .build()
                .groupName(moduleName);
        if (ENABLE_SECURITY) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        return docket;
    }

    @Bean
    public Docket createRestApi2() {
        String moduleName = "2_交通接驳模块";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // .protocols(swaggerProperties.getProtocol())
                // .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(moduleName))
                .select()
                .apis(basePackage(MODULES_PACKAGE+".trf"))
                .paths(PathSelectors.any())
                .build()
                .groupName(moduleName);
        if (ENABLE_SECURITY) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        return docket;
    }

    @Bean
    public Docket createRestApi3() {
        String moduleName = "3_小程序模块";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // .protocols(swaggerProperties.getProtocol())
                // .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(moduleName))
                .select()
                .apis(basePackage(MODULES_PACKAGE+".xcx"))
                .paths(PathSelectors.any())
                .build()
                .groupName(moduleName);
        if (ENABLE_SECURITY) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        return docket;
    }

}
