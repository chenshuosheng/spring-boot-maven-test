package css.testmybatis.vo;

import lombok.Data;

/**
 * @Description: 前端交互数据
 * @Author: CSS
 * @Date: 2023/11/20 10:32
 */


@Data
public class ResultVo<T> {

    private boolean success;
    private String error;
    private T result;

    ResultVo(){}

    public ResultVo(boolean success, String error, T result){
        this.success = success;
        this.error = error;
        this.result = result;
    }

    public ResultVo(boolean success, T result){
        this.success = success;
        this.result = result;
    }
}
