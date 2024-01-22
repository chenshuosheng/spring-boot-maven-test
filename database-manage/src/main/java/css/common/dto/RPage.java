package css.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 增加分页限制，限制每页最大只能100条数据
 * @param <T>
 */
@Data
public class RPage<T> extends Page<T> {

    public RPage(long current, long size) {
        super(current, size);
        if (this.size>100) {
            this.size = 100L;
        }
    }

}
