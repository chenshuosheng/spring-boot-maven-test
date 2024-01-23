package css.testmybatis.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import css.testmybatis.entity.InputData;
import css.testmybatis.listener.InputDataListener;
import css.testmybatis.mapper.InputDataMapper;
import css.testmybatis.service.InputDataService;
import css.testmybatis.vo.ListResult;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 业务层
 * @Author: CSS
 * @Date: 2023/11/20 10:13
 */

@Service
public class InputDataServiceImpl implements InputDataService {
    @Autowired
    private InputDataMapper inputDataMapper;

    @Override
    public int readFromExcel(String path) {
        EasyExcel.read(path, InputData.class, new InputDataListener(this)).sheet().doRead();
        return count();
    }

    @Override
    public void writeToExcel(String path, String filename) throws IOException {
        File file = new File(path, filename);
        if (!file.exists()) {
            file.mkdirs();
        }

        List<InputData> list = list();
        OutputStream outputStream = new FileOutputStream(file);
        try {
            ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("学生信息表").build();

            List<List<Object>> sheet = new ArrayList<>();
            //添加表头
            sheet.add(Arrays.asList("id","stu_id", "stu_name", "age", "sex", "birthday"));

            for (InputData inputData : list) {
                sheet.add(Arrays.asList(inputData.getId(), inputData.getStuId(), inputData.getStuName(), inputData.getAge(), inputData.getSex(), inputData.getBirthday()));
            }

            excelWriter.write(sheet, writeSheet);
            excelWriter.finish();
            System.out.println("Excel 文件已生成：" + filename);
        } catch (Exception e) {
            System.err.println("导出 Excel 文件失败：" + e.getMessage());
        }finally {
            outputStream.close();
        }

    }

    @Override
    public InputData queryById(Integer id) {
        return inputDataMapper.queryById(id);
    }

    @Override
    public void saveList(List<InputData> list) {
        inputDataMapper.saveAll(list);
    }

    @Override
    public List<InputData> list() {
        return inputDataMapper.queryList();
    }

    @Override
    public int count() {
        return inputDataMapper.queryCount();
    }

    @Override
    public ListResult<InputData> listPageByLimit(Integer pageNum, Integer pageSize) {
        int index = (pageNum - 1) * pageSize;
        List<InputData> list = inputDataMapper.pageByLimit(index,pageSize);
        return new ListResult<>(list.size(),list);
    }

    @Override
    public ListResult<InputData> listPageByRowBounds(Integer pageNum, Integer pageSize) {
        //RowBounds对象有2个属性，offset和limit。
        //offset:起始行数
        //limit：需要的数据行数
        int index = (pageNum - 1) * pageSize;
        List<InputData> list = inputDataMapper.pageByRowBounds(new RowBounds(index,pageSize));
        return new ListResult<>(list.size(),list);
    }

    @Override
    public ListResult<InputData> listPageByPageHelper(Integer pageNum, Integer pageSize) {

        //TODO: 使用mybatis分页插件
        PageHelper.startPage(pageNum, pageSize);
        List<InputData> inputData = inputDataMapper.queryList();
        PageInfo<InputData> pageInfo = new PageInfo<>(inputData);
        return new ListResult<>(pageInfo.getTotal(),pageInfo.getList());
    }
}
