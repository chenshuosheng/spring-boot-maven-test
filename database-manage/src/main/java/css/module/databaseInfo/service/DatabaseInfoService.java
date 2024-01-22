package css.module.databaseInfo.service;

import css.module.databaseInfo.entity.DatabaseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import css.module.databaseInfo.vo.DatabaseVo;

/**
 * <p>
 * 数据库信息 服务类
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
public interface DatabaseInfoService extends IService<DatabaseInfo> {

    /**
     * 创建数据库信息，默认管理数据表数量为0
     *
     * @param model
     * @return
     */
    DatabaseInfo create(DatabaseVo model);

    /**
     * 插入(非新创建)数据库信息
     *
     * @param info
     * @return
     */
    DatabaseInfo insert(DatabaseInfo info);


    /**
     * 根据id删除数据库相关信息
     *
     * @param databaseId
     * @return
     */
    boolean deleteById(Integer databaseId);
}
