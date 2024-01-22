package css.common.util.sql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import css.common.util.MyStringUtils;
import css.module.intelligentManagetable.entity.ColumnProperty;

import java.lang.reflect.Field;

/**
 * @Description: SQL构造器
 * @Author: CSS
 * @Date: 2023/11/25 22:17
 */


public class SQLGenerator {

    public static StringBuilder analysisColumnToSQL(String databaseName, StringBuilder sb, ColumnProperty column) {
        String columnName = column.getColumnName();
        String columnType = column.getColumnType();
        String defaultValue = column.getDefaultValue();
        String comment = column.getComment();
        boolean canNull = column.isCanNull();
        boolean autoINC = column.isAutoINC();
        boolean unique = column.isUnique();
        String keyType = column.getKeyType();

        sb.append(columnName).append(" ")
                .append(columnType).append(" ");

        //有默认值
        if (MyStringUtils.isNotEmpty(defaultValue)) {
            sb.append("default ").append(defaultValue).append(" ");
        }

        //是否可为空
        if (canNull && MyStringUtils.isEmpty(keyType)) {
            sb.append("null").append(" ");
        } else {
            sb.append("not null").append(" ");
        }

        //备注
        if (MyStringUtils.isNotEmpty(comment))
            sb.append("comment '").append(comment).append("' ");

        //整数自增(只有是key、整数的列可以自增)
        if (autoINC && MyStringUtils.isNotEmpty(keyType) && "int".equals(columnType))
            sb.append("auto_increment").append(" ");

        //默认为主键
        if (MyStringUtils.isNotEmpty(keyType))
            sb.append("primary key").append(",");
        else
            sb.append(",");

        //唯一
        if (unique)
            sb.append("constraint ").append(databaseName)
                    .append("_").append(columnName)
                    .append("_uindex unique (").append(columnName).append("),");

        return sb;
    }



    //TODO: 根据pojo类生成数据表
    public static String analysisAnnotationToSQL(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        //获取类上的@TableName注解，得到数据表名
        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (tableNameAnnotation != null){
            sb.append("CREATE TABLE ")
                    .append(tableNameAnnotation.value())
                    .append("(");
        }

        //遍历类的字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //获取字段上的@TableField注解，获取属性名
            TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
            if(tableFieldAnnotation!=null){
                sb.append(tableFieldAnnotation.value())
                        .append(" ");
            }else{
                //直接使用属性名可能存在驼峰<-->'_'问题
                sb.append(field.getName());
            }

            TableId tableIdAnnotation = field.getAnnotation(TableId.class);
            if(tableIdAnnotation!=null){
                sb.append("PRIMARY KEY ")
                        .append("(")
                        .append(tableIdAnnotation.value())
                        .append(")");
            }

        }
        return "";
    }
}
