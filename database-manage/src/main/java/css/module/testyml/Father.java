package css.module.testyml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/12/12 16:11
 */

@Repository
@ConfigurationProperties(prefix = "father")
@Data
public class Father{
    private String name;
    private Integer age;
    private Date birthday;
    private Boolean hasChild;
    private List<Child> children;
}
