package css.common.util.sql;

import css.common.util.CommonUtil;
import css.common.util.MyStringUtils;
import css.common.vo.ResultVo;
import css.module.databaseInfo.dto.DatabaseInfoDto;
import css.module.databaseInfo.entity.DatabaseInfo;
import css.module.tableDetailInfo.vo.ColumnVo;
import css.module.tableInfo.dto.TablesInfoDto;
import css.module.tableInfo.entity.TableInfo;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

/**
 * @Description: 读取指定数据库信息
 * @Author: CSS
 * @Date: 2023/11/24 9:17
 */

@Component
public class ExecuteSQL {

    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";

    private static final String baseUrl = "jdbc:mysql://localhost:3306/";

    private static final String userName = "root";

    private static final String password = "123456";

    public static DatabaseInfoDto getDatabaseInfo() throws SQLException {

        List<String> databaseNames = new ArrayList<>();
        HashMap<String, TablesInfoDto> map = new HashMap<>();

        Connection conn = null;

        try {
            Class.forName(driverClassName);

            conn = DriverManager.getConnection(baseUrl, userName, password);

//            Statement statement = conn.createStatement();

            DatabaseMetaData metaData = conn.getMetaData();

            //获取所有数据库名
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                String databaseName = catalogs.getString("TABLE_CAT");
                if (databaseName.endsWith("_schema") || "sys".equals(databaseName) || "mysql".equals(databaseName))
                    continue;
                databaseNames.add(databaseName);
            }

            //获取指定数据库下所有数据表名
            for (String databaseName : databaseNames) {

                List<String> tableNames = new ArrayList<>();
                ResultSet tables = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }

                TablesInfoDto tablesInfoDto = new TablesInfoDto(tableNames.size(), tableNames);
                map.put(databaseName, tablesInfoDto);
            }
            return new DatabaseInfoDto(databaseNames.size(), map);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return null;
    }


    public static ColumnVo getProperties(DatabaseInfo databaseInfo, TableInfo tableInfo) throws SQLException {

        Integer databaseId = databaseInfo.getId();
        String databaseName = databaseInfo.getDatabaseName();

        Integer tableId = tableInfo.getId();
        String tableName = tableInfo.getTableName();

        Connection conn = null;

        try {
            Class.forName(driverClassName);

            conn = DriverManager.getConnection(baseUrl + databaseName, userName, password);

            Statement statement = conn.createStatement();

            String sql = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = '" + databaseName + "' AND TABLE_NAME = '" + tableName + "'";

            ResultSet rs = statement.executeQuery(sql);
            StringJoiner columnNames = new StringJoiner(",");
            StringJoiner dataTypes = new StringJoiner(",");
            StringJoiner canNulls = new StringJoiner(",");
            StringJoiner columnKeys = new StringJoiner(",");
            ColumnVo columnVo = new ColumnVo();
            int column_num = 0;

            while (rs.next()) {
                column_num++;
                String column_name = rs.getString("COLUMN_NAME");
                columnNames.add(column_name);
                dataTypes.add(rs.getString("DATA_TYPE"));
                canNulls.add(rs.getString("IS_NULLABLE"));
                String column_key = rs.getString("COLUMN_KEY");
                columnKeys.add(column_key);
                if ("PRI".equals(column_key)) {
                    String primaryKey = columnVo.getPrimaryKey();
                    if (primaryKey == null) {
                        columnVo.setPrimaryKey(column_name);
                    } else {
                        columnVo.setPrimaryKey(primaryKey + "," + column_name);
                    }
                }
            }

            columnVo.setDatabaseId(databaseId);
            columnVo.setTableId(tableId);
            columnVo.setColumnsName(columnNames.toString());
            columnVo.setColumnsType(dataTypes.toString());
            columnVo.setColumnCanNull(canNulls.toString());
            columnVo.setAllKey(columnKeys.toString());
            tableInfo.setColumnNum(column_num);

            return columnVo;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return null;
    }


    /**
     * 根据sql删除数据表
     *
     * @param databaseName
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static ResultVo<String> deleteTable(String databaseName, String tableName) throws SQLException {

        if (MyStringUtils.isEmpty(databaseName))
            return new ResultVo<>(false, "请输入数据库名");
        if (MyStringUtils.isEmpty(tableName))
            return new ResultVo<>(false, "请输入数据表名");

        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS `")
                .append(tableName)
                .append("`;");

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(driverClassName);

            //连接数据库
            conn = DriverManager.getConnection(baseUrl + databaseName, userName, password);

            //执行SQL语句
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());

            return new ResultVo<>(true, "成功删除数据表：" + databaseName + "." + tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>(false,e.toString(),null);
        } finally {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }


    /**
     * 根据sql创建新数据表(含删除旧表)
     *
     * @param databaseName
     * @param tableName
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ResultVo<String> createTable(String databaseName, String tableName, String sql) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            //加载驱动
            Class.forName(driverClassName);
            //连接数据库
            conn = DriverManager.getConnection(baseUrl + databaseName, userName, password);


            //删除旧表
            ResultVo<String> vo = deleteTable(databaseName, tableName);
            if (!vo.isSuccess())
                return vo;

            //创建新表
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return new ResultVo<>(true, "新建数据表成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo<>(false, e.toString());
        } finally {
            //关闭数据库连接
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }


    /**
     * 导出指定数据表的sql文件到指定路径下
     *
     * @param databaseName
     * @param tableName
     * @param outputPath
     * @throws SQLException
     * @throws IOException
     */
    public static void exportTable(String databaseName, String tableName, String outputPath) throws SQLException, IOException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                baseUrl + databaseName,
                userName,
                password
        );

        // 创建输出文件
        FileWriter writer = new FileWriter(outputPath);
        Statement stmt = null;
        try {

            ResultSet rs = null;

            //查询表结构
            try {
                String queryTableSql = "SHOW CREATE TABLE " + tableName;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryTableSql);
                if (rs.next()) {
                    String createTableSql = rs.getString(2);
                    writer.write(createTableSql + ";\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }

            //查询表数据
            try {
                String queryDataSql = "SELECT * FROM " + tableName;
                rs = stmt.executeQuery(queryDataSql);
                //读取每一个记录
                while (rs.next()) {
                    StringBuilder rowData = new StringBuilder();
                    int columnCount = rs.getMetaData().getColumnCount();
                    //每一个元素
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.append(rs.getString(i));
                        if (i < columnCount) {
                            rowData.append(",");
                        }
                    }
                    writer.write("INSERT INTO " + tableName + " VALUES (" + rowData.toString() + ");\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (writer != null)
                writer.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }


    /**
     * 导出指定数据表的sql文件到指定路径下
     *
     * @param databaseName
     * @param tableName
     * @throws SQLException
     * @throws IOException
     */
    public static String getTableSql(String databaseName, String tableName) throws SQLException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                baseUrl + databaseName,
                userName,
                password
        );

        // 创建输出文件
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        try {

            ResultSet rs = null;

            //查询表结构
            try {
                String queryTableSql = "SHOW CREATE TABLE " + tableName;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryTableSql);
                if (rs.next()) {
                    String createTableSql = rs.getString(2);
                    sb.append(createTableSql);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }

            //查询表数据
            try {
                String queryDataSql = "SELECT * FROM " + tableName;
                rs = stmt.executeQuery(queryDataSql);
                //读取每一个记录
                while (rs.next()) {
                    StringBuilder rowData = new StringBuilder();
                    int columnCount = rs.getMetaData().getColumnCount();
                    //每一个元素
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.append(rs.getString(i));
                        if (i < columnCount) {
                            rowData.append(",");
                        }
                    }
                    sb.append("INSERT INTO " + tableName + " VALUES (" + rowData.toString() + ");\n");
                }

                return sb.toString();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return null;
    }


    public static void main(String[] args) {
        try {
            // 导出数据表为 SQL 文件
            String databaseName = CommonUtil.scanner("数据库名");
            String tableName = CommonUtil.scanner("数据表每");
            String outputPath = "F:\\personalItem\\myproject\\databasemanage\\src\\main\\resources\\output.sql";
            exportTable(databaseName, tableName, outputPath);
            System.out.println("导出成功！");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
