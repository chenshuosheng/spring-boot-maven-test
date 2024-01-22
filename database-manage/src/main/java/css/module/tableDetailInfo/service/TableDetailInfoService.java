package css.module.tableDetailInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import css.common.vo.ListResult;
import css.common.vo.ResultVo;
import css.common.vo.SuccessCount;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import css.module.tableDetailInfo.vo.ColumnVo;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CSS
 * @since 2023-11-25
 */


public interface TableDetailInfoService extends IService<TableDetailInfo> {

        /**
         * 查询
         *
         * @param filter 过滤条件
         * @return ListResult<TableDetailInfo>
         */
        ListResult<TableDetailInfo> list(String filter, String order);

        /**
         * 分页查询
         *
         * @param filter 过滤条件
         * @param pageNum  第几页
         * @param pageSize 每页数量
         * @return ListResult<TableDetailInfo>
         */
        ListResult<TableDetailInfo> pageList(String filter, Integer pageNum, Integer pageSize, String order);

        /**
         * 根据数据库id查询相关列信息
         *
         * @param databaseId
         */
        List<TableDetailInfo> getByDatabaseId(Integer databaseId);

        /**
         * 根据数据库id和数据表id查询数据表详细信息
         *
         * @param databaseId
         * @param tableId
         * @return
         */
        TableDetailInfo getByDatabaseIdAndTableId(Integer databaseId, Integer tableId);


        /**
         * 添加数据
         *
         * @param model
         * @return ResultVo<TableDetailInfo>
         */
        ResultVo<TableDetailInfo> insert(ColumnVo model);


        /**
         * 修改数据
         *
         * @param model
         * @return TableDetailInfo
         */
        TableDetailInfo update(ColumnVo model);

        /**
         * 删除数据
         *
         * @param id
         * @return SuccessCount
         */
        SuccessCount delete(String id);


        /**
         * 根据数据表id删除相应的属性信息
         *
         * @param tableId
         */
        boolean removeByTableId(Integer tableId);
}


