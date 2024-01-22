package css.common.util.sql;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ng_Chun-fai
 * @date 2021年11月30日 16:27
 */
public enum OrderCheckUtil {
    /**
     *
     */
    instance;
    public static final String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";
    private boolean checkOrderType(String type) {
        type = type.toLowerCase();
        return "asc".equals(type) || "desc".equals(type);
    }

    /**
     * true为未检验出异常(仅校验)
     * @param classToCheck
     * @param order
     * @return
     */
    public boolean checkOrder(Class classToCheck, String order) {
        Field[] declaredFields = classToCheck.getDeclaredFields();
        List<String> collect = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toList());
        String[] split = order.split(",");
        for (String str : split) {
            //split默认会丢弃末尾空值，但是开头的不会，还是要trim
            str = str.trim();
            String[] splitOne = str.split("\\s+");
            if (splitOne.length > 2 || splitOne.length == 0) {
                return false;
            }
            if (!(collect.contains(splitOne[0]) && (splitOne.length == 1 || checkOrderType(splitOne[1])))) {
                return false;
            }
        }
        return true;
    }

    /**
     * true为未检验出异常(用orderList拿到分割后的order)
     * @param classToCheck
     * @param order
     * @return
     */
    public boolean checkOrder(Class classToCheck, String order, List<String[]> orderList) {
        Field[] declaredFields = classToCheck.getDeclaredFields();
        List<String> collect = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toList());
        String[] split = order.split(",");
        for (String str : split) {
            //split默认会丢弃末尾空值，但是开头的不会，还是要trim
            str = str.trim();
            String[] splitOne = str.split("\\s+");
            if (splitOne.length > 2 || splitOne.length == 0) {
                return false;
            }
            if (!(collect.contains(splitOne[0]) && (splitOne.length == 1 || checkOrderType(splitOne[1])))) {
                return false;
            }
            orderList.add(splitOne);
        }
        return true;
    }

    /**
     * true为未检验出异常(用orderList拿到分割后的order)
     * @param columnSet
     * @param order
     * @param orderList
     * @return
     */
    public boolean checkOrder(Set<String> columnSet, String order, List<String[]> orderList) {
        String[] split = order.split(",");
        for (String str : split) {
            //split默认会丢弃末尾空值，但是开头的不会，还是要trim
            str = str.trim();
            String[] splitOne = str.split("\\s+");
            if (splitOne.length > 2 || splitOne.length == 0) {
                return false;
            }
            if (!(columnSet.contains(splitOne[0]) && (splitOne.length == 1 || checkOrderType(splitOne[1])))) {
                return false;
            }
            orderList.add(splitOne);
        }
        return true;
    }

    /**
     * true为未检验出异常(用orderList拿到分割后的order)
     * @param columnSet
     * @param order
     * @return
     */
    public boolean checkOrder(Set<String> columnSet, String order) {
        String[] split = order.split(",");
        for (String str : split) {
            //split默认会丢弃末尾空值，但是开头的不会，还是要trim
            str = str.trim();
            String[] splitOne = str.split("\\s+");
            if (splitOne.length > 2 || splitOne.length == 0) {
                return false;
            }
            if (!(columnSet.contains(splitOne[0]) && (splitOne.length == 1 || checkOrderType(splitOne[1])))) {
                return false;
            }
        }
        return true;
    }
    public  void filterKeyword(String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new RuntimeException("参数存在SQL注入风险");
            }
        }
    }
    /**
     * true为未检验出异常
     * @param aliasClassMap
     * @param order
     * @return
     */
    public boolean checkOrderWithUnionForm(Map<String, Class> aliasClassMap, String order) {
        HashMap<String, List<String>> aliasFieldsMap = new HashMap<>();
        for (Map.Entry<String, Class> entry : aliasClassMap.entrySet()) {
            aliasFieldsMap.put(entry.getKey(), Arrays.stream(entry.getValue().getDeclaredFields()).map(Field::getName).collect(Collectors.toList()));
        }
        String[] split = order.split(",");
        for (String str : split) {
            //split默认会丢弃末尾空值，但是开头的不会，还是要trim
            str = str.trim();
            String[] splitOne = str.split("\\s+");
            if (splitOne.length > 2 || splitOne.length == 0) {
                return false;
            }
            String[] column = splitOne[0].split("\\.");
            if (column.length != 2){
                return false;
            }
            try{
                if (!(aliasFieldsMap.get(column[0]).contains(column[1]) && (splitOne.length == 1 || checkOrderType(splitOne[1])))) {
                    return false;
                }
            }catch (NullPointerException e){
                return false;
            }
        }
        return true;
    }
}
