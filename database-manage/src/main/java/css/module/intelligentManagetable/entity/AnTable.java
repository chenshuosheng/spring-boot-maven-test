package css.module.intelligentManagetable.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 一张数据表的实体类
 * @Author: CSS
 * @Date: 2023/11/25 21:56
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AnTable {
    @ApiModelProperty(value = "数据表描述")
    private String comment;

    @ApiModelProperty(value = "数据库名")
    private String databaseName;

    @ApiModelProperty(value = "数据表名")
    private String tableName;

    @ApiModelProperty("数据表属性列表")
    private List<ColumnProperty> columnProperties;
}
