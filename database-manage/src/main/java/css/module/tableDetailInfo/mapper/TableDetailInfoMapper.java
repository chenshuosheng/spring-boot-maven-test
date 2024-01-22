package css.module.tableDetailInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CSS
 * @since 2023-11-25
 */
@Mapper
@Repository
public interface TableDetailInfoMapper extends BaseMapper<TableDetailInfo> {

}

