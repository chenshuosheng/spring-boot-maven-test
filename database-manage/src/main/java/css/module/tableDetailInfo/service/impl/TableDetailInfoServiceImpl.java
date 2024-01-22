package css.module.tableDetailInfo.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import css.common.dto.RPage;
import css.common.util.Filter2QueryWrapperUtil;
import css.common.util.QueryWrapperOrderUtil;
import css.common.vo.ListResult;
import css.common.vo.ResultVo;
import css.common.vo.SuccessCount;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.databaseInfo.service.DatabaseInfoService;
import css.module.tableDetailInfo.entity.TableDetailInfo;
import css.module.tableDetailInfo.mapper.TableDetailInfoMapper;
import css.module.tableDetailInfo.service.TableDetailInfoService;
import css.module.tableDetailInfo.vo.ColumnVo;
import css.module.tableInfo.entity.TableInfo;
import css.module.tableInfo.service.TableInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CSS
 * @since 2023-11-25
 */

@Service
public class TableDetailInfoServiceImpl extends ServiceImpl<TableDetailInfoMapper, TableDetailInfo> implements TableDetailInfoService {

    @Autowired
    private DatabaseInfoService databaseInfoService;

    @Autowired
    private TableInfoService tableInfoService;

    @Override
    public ListResult<TableDetailInfo> list(String filter, String order) {
        QueryWrapper<TableDetailInfo> queryWrapper = Filter2QueryWrapperUtil.instance.handleFilter(filter, TableDetailInfo.COLUMN_MAP);

        QueryWrapperOrderUtil.instance.mySqlSingleOrder(queryWrapper, order, TableDetailInfo.COLUMN_MAP);
        List<TableDetailInfo> list = super.list(queryWrapper);
        return new ListResult<>(list.size(), list);
    }

    @Override
    public ListResult<TableDetailInfo> pageList(String filter, Integer pageNum, Integer pageSize, String order) {
        QueryWrapper<TableDetailInfo> queryWrapper = Filter2QueryWrapperUtil.instance.handleFilter(filter, TableDetailInfo.COLUMN_MAP);

        QueryWrapperOrderUtil.instance.mySqlSingleOrder(queryWrapper, order, TableDetailInfo.COLUMN_MAP);
        Page<TableDetailInfo> page = this.page(new RPage<>(pageNum, pageSize), queryWrapper);
        return new ListResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public TableDetailInfo getByDatabaseIdAndTableId(Integer databaseId, Integer tableId){
        LambdaQueryWrapper<TableDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableDetailInfo::getDatabaseId,databaseId);
        wrapper.eq(TableDetailInfo::getTableId,tableId);
        return getOne(wrapper);
    }


    @Override
    public ResultVo<TableDetailInfo> insert(ColumnVo model) {

        Integer databaseId = model.getDatabaseId();
        Integer tableId = model.getTableId();

        if(databaseId==null || tableId == null){
            return new ResultVo<>(false,"请输入正确的数据库和数据表信息！",null);
        }

        DatabaseInfo databaseInfo = databaseInfoService.getById(databaseId);
        if(databaseInfo==null){
            return new ResultVo<>(false,"数据库不存在，请输入正确的数据库信息！",null);
        }

        LambdaQueryWrapper<TableInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableInfo::getDatabaseId,databaseId);
        queryWrapper.eq(TableInfo::getId,tableId);
        TableInfo tableInfo = tableInfoService.getOne(queryWrapper);
        if(tableInfo == null){
            return new ResultVo<>(false,"数据表不存在或所属数据库信息有误，请重新输入！",null);
        }

        TableDetailInfo old = getByDatabaseIdAndTableId(databaseId, tableId);
        if(old!=null){
            return new ResultVo<>(false,"该表已经存在！若希望修改数据表的列元素信息，请使用更新接口进行修改！",null);
        }

        TableDetailInfo tableDetailInfo = new TableDetailInfo();
        BeanUtils.copyProperties(model, tableDetailInfo);
        tableDetailInfo.setDatabaseName(databaseInfo.getDatabaseName());
        tableDetailInfo.setTableName(tableInfo.getTableName());

        this.save(tableDetailInfo);
        TableDetailInfo save = getByDatabaseIdAndTableId(databaseId, tableId);
        if(save != null){
            return new ResultVo<>(true,save);
        }
        return new ResultVo<>(false,"新增失败，请重试！",null);
    }

    @Override
    public TableDetailInfo update(ColumnVo model) {
        Integer id = model.getId();
        TableDetailInfo oldDetailInfo = this.getById(id);
        TableDetailInfo newDetailInfo = new TableDetailInfo();

        //若所归属的数据库和数据表不给修改
        if(oldDetailInfo.getTableId().intValue() != model.getTableId().intValue() || oldDetailInfo.getDatabaseId().intValue() != model.getDatabaseId().intValue()){
            return null;
        }

        BeanUtils.copyProperties(model,newDetailInfo);
        newDetailInfo.setDatabaseName(oldDetailInfo.getDatabaseName());
        newDetailInfo.setTableName(oldDetailInfo.getTableName());

        boolean success = this.updateById(newDetailInfo);
        if(success)
            return newDetailInfo;
        return null;
    }

    @Override
    public SuccessCount delete(String id) {
        LambdaQueryWrapper<TableDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableDetailInfo::getId, id);
        TableDetailInfo model = this.getOne(wrapper);
        if (model == null) {
            return new SuccessCount(0);
        }
        return new SuccessCount(this.removeById(id) ? 1 : 0);
    }

    @Override
    public boolean removeByTableId(Integer tableId) {
        LambdaQueryWrapper<TableDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableDetailInfo::getId,tableId);
        return remove(wrapper);
    }


    @Override
    public List<TableDetailInfo> getByDatabaseId(Integer databaseId) {
        LambdaQueryWrapper<TableDetailInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableDetailInfo::getDatabaseId,databaseId);
        return this.list(wrapper);
    }

}


