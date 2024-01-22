package css.common.config.inlinedocument;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/*
 * @Description: 生成在线文档
 * @Author: CSS
 * @Date: 2023/11/23 22:43
 *
 */



@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("数据库信息管理")
                        .description("包含数据库、数据表、列元素的增删改查，个人想法实现实时统计")
                        .contact(new Contact("SsChen","",""))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("database-manage")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("css"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
