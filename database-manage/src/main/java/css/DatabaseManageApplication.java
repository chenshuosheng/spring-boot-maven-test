package css;

import css.common.util.MyStringUtils;
import css.module.testyml.Father;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/24 17:56
 */

@MapperScan("css/module/*/mapper")
//@ServletComponentScan // 扫描Servlet组件，包括过滤器
@SpringBootApplication
public class DatabaseManageApplication {

    private static final Logger Log = LoggerFactory.getLogger(DatabaseManageApplication.class);

    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext run = SpringApplication.run(DatabaseManageApplication.class, args);

        Father father = run.getBean(Father.class);
        System.out.println(father);

        /*System.out.println("-------------------------------------------------");
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("-------------------------------------------------");*/

        Environment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = MyStringUtils.getString(env.getProperty("server.servlet.context-path"));
        String swaggerTail = "swagger-ui.html";
        String knif4jTail = "doc.html";
        if(MyStringUtils.isNotEmpty(path)){
            swaggerTail = path + "/" + swaggerTail;
            knif4jTail = path + "/" + knif4jTail;
        }

        Log.info("---------------------------------------------------------------------\n" +
                "Local：      \thttp://localhost:" + port + "/" + path + "\n" +
                "Swagger文档：\thttp://" + ip + ":" + port + "/" + swaggerTail + "\n" +
                "knif4j文档： \thttp://" + ip + ":" + port + "/" + knif4jTail + "\n" +
                "---------------------------------------------------------------------");
    }
}
