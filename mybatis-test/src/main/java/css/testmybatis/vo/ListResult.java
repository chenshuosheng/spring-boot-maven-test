package css.testmybatis.vo;

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

    //当失败时，默认为0
    private long total;

    //当失败时，默认为null
    private List<T> record;

}
