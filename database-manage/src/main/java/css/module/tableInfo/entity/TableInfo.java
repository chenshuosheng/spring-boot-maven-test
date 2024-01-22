package css.module.tableInfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据表信息
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("table_info")
@ApiModel(value="TableInfo对象", description="数据表信息")
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "对应于所属数据库的数据库名在database_info表中的id")
    @TableField("database_id")
    private Integer databaseId;

    @ApiModelProperty(value = "数据表名")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty(value = "数据表属性个数")
    @TableField("column_num")
    private Integer columnNum;


}
