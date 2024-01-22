package css.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import css.common.dto.SqliteParameter;
import css.common.exception.FilterHandleException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Ng_Chun-fai
 * @date 2021年12月30日 17:28
 */
public enum Filter2QueryWrapperUtil {
    /**
     * filter转QueryWrapper
     */
    instance;

    private static final String LEGAL_COLUMN_NAME = "[a-zA-Z]+";

    private static final String AND = "&&";
    private static final String OR = "||";
    private static final String LEFT = "(";
    private static final String RIGHT = ")";
    private static final String EQUAL_TO = "==";
    private static final String NOT_EQUAL_TO = "!=";
    /**
     * 百分号在左
     */
    private static final String LIKE_LEFT = "likeLeft";
    private static final String LIKE = "like";
    private static final String NOT_LIKE = "notLike";
    private static final String LIKE_RIGHT = "likeRight";
    private static final String IN = "in";
    private static final String LESS_THAN = "<";
    private static final String LESS_THAN_OR_EQUAL_TO = "<=";
    private static final String GREATER_THAN = ">";
    private static final String GREATER_THAN_OR_EQUAL_TO = ">=";
    private static final String NOT = "!";
//    private static final String JSON_EXTRACT = "json_extract";

    private static final String NULL = "null";
    private static final String TRUE = "true";
    private static final String FALSE = "false";


    public <T> QueryWrapper<T> handleFilter(String filter, Map<String, String> columnMap) {
        return handleFilter(filter, columnMap, null);
    }

