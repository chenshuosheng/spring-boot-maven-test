package css.testmybatis.service;

import css.testmybatis.entity.InputData;
import css.testmybatis.vo.ListResult;

import java.io.IOException;
import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/20 10:12
 */
public interface InputDataService {
    int readFromExcel(String path);

    void writeToExcel(String path,String filename) throws IOException;

    InputData queryById(Integer id);

    void saveList(List<InputData> list);

    List<InputData> list();

    int count();

    ListResult<InputData> listPageByLimit(Integer pageNum, Integer pageSize);

    ListResult<InputData> listPageByRowBounds(Integer pageNum, Integer pageSize);

    ListResult<InputData> listPageByPageHelper(Integer pageNum, Integer pageSize);
}
