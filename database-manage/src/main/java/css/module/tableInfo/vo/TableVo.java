package css.module.tableInfo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/25 17:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableVo {

    @ApiModelProperty(value = "数据表id")
    private Integer id;

    @ApiModelProperty(value = "对应于所属数据库的数据库名在database_info表中的id")
    private Integer databaseId;

    @ApiModelProperty(value = "数据表名")
    private String tableName;

}
