package css.module.tableDetailInfo.controller;

import css.common.vo.ListResult;
import css.common.vo.ResultVo;
import css.common.vo.SuccessCount;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import css.module.tableDetailInfo.service.TableDetailInfoService;
import css.module.tableDetailInfo.vo.ColumnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  前端控制器
 * @author CSS
 * @since 2023-11-25
 */

@Api(tags = "数据表详情管理")
@RestController
@RequestMapping("/columnInfo/columnInfo")
public class TableDetailInfoController {

    @Autowired
    private TableDetailInfoService tableDetailInfoService;

    /**
     * 新增
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/create")
    public ResponseEntity<ResultVo<TableDetailInfo>> create(@RequestBody ColumnVo model){
        return new ResponseEntity<>(tableDetailInfoService.insert(model), HttpStatus.OK);
    }


    /**
     * 修改
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public ResponseEntity<ResultVo<TableDetailInfo>> update(@RequestBody ColumnVo model){
        TableDetailInfo update = tableDetailInfoService.update(model);
        if(update==null)
            return ResponseEntity.ok(new ResultVo<>(false,"更新失败！",null));
        return new ResponseEntity<>(new ResultVo<>(true, null, update), HttpStatus.OK);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResponseEntity<ResultVo<SuccessCount>> delete(String id) {
        return new ResponseEntity<>(new ResultVo<>(true, null, tableDetailInfoService.delete(id)), HttpStatus.OK);
    }


    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取")
    @GetMapping("/getById")
    public ResponseEntity<ResultVo<TableDetailInfo>> getById(String id){
        return new ResponseEntity<>(new ResultVo<>(true, null, tableDetailInfoService.getById(id)), HttpStatus.OK);
    }


    /**
     * 列表查询（非分页）
     *
     * @return
     */
     @ApiOperation(value = "获取（非分页）")
     @GetMapping("/getList")
     public ResponseEntity<ResultVo<ListResult<TableDetailInfo>>> getList(String filter, String order){
       return new ResponseEntity<>(new ResultVo<>(true, null, tableDetailInfoService.list(filter, order)), HttpStatus.OK);
     }


    /**
     * 列表查询（分页）
     *
     * @return
     */
    @ApiOperation(value = "获取（分页）")
    @GetMapping("/getPaged")
    public ResponseEntity<ResultVo<ListResult<TableDetailInfo>>> getPaged(String filter, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String order){
        return new ResponseEntity<>(new ResultVo<>(true, null, tableDetailInfoService.pageList(filter, pageNum, pageSize, order)), HttpStatus.OK);
    }



    /**
     * 列表查询（获取所有）
     *
     * @return
     */
    @ApiOperation(value = "获取所有")
    @GetMapping("/getAll")
    public ResponseEntity<ResultVo<ListResult<TableDetailInfo>>> getList(){
        List<TableDetailInfo> list = tableDetailInfoService.list();
        return new ResponseEntity<>(new ResultVo<>(true, null, new ListResult<>(list.size(),list)), HttpStatus.OK);
    }

}

