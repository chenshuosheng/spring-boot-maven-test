package css.module.tableDetailInfo.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



/**
 * <p>
 *
 * </p>
 *
 * @author CSS
 * @since 2023-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("table_detail_info")
@ApiModel(value = "TableDetailInfo对象" , description = "数据表详细信息")
public class TableDetailInfo implements Serializable{

    private static final long serialVersionUID=1L;

    public static final Map<String, String> COLUMN_MAP = new HashMap<String, String>(10){{
        put("id", "id");
        put("databaseId", "database_id");
        put("databaseName", "database_name");
        put("tableId", "table_id");
        put("tableName", "table_name");
        put("columnsName", "columns_name");
        put("columnsType", "columns_type");
        put("columnCanNull", "column_can_null");
        put("allKey", "all_key");
        put("primaryKey", "primary_key");
    }};

    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "所属数据库id")
    @TableField(value = "database_id", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private Integer databaseId;

    @ApiModelProperty(value = "所属数据库名")
    @TableField(value = "database_name", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String databaseName;

    @ApiModelProperty(value = "所属数据表id")
    @TableField(value = "table_id", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private Integer tableId;

    @ApiModelProperty(value = "所属数据表名")
    @TableField(value = "table_name", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String tableName;

    @ApiModelProperty(value = "列名(用逗号分隔)")
    @TableField(value = "columns_name", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String columnsName;

    @ApiModelProperty(value = "列元素类型(用逗号分隔)")
    @TableField(value = "columns_type", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String columnsType;

    @ApiModelProperty(value = "列值是否可以为空(用逗号分隔)")
    @TableField(value = "column_can_null", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String columnCanNull;

    @ApiModelProperty(value = "所有键")
    @TableField(value = "all_key", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String allKey;

    @ApiModelProperty(value = "主键")
    @TableField(value = "primary_key", updateStrategy = FieldStrategy.IGNORED, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String primaryKey;


}

