package css.module.intelligentManagetable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import css.common.util.sql.ExecuteSQL;
import css.common.util.sql.SQLGenerator;
import css.common.vo.ResultVo;
import css.module.intelligentManagetable.entity.AnTable;
import css.module.intelligentManagetable.entity.ColumnProperty;
import css.module.intelligentManagetable.mapper.TableManageMapper;
import css.module.intelligentManagetable.service.TableManageService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/25 22:06
 */

@Service
public class TableManageServiceImpl extends ServiceImpl<TableManageMapper, AnTable> implements TableManageService {

    @Override
    public ResultVo<String> create(AnTable anTable) throws SQLException, ClassNotFoundException {
        String comment = anTable.getComment();
        String databaseName = anTable.getDatabaseName();
        String tableName = anTable.getTableName();
        List<ColumnProperty> properties = anTable.getColumnProperties();

        //在大多数数据库中，executeUpdate() 方法默认只支持执行单个 SQL 语句，因此若同时执行多个已经会出现语法错误。
        //删除旧表
        ResultVo<String> vo = ExecuteSQL.deleteTable(databaseName, tableName);
        if(!vo.isSuccess())
            return vo;

        StringBuilder sb = new StringBuilder("CREATE TABLE ")
                .append(databaseName + "." + tableName + "(");
        for (ColumnProperty column : properties) {
            SQLGenerator.analysisColumnToSQL(databaseName, sb, column);
        }
        sb.append(")").append("comment '").append(comment).append("';");

        String s = sb.toString();
        int index = s.lastIndexOf(",");
        s = s.substring(0, index) + s.substring(index + 1);

        return ExecuteSQL.createTable(databaseName,tableName, s);
    }

    @Override
    public ResultVo<String> delete(String databaseName, String tableName) throws SQLException {
        return ExecuteSQL.deleteTable(databaseName,tableName);
    }
}
