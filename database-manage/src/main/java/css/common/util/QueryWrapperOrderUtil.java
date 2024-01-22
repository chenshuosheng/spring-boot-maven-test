package css.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import css.common.consts.SqlKeyword;
import css.common.enums.DatabaseType;
import css.common.exception.OrderIlleagalException;
import css.common.util.sql.OrderCheckUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Ng_Chun-fai
 * @date 2021年12月10日 15:59
 */
public enum QueryWrapperOrderUtil {
    /**
     *
     */
    instance;

    public <T> void sqlServerSingleOrder(QueryWrapper<T> wrapper, String order, Map<String, String> columnMap){
        singleOrder(wrapper, order, columnMap, DatabaseType.SQLSERVER);
    }

    public <T> void mySqlSingleOrder(QueryWrapper<T> wrapper, String order, Map<String, String> columnMap){
        singleOrder(wrapper, order, columnMap, DatabaseType.MYSQL);
    }

    public <T> void sqliteSingleOrder(QueryWrapper<T> wrapper, String order, Map<String, String> columnMap){
        singleOrder(wrapper, order, columnMap, DatabaseType.SQLITE);
    }

    private <T> void singleOrder(QueryWrapper<T> wrapper, String order, Map<String, String> columnMap, DatabaseType databaseType){
        if (StringUtils.isNotBlank(order)) {
            LinkedList<String[]> orderList = new LinkedList<>();
            if (!OrderCheckUtil.instance.checkOrder(columnMap.keySet(), order, orderList)) {
                throw new OrderIlleagalException("order 非法");
            }
            for (String[] curOrder : orderList) {
                //替换为数据库真实字段
                curOrder[0] = columnMap.get(curOrder[0]);
                switch (databaseType) {
                    case SQLSERVER:
                        //如果是关键字就加以[]包围
                        if (SqlKeyword.SQL_SERVER_KEYWORDS.contains(curOrder[0].toLowerCase())){
                            curOrder[0] = "[" + curOrder[0] + "]";
                        }
                        break;
                    case MYSQL:
                        //如果是关键字就加以``包围
                        if (SqlKeyword.MYSQL_KEYWORDS.contains(curOrder[0].toLowerCase())){
                            curOrder[0] = "`" + curOrder[0] + "`";
                        }
                        break;
                    case SQLITE:
                        //如果是关键字就加以``包围
                        if (SqlKeyword.SQLITE_KEYWORDS.contains(curOrder[0].toLowerCase())){
                            curOrder[0] = "`" + curOrder[0] + "`";
                        }
                        break;
                    default:
                        break;
                }
                wrapper.orderBy(true, curOrder.length == 1 || "asc".equals(curOrder[1]), curOrder[0]);
            }
        }
    }
}
