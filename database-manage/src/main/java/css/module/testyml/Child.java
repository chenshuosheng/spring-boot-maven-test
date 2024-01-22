package css.module.testyml;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/12/12 16:10
 */

@Data
public class Child{
    private String name;
    private Integer age;
    private Date birthday;
    private List<Dog> dogs;
    private Map<String, Cat> cats;
}
