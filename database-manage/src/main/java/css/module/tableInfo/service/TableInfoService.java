package css.module.tableInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import css.common.vo.ResultVo;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.vo.TableVo;

/**
 * <p>
 * 数据表信息 服务类
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
public interface TableInfoService extends IService<TableInfo> {

    /**
     * 新增数据表简要信息(模拟新建数据表：所属数据库id、表名，列数为0)
     *
     * @param model
     * @return
     */
    TableInfo create(TableVo model);

    /**
     * 插入数据表详细信息(模拟添加详细列后的数据表信息：所属数据库id、表名、详细列数)
     *
     * @param info
     * @return
     */
    TableInfo insert(TableInfo info);


    /**
     * 删除数据表相关记录
     *
     * @param id
     * @return
     */
    boolean deleteWithDetailInfo(Integer id);


    /**
     *
     * @param model
     * @param oldTableInfo
     * @return
     */
    ResultVo<TableInfo> updateAboutTable(TableVo model, TableInfo oldTableInfo);
}
