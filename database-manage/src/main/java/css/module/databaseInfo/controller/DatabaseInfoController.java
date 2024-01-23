package css.module.databaseInfo.controller;


import css.common.util.MyStringUtils;
import css.common.util.sql.ExecuteSQL;
import css.common.vo.ListResult;
import css.common.vo.ResultVo;
import css.module.databaseInfo.dto.DatabaseInfoDto;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.databaseInfo.service.DatabaseInfoService;
import css.module.databaseInfo.vo.DatabaseVo;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import css.module.tableDetailInfo.service.TableDetailInfoService;
import css.module.tableDetailInfo.vo.ColumnVo;
import css.module.tableInfo.dto.TablesInfoDto;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.service.TableInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 数据库信息 前端控制器
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
@Api(tags = "数据库管理")
@RestController
@RequestMapping("/datsbaseinfomange/databaseInfo")
public class DatabaseInfoController {

    @Autowired
    private DatabaseInfoService databaseInfoService;

    @Autowired
    private TableInfoService tableInfoService;

    @Autowired
    private TableDetailInfoService tableDetailInfoService;


    @ApiOperation(value = "读取数据库信息，写入数据库")
    @GetMapping(value = "read")
    public void readInfoToDataBase() throws SQLException {
        DatabaseInfoDto databaseInfoDto = ExecuteSQL.getDatabaseInfo();
        HashMap<String, TablesInfoDto> databases = databaseInfoDto.getDatabases();
        databases.forEach((k,v)->{
            //插入数据库信息
            DatabaseInfo databaseInfo = new DatabaseInfo();
            databaseInfo.setDatabaseName(k);
            databaseInfo.setTableNum(0);//在插入数据表时会自增
            try {
                databaseInfo = databaseInfoService.insert(databaseInfo);
                //处理数据表信息
                List<String> tables = v.getTables();
                for (String tableName : tables) {
                    //插入数据表简要信息
                    TableInfo tableInfo = tableInfoService.insert(new TableInfo(null, databaseInfo.getId(), tableName, 0));
                    ColumnVo columnVo = null;
                    try {
                        columnVo = ExecuteSQL.getProperties(databaseInfo, tableInfo);
                        //更新数据表信息(列数)
                        tableInfoService.updateById(tableInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //插入数据表详细信息
                    tableDetailInfoService.insert(columnVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @ApiOperation(value = "新增数据库信息")
    @PostMapping(value = "create")
    public ResultVo<DatabaseInfo> createDatabaseInfo(@ApiParam(value = "数据库信息")
                                                         @RequestBody DatabaseVo model) {
        DatabaseInfo databaseInfo = databaseInfoService.create(model);
        if(databaseInfo!=null)
            return new ResultVo<>(true,databaseInfo);
        return new ResultVo<>(false, "新增失败！", null);
    }


    @ApiOperation(value = "删除所有数据库信息")
    @DeleteMapping(value = "deleteAll")
    @Transactional
    public ResultVo<String> deleteAll() {
        //查询所有记录
        List<DatabaseInfo> list = databaseInfoService.list();

        //逐个删除
        for (DatabaseInfo databaseInfo : list) {
            deleteById(databaseInfo.getId());
        }

        return new ResultVo<>(true, "成功删除");
    }


    @ApiOperation(value = "根据id删除指定数据库信息")
    @DeleteMapping(value = "deleteOne")
    public ResultVo<String> deleteById(@ApiParam(value = "数据库对应id")
                                           @RequestParam Integer databaseId) {

        boolean success = databaseInfoService.deleteById(databaseId);

        if (success) {
            return new ResultVo<>(true, "成功删除！");
        }
        return new ResultVo<>(false, "删除失败！", null);
    }


    @ApiOperation(value = "获取所有数据库信息")
    @GetMapping(value = "queryAll")
    public ResultVo<ListResult<DatabaseInfo>> queryAll() {
        List<DatabaseInfo> list = databaseInfoService.list();
        return new ResultVo<>(true, new ListResult<>(list.size(),list));
    }


    @ApiOperation(value = "更新指定数据库名称")
    @PutMapping(value = "updateName")
    public ResultVo<DatabaseInfo> updateNameById(@ApiParam(value = "数据库信息")
                                                 @RequestBody DatabaseVo model) {

        Integer databaseId = model.getId();
        DatabaseInfo old = databaseInfoService.getById(databaseId);
        if(old==null){
            return new ResultVo<>(false, "不存在该数据库，更新失败！", null);
        }

        String oldDatabaseName = old.getDatabaseName();
        String newDatabaseName = model.getDatabaseName();

        if(MyStringUtils.isEmpty(newDatabaseName))
            return new ResultVo<>(false, "数据库名称不可为空！", null);

        if(oldDatabaseName.equals(newDatabaseName))
            return new ResultVo<>(true, "数据无需更新！", old);

        old.setDatabaseName(newDatabaseName);
        boolean success = databaseInfoService.updateById(old);
        if (success) {

            //更新column_info表
            List<TableDetailInfo> list = tableDetailInfoService.getByDatabaseId(databaseId);
            for (TableDetailInfo item: list) {
                item.setDatabaseName(newDatabaseName);
                tableDetailInfoService.updateById(item);
            }

            return new ResultVo<>(true, old);
        }
        return new ResultVo<>(false, "更新失败！", null);
    }
}

