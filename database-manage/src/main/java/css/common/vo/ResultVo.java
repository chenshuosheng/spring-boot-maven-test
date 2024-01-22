package css.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 前端交互数据
 * @Author: CSS
 * @Date: 2023/11/20 10:32
 */



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo<T> {

    @ApiModelProperty(value = "是否成功标志")
    private boolean success;

    @ApiModelProperty(value = "失败时返回错误原因，成功时为空")
    private String error;

    @ApiModelProperty(value = "结果集")
    private T result;

    public ResultVo(boolean success, T result){
        this.success = success;
        this.result = result;
    }
}
