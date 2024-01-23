package css.testmybatis;

import css.testmybatis.entity.InputData;
import css.testmybatis.service.InputDataService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/20 10:38
 */

@SpringBootTest
public class TestHandleExcel {

    private static final Logger Log = LoggerFactory.getLogger(TestHandleExcel.class);

    @Autowired
    private InputDataService inputDataService;


    @Test
    public void input() {
        String basePath = System.getProperty("user.dir");
        String path = basePath + "\\src\\main\\resources\\static\\test.xlsx";
        int count = inputDataService.readFromExcel(path);
        System.out.println(count);
    }

    @Test
    public void output() throws IOException {
        String basePath = System.getProperty("user.dir");
        String path = basePath + "\\src\\main\\resources\\static";
        String filename = "test2.xlsx";
        inputDataService.writeToExcel(path, filename);//当前为续写
    }

    @Test
    public void queryById() {
        Integer id = new Integer(1);
        InputData inputData = inputDataService.queryById(id);
        System.out.println(inputData);
    }

}
