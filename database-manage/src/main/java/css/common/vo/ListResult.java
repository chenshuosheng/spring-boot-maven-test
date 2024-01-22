package css.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/23 21:53
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResult<T>{

/*    //结果状态：true/false
    private boolean success;

    //消息：成功/失败
    private String msg;*/

    //当失败时，默认为0
    private long total;

    //当失败时，默认为null
    private List<T> record;

}
