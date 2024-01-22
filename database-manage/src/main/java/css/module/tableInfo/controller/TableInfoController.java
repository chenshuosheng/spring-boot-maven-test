package css.module.tableInfo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import css.common.vo.ListResult;
import css.common.vo.ResultVo;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.databaseInfo.service.DatabaseInfoService;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.service.TableInfoService;
import css.module.tableInfo.vo.TableVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据表信息 前端控制器
 * </p>
 *
 * @author CSS
 * @since 2023-11-24
 */
@RestController
@RequestMapping("/datsbaseinfomange/tableInfo")
@Api(tags = "数据表管理")
public class TableInfoController {

    @Autowired
    private DatabaseInfoService databaseInfoService;

    @Autowired
    private TableInfoService tableInfoService;

    @ApiOperation(value = "新增数据表信息")
    @PostMapping(value = "create")
    public ResultVo<TableInfo> createTableInfo(@ApiParam(value = "数据表信息")
                                               @RequestBody TableVo model) {
        //正常来说不会是null
        if (model != null) {
            TableInfo tableInfo = tableInfoService.create(model);
            if (tableInfo != null) {
                return new ResultVo<>(true, tableInfo);
            }
        }

        return new ResultVo<>(false, "添加失败！", null);
    }


    @ApiOperation(value = "根据id删除指定数据表信息")
    @DeleteMapping(value = "delete")
    public ResultVo<String> deleteById(@ApiParam(value = "数据表对应id")
                                       @RequestParam Integer id) {
        //获取记录
        TableInfo tableInfo = tableInfoService.getById(id);
        if (tableInfo != null) {
            Integer databaseId = tableInfo.getDatabaseId();
            //获取所属数据库信息
            DatabaseInfo databaseInfo = databaseInfoService.getById(databaseId);
            //删除数据表<-----删除数据表详细信息
            boolean success = tableInfoService.deleteWithDetailInfo(id);
            if (success) {
                //数据库所管理表数量减1
                databaseInfo.setTableNum(databaseInfo.getTableNum() - 1);
                databaseInfoService.updateById(databaseInfo);
                return new ResultVo<>(true, "成功删除！");
            }
        }

        return new ResultVo<>(false, "删除失败！", null);
    }


    @ApiOperation(value = "获取所有数据表信息")
    @GetMapping(value = "queryAll")
    public ResultVo<ListResult<TableInfo>> queryAll() {
        List<TableInfo> list = tableInfoService.list();
        return new ResultVo<>(true, new ListResult<>(list.size(), list));
    }


    @ApiOperation(value = "获取指定数据库下所有数据表信息")
    @GetMapping(value = "queryDesignated")
    public ResultVo<ListResult<TableInfo>> queryByDatabaseId(@ApiParam(value = "指定数据库对应id")
                                                             @RequestParam Integer id) {
        LambdaQueryWrapper<TableInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableInfo::getDatabaseId, id);
        List<TableInfo> list = tableInfoService.list(wrapper);
        return new ResultVo<>(true, new ListResult<>(list.size(), list));
    }


    @ApiOperation(value = "更新指定数据表信息")
    @PutMapping(value = "update")
    public ResultVo<TableInfo> updateById(@ApiParam(value = "数据表信息")
                                          @RequestBody TableVo model) {

        Integer tableId = model.getId();

        if (tableId == null) {
            return new ResultVo<>(false, "请输入id！", null);
        }

        //根据指定id查询是否存在相应记录
        TableInfo oldTableInfo = tableInfoService.getById(tableId);
        if (oldTableInfo == null) {
            return new ResultVo<>(false, "请确认id是否正确，未查到相应记录！", null);
        }

        ResultVo<TableInfo> x = tableInfoService.updateAboutTable(model, oldTableInfo);
        if (x != null) return x;

        return new ResultVo<>(false, "更新失败！", null);
    }
}

