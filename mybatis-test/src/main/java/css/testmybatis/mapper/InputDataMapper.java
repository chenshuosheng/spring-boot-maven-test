package css.testmybatis.mapper;

import css.testmybatis.entity.InputData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Description: Dao层
 * @Author: CSS
 * @Date: 2023/11/20 10:11
 */

@Mapper
public interface InputDataMapper{


    void saveAll(@Param("list") List<InputData> list);

//    @Select("select * from test_excel.input_data where id = #{id}")
    InputData queryById(@Param("id") Integer id);

    List<InputData> queryList();

    int queryCount();

    List<InputData> pageByLimit(@Param("index") Integer index, @Param("pageSize") Integer pageSize);


    List<InputData> pageByRowBounds(@Param("rowBounds") RowBounds rowBounds);
}
