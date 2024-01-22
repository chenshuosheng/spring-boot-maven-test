/*
package css.common.config.crossconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

*/
/**
 * @Description: 全局跨域配置类
 * @Author: CSS
 * @Date: 2023/11/27 10:57
 *//*


//CrosConfig和CorsFilter只能配一个，含有同名bean



@Configuration
public class CorsConfig implements WebMvcConfigurer {
    */
/**
     * 跨域配置
     *//*

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/hello/**")       //允许跨域请求的url
                //.allowedOrigins("*")                //允许跨域的请求域名
                .allowedMethods("get")                //允许的请求方法
                .allowedHeaders("*")                //允许的header属性
                .allowCredentials(true)             //设置是否允许发送身份凭证（cookies、HTTP认证等）。
                .maxAge(3600);                      //设置预检请求（OPTIONS请求）的最长缓存时间，单位为秒
    }



    //Spring MVC框架默认已经集成了一些CORS的支持。
    //比如会处理一些预检OPTION请求,会在响应头中加入一些CORS需要的头信息(比如Access-Control-Allow-Origin)。
    //从安全性和灵活性上考虑,一般建议自己定义CorsFilter,而不仅仅依赖Spring MVC默认的简单CORS处理。


    //用来构建一个CorsFilter过滤器,并通过UrlBasedCorsConfigurationSource为所有的URL路径"/"注册了刚才构建的跨域配置。
    //这个过滤器用来在请求处理之前检查CORS头,实现跨域资源共享。
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
*/
