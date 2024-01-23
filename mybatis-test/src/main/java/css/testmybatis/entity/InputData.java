package css.testmybatis.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 从excel表导入数据所转化的实体类
 * @Author: CSS
 * @Date: 2023/11/20 9:59
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData implements Serializable {

    private String id;

    @ExcelProperty(value = "学号")
    private String stuId;

    @ExcelProperty(value = "姓名")
    private String stuName;

    @ExcelProperty(value = "年龄")
    private int age;

    @ExcelProperty(value = "性别")
    private String sex;

    @ExcelProperty(value = "生日")
    /*@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")*/
    private String birthday;
}
