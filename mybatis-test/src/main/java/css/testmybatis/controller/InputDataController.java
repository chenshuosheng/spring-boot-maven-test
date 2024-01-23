package css.testmybatis.controller;

import css.testmybatis.entity.InputData;
import css.testmybatis.service.InputDataService;
import css.testmybatis.vo.ListResult;
import css.testmybatis.vo.ResultVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 控制层，从excel表导入数据转为实体类存储到数据库
 * @Author: CSS
 * @Date: 2023/11/20 10:30
 */

@RestController
public class InputDataController {

    @Autowired
    private InputDataService inputDataService;

    @GetMapping(value = "/read")
    public ResultVo<Integer> startInput() {
        String basePath = System.getProperty("user.dir");
        String path = basePath + "\\mybatis-test\\src\\main\\resources\\static\\test.xlsx";
        inputDataService.readFromExcel(path);
        //List<InputData> list = inputDataService.list();
        int count = inputDataService.count();
        return new ResultVo(true, count);
    }


    @ApiOperation(value = "先查询所有，然后用subList截取")
    @GetMapping(value = "subList")
    public ResultVo<ListResult<InputData>> subList(@ApiParam(value = "页码")
                                                   @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @ApiParam(value = "页数")
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<InputData> list = inputDataService.list();

        int offset = (pageNum - 1) * pageSize;
        int count = inputDataService.count();
        int end = offset + pageSize <= count ? offset + pageSize : count;
        List<InputData> inputData = list.subList(offset, end);
        ListResult<InputData> result = new ListResult<>();
        result.setTotal(count);
        result.setRecord(inputData);
        return new ResultVo<>(true, null, result);
    }


    @ApiOperation(value = "通过limit查询分页结果")
    @GetMapping(value = "list1")
    public ResultVo<ListResult<InputData>> listPageByLimit(@ApiParam(value = "页码")
                                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                           @ApiParam(value = "页数")
                                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListResult<InputData> list = inputDataService.listPageByLimit(pageNum, pageSize);
        return new ResultVo<>(true, null, list);
    }


    //这个属于逻辑分页，即实际上sql查询的是所有的数据，在业务层进行了分页而已，比较占用内存，
    //而且数据更新不及时，可能会有一定的滞后性！不推荐使用！
    @ApiOperation(value = "通过RowBounds查询分页结果")
    @GetMapping(value = "list2")
    public ResultVo<ListResult<InputData>> listPageByRowBounds(@ApiParam(value = "页码")
                                                               @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                               @ApiParam(value = "页数")
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListResult<InputData> list = inputDataService.listPageByRowBounds(pageNum, pageSize);
        return new ResultVo<>(true, null, list);
    }


    //Mybatis_PageHelper分页插件，是物理分页！
    @ApiOperation(value = "通过Mybatis_PageHelper分页插件查询分页结果")
    @GetMapping(value = "list3")
    public ResultVo<ListResult<InputData>> listPageByPageHelper(@ApiParam(value = "页码")
                                                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                @ApiParam(value = "页数")
                                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListResult<InputData> list = inputDataService.listPageByPageHelper(pageNum, pageSize);
        return new ResultVo<>(true, null, list);
    }
}
