package css.module.intelligentManagetable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import css.common.vo.ResultVo;
import css.module.intelligentManagetable.entity.AnTable;

import java.sql.SQLException;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/25 22:05
 */


public interface TableManageService extends IService<AnTable> {
    ResultVo<String> create(AnTable anTable) throws SQLException, ClassNotFoundException;

    ResultVo<String> delete(String databaseName, String tableName) throws SQLException;
}
