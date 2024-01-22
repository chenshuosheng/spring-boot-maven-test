package css.common.config.inlinedocument;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/*
 * @Description: swagger2在线接口文档
 * @Author: CSS
 * @Date: 2023/11/23 22:55
 */


@Configuration//配置类
@EnableSwagger2WebMvc//开启swagger2
public class Swagger2Configuration {
    @Bean//加入ioc容器
    public Docket adminApiConfig(){
        //需要详情配置
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())//页面显示信息
                .select()//必须有select扫描
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("数据库信息管理")
                .description("包含数据库、数据表、列元素的增删改查，个人想法实现实时统计")
                .version("1.0")
                .build();
    }
}
