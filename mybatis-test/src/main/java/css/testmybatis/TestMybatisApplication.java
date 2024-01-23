package css.testmybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;


@MapperScan(value = "css/**/mapper")
@SpringBootApplication
@Slf4j
public class TestMybatisApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(TestMybatisApplication.class, args);

        Environment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("\n\thttp://localhost:{}/doc.html\n" +
                "\thttp://{}:{}/doc.html\n",port,ip,port);
    }

}
