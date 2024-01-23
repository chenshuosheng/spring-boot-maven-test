package css.testmybatis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/29 9:12
 */

@EnableSwagger2WebMvc
@Configuration
public class Knife4jConfiguration {

    @Bean(value = "mybatis-test")
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .description("mybatis的练习")
                                .title("mybatis-test")
                                .contact(new Contact("SsChen","",""))
                                .version("1.0")
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("css"))
                .paths(PathSelectors.any())
                .build();
    }
}