    public <T> QueryWrapper<T> handleFilter(String filter, Map<String, String> columnMap, LinkedList<SqliteParameter> sqliteParameters) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(filter)) {
            String[] split = filterParser(filter);

            //因为在createQueryWrapper方法中，右括号是直接return的，只会生成奇怪的sql，无法对右括号进行完整的检测，所以在此进行括号检测
            if (checkBracketsIsNotMatch(split)) {
                throw new FilterHandleException("filter括号不匹配");
            }

            try {
                createQueryWrapper(split, queryWrapper, 0, columnMap, sqliteParameters);
            } catch (FilterHandleException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new FilterHandleException(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "filter解析失败，请联系管理员");
            }
        }

        return queryWrapper;
    }

    /**
     * 检测左右括号是否匹配
     * 1.数量是否对得上
     * 2.右括号是否在左括号右边
     *
     * @param array 待匹配数组
     * @return 匹配不上返回false
     */
    private boolean checkBracketsIsNotMatch(String[] array) {
        int numberOfBrackets = 0;
        for (String cur : array) {
            if (LEFT.equals(cur)) {
                numberOfBrackets++;
            } else if (RIGHT.equals(cur) && --numberOfBrackets < 0) {
                return true;
            }
        }
        return numberOfBrackets != 0;
    }

    @SuppressWarnings("AlibabaMethodTooLong")
    private <T> int createQueryWrapper(String[] split, QueryWrapper<T> queryWrapper, int cur, Map<String, String> columnMap, LinkedList<SqliteParameter> sqliteParameters) {
        int i = cur;
        boolean isSqlite = sqliteParameters != null;
        for (; i < split.length; i++) {
            if (RIGHT.equals(split[i])) {
                return i;
            }
            if (NOT.equals(split[i])) {
                if (LEFT.equals(split[i + 1])) {
                    //lambda表达式只允许使用final类型变量，所以new一个数组来传参及接收结果
                    int[] newI = new int[1];
                    newI[0] = i + 2;
                    queryWrapper.not(wrapper -> newI[0] = createQueryWrapper(split, wrapper, newI[0], columnMap, sqliteParameters));
                    i = newI[0];
                } else {
                    throw new FilterHandleException("! 后必须跟左括号");
                }
            } else if (LEFT.equals(split[i])) {
                int[] newI = new int[1];
                newI[0] = i + 1;
                queryWrapper.and(wrapper -> newI[0] = createQueryWrapper(split, wrapper, newI[0], columnMap, sqliteParameters));
                i = newI[0];
            } else if (AND.equals(split[i])) {
                //判断是否要加括号，如果右一位不是左括号，不用处理，因为默认就是and
                if (LEFT.equals(split[i + 1])) {
                    int[] newI = new int[1];
                    newI[0] = i + 2;
                    queryWrapper.and(wrapper -> newI[0] = createQueryWrapper(split, wrapper, newI[0], columnMap, sqliteParameters));
                    i = newI[0];
                }
            } else if (OR.equals(split[i])) {
                //判断是否要加括号，如果右一位不是左括号，加个or
                if (LEFT.equals(split[i + 1])) {
                    int[] newI = new int[1];
                    newI[0] = i + 2;
                    queryWrapper.or(wrapper -> newI[0] = createQueryWrapper(split, wrapper, newI[0], columnMap, sqliteParameters));
                    i = newI[0];
                } else {
                    queryWrapper.or();
                }
            } else {
                if (EQUAL_TO.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.eq(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else if (NULL.equals(value)) {
                        queryWrapper.isNull(getColumnName(columnMap, key));
                    } else if (TRUE.equals(value)) {
                        queryWrapper.eq(getColumnName(columnMap, key), true);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("true", "Boolean"));
                        }
                    } else if (FALSE.equals(value)) {
                        queryWrapper.eq(getColumnName(columnMap, key), false);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("false", "Boolean"));
                        }
                    } else {
                        queryWrapper.eq(getColumnName(columnMap, key), Long.parseLong(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                } else if (NOT_EQUAL_TO.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.ne(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else if (NULL.equals(value)) {
                        queryWrapper.isNotNull(getColumnName(columnMap, key));
                    } else if (TRUE.equals(value)) {
                        queryWrapper.ne(getColumnName(columnMap, key), true);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("true", "Boolean"));
                        }
                    } else if (FALSE.equals(value)) {
                        queryWrapper.ne(getColumnName(columnMap, key), false);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("false", "Boolean"));
                        }
                    } else {
                        queryWrapper.ne(getColumnName(columnMap, key), Long.parseLong(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                } else if (LIKE_LEFT.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.likeLeft(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("%" + value, "String"));
                        }
                    }
                } else if (LIKE.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.like(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("%" + value + "%", "String"));
                        }
                    }
                } else if (LIKE_RIGHT.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.likeRight(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value + "%", "String"));
                        }
                    }
                } else if (NOT_LIKE.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.notLike(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter("%" + value + "%", "String"));
                        }
                    }
                } else if (IN.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        String[] splitIn = value.split(",");
                        if (isSqlite) {
                            for (String s : splitIn) {
                                sqliteParameters.add(new SqliteParameter(s, "String"));
                            }
                        }
                        List<String> list = Arrays.asList(splitIn);
                        queryWrapper.in(getColumnName(columnMap, key), list);
                    } else {
                        String[] splitIn = value.split(",");
                        ArrayList<Long> longs = new ArrayList<>();
                        for (String s : splitIn) {
                            longs.add(Long.valueOf(s));
                            if (isSqlite) {
                                sqliteParameters.add(new SqliteParameter(s, "Long"));
                            }
                        }
                        queryWrapper.in(getColumnName(columnMap, key), longs);
                    }
                } else if (LESS_THAN.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.lt(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else {
                        queryWrapper.lt(getColumnName(columnMap, key), Long.valueOf(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                } else if (GREATER_THAN.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.gt(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else {
                        queryWrapper.gt(getColumnName(columnMap, key), Long.valueOf(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                } else if (LESS_THAN_OR_EQUAL_TO.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.le(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else {
                        queryWrapper.le(getColumnName(columnMap, key), Long.valueOf(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                } else if (GREATER_THAN_OR_EQUAL_TO.equals(split[i])) {
                    String key = split[i - 1];
                    String value = split[i + 1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                        queryWrapper.ge(getColumnName(columnMap, key), value);
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "String"));
                        }
                    } else {
                        queryWrapper.ge(getColumnName(columnMap, key), Long.valueOf(value));
                        if (isSqlite) {
                            sqliteParameters.add(new SqliteParameter(value, "Long"));
                        }
                    }
                }
//                else if (JSON_EXTRACT.equals(split[i])) {
//                    //这玩意就是为了兼容 project 的 acceptedEntrustedAgency 字段
//                    String key = split[i - 1];
//                    if (!"acceptedEntrustedAgency".equals(key)){
//                        throw new FilterHandleException("json_extract操作符目前仅支持acceptedEntrustedAgency字段");
//                    }
//                    String value = split[i + 1];
//                    value = value.substring(1, value.length() - 1);
//
//                    StringBuilder sb = new StringBuilder();
//                    String[] valueSplit = value.split("&&");
//                    Object[] objects = new Object[valueSplit.length * 2];
//                    for (int j = 0; j < valueSplit.length; j++) {
//                        if (j != 0) {
//                            sb.append("and ");
//                        }
//                        String[] curCondition = valueSplit[j].split("=");
//                        if (curCondition.length != 2) {
//                            throw new FilterHandleException(JSON_EXTRACT + "处理失败，请检查参数");
//                        }
//                        sb.append("json_extract(j.value, '$.");
//                        if (!isLegalColumnName(curCondition[0])) {
//                            throw new FilterHandleException(JSON_EXTRACT + "的json字段名只能包含大小写英文字母");
//                        }
//                        sb.append(curCondition[0]);
//                        sb.append("') = {1} ");
//                        boolean isNumber = isNumber(curCondition[1]);
////                        objects[j] = isNumber ? Long.parseLong(curCondition[1]) : curCondition[1];
////                        if (isSqlite) {
////                            sqliteParameters.add(new SqliteParameter(curCondition[1], isNumber ? "Long" : "String"));
////                        }
//                        objects[j] = curCondition[1];
//                        if (isSqlite) {
//                            sqliteParameters.add(new SqliteParameter(curCondition[1],"String"));
//                        }
//                    }
//
//                    queryWrapper.apply(sb.toString(), objects);
//                }
            }
        }
        //已达到最大长度，减不减1都一样
        return split.length;
    }

    /**
     * 分离filter为数组
     * 1.将filter分解成[分段1, 字符串1, 分段2, 字符串2, 分段3, 字符串3, 分段4, 字符串4...]
     * 2.将分段按照空格分解
     *
     * @param filter String
     * @return String[]
     */
    private String[] filterParser(String filter) {
        char backSlash = '\\';
        List<String> list = new ArrayList<>();
        int left = 0;
        boolean isString = false;
        char[] charArray = filter.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            //先找出字符串
            if (charArray[i] == '"') {
                int j = i;
                int count = 0;
                while (j > 0 && charArray[--j] == backSlash) {
                    count++;
                }
                if ((count & 1) == 0) {
                    if (isString) {
                        String strValue = filter.substring(left, ++i);
                        list.add(strValue);
                    } else {
                        String substring = filter.substring(left, i);
                        String[] split = substring.trim().split("\\s+");
                        list.addAll(Arrays.asList(split));
                    }
                    isString = !isString;
                    left = i;
                }
            }
        }
        if (left != length) {
            String end = filter.substring(left, length);
            String[] split = end.trim().split("\\s+");
            list.addAll(Arrays.asList(split));
        }
        return list.toArray(new String[0]);
    }

    private String getColumnName(Map<String, String> columnMap, String key) {
        String value = columnMap.get(key);
        if (value == null) {
            throw new FilterHandleException("字段不存在：" + key);
        }
        return value;
    }

    private boolean isNumber(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isLegalColumnName(String columnName){
        return Pattern.matches(LEGAL_COLUMN_NAME, columnName);
    }
}
