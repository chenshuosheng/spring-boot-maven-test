package css.module.tableInfo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import css.common.util.MyStringUtils;
import css.common.vo.ResultVo;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.databaseInfo.service.DatabaseInfoService;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import css.module.tableDetailInfo.service.TableDetailInfoService;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.mapper.TableInfoMapper;
import css.module.tableInfo.service.TableInfoService;
import css.module.tableInfo.vo.TableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据表信息 服务实现类
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

    @Autowired
    private DatabaseInfoService databaseInfoService;

    @Autowired
    private TableDetailInfoService tableDetailInfoService;

    @Override
    public TableInfo create(TableVo model) {
        TableInfo info = new TableInfo(null, model.getDatabaseId(), model.getTableName(), 0);
        return insert(info);
    }

    @Override
    public TableInfo insert(TableInfo info) {

        Integer databaseId = info.getDatabaseId();
        String tableName = info.getTableName();
        if (databaseId == null || MyStringUtils.isEmpty(tableName)) {
            ;
        } else {
            //查询是否已经存在相应的记录，是则不新增
            LambdaQueryWrapper<TableInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TableInfo::getDatabaseId, databaseId)
                    .eq(TableInfo::getTableName, tableName);
            TableInfo tableInfo = this.getOne(wrapper);
            if (tableInfo == null) {
                //获取所属数据库信息
                DatabaseInfo databaseInfo = databaseInfoService.getById(databaseId);
                //对应数据库是否存在
                if (databaseInfo != null) {
                    boolean success = this.save(info);
                    if (success) {
                        //数据库所管理表数量加1
                        databaseInfo.setTableNum(databaseInfo.getTableNum() + 1);
                        databaseInfoService.updateById(databaseInfo);

                        LambdaQueryWrapper<TableInfo> wrapper2 = new LambdaQueryWrapper<>();
                        wrapper2.eq(TableInfo::getDatabaseId, databaseId)
                                .eq(TableInfo::getTableName, tableName);
                        return this.getOne(wrapper2);
                    }
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteWithDetailInfo(Integer id) {
        //先查看对应数据表是否存在
        TableInfo tableInfo = this.getById(id);

        if (tableInfo != null) {
            //删除表格详细信息————>column_info表
            tableDetailInfoService.removeByTableId(tableInfo.getId());
            this.removeById(id);
            return true;
        }

        return false;
    }


    @Override
    public ResultVo<TableInfo> updateAboutTable(TableVo model, TableInfo oldTableInfo) {

        Integer tableId = model.getId();
        String tableName = model.getTableName();

        Integer newDatabaseId = model.getDatabaseId();
        Integer oldDatabaseId = oldTableInfo.getDatabaseId();

        DatabaseInfo oldDatabaseInfo = databaseInfoService.getById(oldDatabaseId);
        DatabaseInfo newDatabaseInfo = databaseInfoService.getById(newDatabaseId);
        if (newDatabaseId != null) {
            int flag = 0;

            //未改变归属数据库，数据库一定存在
            if (oldDatabaseId == newDatabaseId) {
                flag = 1;
            } else {//改变归属数据库，数据库不一定存在
                //更新指定的数据库是否存在
                if (newDatabaseInfo != null) {
                    flag = 2;
                } else {
                    return new ResultVo<>(false, "不存在相应的数据库！", null);
                }
            }

            //更新数据表记录
            TableInfo newTableInfo = new TableInfo(tableId, newDatabaseId, tableName, oldTableInfo.getColumnNum());
            boolean success = this.updateById(newTableInfo);

            //更新数据表详细信息及数据库信息
            if (success) {
                //更新数据库信息
                if (flag == 2) {
                    //旧归属数据库所管理数据表数量减1
                    oldDatabaseInfo.setTableNum(oldDatabaseInfo.getTableNum() - 1);
                    databaseInfoService.updateById(oldDatabaseInfo);

                    //新归属数据库所管理数据表数量加1
                    newDatabaseInfo.setTableNum(newDatabaseInfo.getTableNum() + 1);
                    databaseInfoService.updateById(newDatabaseInfo);
                }

                //更新数据表详细信息
                TableDetailInfo record = tableDetailInfoService.getByDatabaseIdAndTableId(oldDatabaseId, tableId);
                record.setDatabaseName(newDatabaseInfo.getDatabaseName());
                record.setDatabaseId(newDatabaseId);
                record.setTableName(tableName);
                tableDetailInfoService.updateById(record);

                return new ResultVo<>(true, newTableInfo);
            }

        } else {
            return new ResultVo<>(false, "请指定所属数据库id后重试！", null);
        }
        return null;
    }
}
