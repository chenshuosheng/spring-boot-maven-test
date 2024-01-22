package css.module.intelligentManagetable.controller;

import css.common.util.sql.ExecuteSQL;
import css.common.vo.ResultVo;
import css.module.intelligentManagetable.entity.AnTable;
import css.module.intelligentManagetable.service.TableManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * @Description: 智能管理数据表
 * @Author: CSS
 * @Date: 2023/11/25 22:01
 */


@Api(tags = "智能管理数据表")
@RestController
@RequestMapping("/tableManage")
public class TableManageController {

    @Autowired
    private TableManageService tableManageService;

    @ApiOperation(value = "生成数据表")
    @PostMapping(value = "/create")
    public ResultVo<String> createTable(@RequestBody AnTable anTable) throws SQLException, ClassNotFoundException {
        return tableManageService.create(anTable);
    }

    @ApiOperation(value = "删除数据表")
    @PostMapping(value = "/delete")
    public ResultVo<String> deleteTable(String databaseName, String tableName) throws SQLException{
        return tableManageService.delete(databaseName,tableName);
    }

    @ApiOperation(value = "下载sql文件")
    @GetMapping(value = "downloadSql")
    public ResponseEntity downloadSql(@ApiParam(value = "数据库名")
                                      @RequestParam String databaseName,
                                      @ApiParam(value = "数据表名")
                                      @RequestParam String tableName) throws SQLException{
        String tableSql = ExecuteSQL.getTableSql(databaseName, tableName);
        if (tableSql == null) {
            return ResponseEntity.badRequest().body("获取资源失败！请检查所填信息是否有误");
        }else{

            // 将字符串转换为字节数组
            byte[] bytes =tableSql.getBytes(StandardCharsets.UTF_8);

            // 创建InputStreamResource
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            Resource resource = new InputStreamResource(inputStream);

            // 设置响应头信息

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = "+tableName+".sql");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            return ResponseEntity.ok().headers(headers).contentLength(bytes.length).body(resource);
        }
    }
}
