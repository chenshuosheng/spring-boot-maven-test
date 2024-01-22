package css.module.databaseInfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.databaseInfo.mapper.DatabaseInfoMapper;
import css.module.databaseInfo.service.DatabaseInfoService;
import css.module.databaseInfo.vo.DatabaseVo;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据库信息 服务实现类
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
@Service
public class DatabaseInfoServiceImpl extends ServiceImpl<DatabaseInfoMapper, DatabaseInfo> implements DatabaseInfoService {

    @Autowired
    private TableInfoService tableInfoService;



    @Override
    public DatabaseInfo create(DatabaseVo model) {
        DatabaseInfo info = new DatabaseInfo(null, model.getDatabaseName(), 0);
        return this.insert(info);
    }

    @Override
    public DatabaseInfo insert(DatabaseInfo info) {
        boolean success = this.save(info);
        if (success) {
            LambdaQueryWrapper<DatabaseInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DatabaseInfo::getDatabaseName, info.getDatabaseName());
            return this.getOne(wrapper);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteById(Integer databaseId) {
        //先删除所含数据表
        LambdaQueryWrapper<TableInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableInfo::getDatabaseId, databaseId);
        //获取数据表列表
        List<TableInfo> tableInfoList = tableInfoService.list(wrapper);
        for (TableInfo tableInfo : tableInfoList) {
            //逐个删除数据表及其列元素信息
            tableInfoService.deleteWithDetailInfo(tableInfo.getId());
        }

        //再删除数据库
        return this.removeById(databaseId);
    }
}
