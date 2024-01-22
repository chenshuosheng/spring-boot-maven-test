package css.module.intelligentManagetable.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 数据表的每一个属性
 * @Author: CSS
 * @Date: 2023/11/25 21:45
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnProperty {

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "列类型")
    private String columnType;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "是否可为空")
    private boolean canNull;

    @ApiModelProperty(value = "是否自增")
    private boolean autoINC;

    @ApiModelProperty(value = "是否唯一")
    private boolean unique;

    @ApiModelProperty(value = "键的类型：普通属性为空，主键为：PRI(pri)")
    private String keyType;
}
