package css.module.tableDetailInfo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/25 15:21
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnVo {

    @ApiModelProperty(value = "id，服务端生成")
    private Integer id;

    @ApiModelProperty(value = "所属数据库id")
    private Integer databaseId;

    //@ApiModelProperty(value = "所属数据库名")
    //private String databaseName;

    @ApiModelProperty(value = "所属数据表id")
    private Integer tableId;

    //@ApiModelProperty(value = "所属数据表名")
    //private String tableName;

    @ApiModelProperty(value = "列名(用逗号分隔)")
    private String columnsName;

    @ApiModelProperty(value = "列元素类型(用逗号分隔)")
    private String columnsType;

    @ApiModelProperty(value = "列值是否可以为空(用逗号分隔)")
    private String columnCanNull;

    @ApiModelProperty(value = "所有键")
    private String allKey;

    @ApiModelProperty(value = "主键")
    private String primaryKey;
}
