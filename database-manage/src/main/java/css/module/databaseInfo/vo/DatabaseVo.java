package css.module.databaseInfo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/25 16:29
 */

@Data
@ApiModel(value="DatabaseVo对象", description="数据库简要信息")
public class DatabaseVo {

    @ApiModelProperty(value = "数据库id")
    private Integer id;

    @ApiModelProperty(value = "数据库名")
    private String databaseName;
}
