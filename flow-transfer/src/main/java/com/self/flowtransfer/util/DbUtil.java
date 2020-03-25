package com.self.flowtransfer.util;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 数据源工具类
 */
public class DbUtil {

    /**
     * 通过jdbc驱动名来获取数据库方言
     * @param driverUrl 数据源url
     * @return 数据库方言
     */
    public static Dialect getDbDialectByDriver(String driverUrl) {
        Dialect dialect = null;
        if (StringUtil.isEmpty(driverUrl)) {
            return dialect;
        }
        if (driverUrl.toLowerCase().indexOf("mysql") > -1) {
            dialect = new MysqlDialect();
        } else if (driverUrl.toLowerCase().indexOf("sqlserver") > -1) {
            dialect = new SqlServerDialect();
        } else if (driverUrl.toLowerCase().indexOf("oracle") > -1) {
            dialect = new OracleDialect();
        }

        return dialect;
    }

    /**
     * int[]数组求和
     */
    public static int sumResultCount(int[] result) {
        int resultSum = 0;
        for (int temp : result) {
            resultSum += temp;
        }

        return resultSum;
    }

    /**
     * int[]数组求和
     */
    public static int sumResultCount(Integer[] result) {
        int resultSum = 0;
        for (int temp : result) {
            resultSum += temp;
        }

        return resultSum;
    }

    public static int[] wrapList(List<Integer> array) {
        int[] num = new int[array.size()];
        for (int i = 0; i < array.size(); i++) {
            num[i] = array.get(i);
        }

        return num;
    }

    /**
     * 从record对象转化为插入sql语句
     * @param tableName 表名
     * @param records 数据对象
     * @param batchCount 批量大小
     * @param getDbCurrentDateFields 需要获取数据库当前时间的字段，格式如:[createTime,insertTime]
     */
    public static List<StringBuffer> getInsertSqlFromRecord(String tableName, List<Record> records, int batchCount, String[] getDbCurrentDateFields) {
        List result = new ArrayList();
        StringBuffer insert = new StringBuffer().append("insert into `").append(tableName).append("`("),
                sql = new StringBuffer().append(insert);
        for (int i = 0, count = 1; i < records.size(); i++, count++) {
            StringBuffer value = new StringBuffer();
            Record record = records.get(i);
            //判断是否大于最大批量处理数
            if (count > batchCount) {
                count = 1;

                //存储上一个sql
                result.add(sql);

                //重置sql语句
                sql = new StringBuffer().append(insert);
            }
            //遍历每个字段，拼接sql语句
            Iterator iterator = record.getColumns().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry e = (Map.Entry) iterator.next();
                //表示不是第一个字段了
                if (value.length() > 0) {
                    if (count == 1) {
                        sql.append(", ");
                    }
                    value.append(", ");
                }
                //拼接需要插入的字段
                if (count == 1) {
                    sql.append('`').append((String) e.getKey()).append('`');
                }
                //如果是日期类型  需要格式化
                Object keyValue = e.getValue();
                if (keyValue instanceof Date) {
                    keyValue = DateUtil.formatDate((Date) keyValue, "yyyy-MM-dd HH:mm:ss");
                }
                //判断当前字段是否是获取数据库当前时间
                if (getDbCurrentDateFields.length > 0) {
                    for (String field : getDbCurrentDateFields) {
                        if (e.getKey().equals(field)) {
                            keyValue = getDbCurrentDateSql();
                            break;
                        }
                    }
                    //拼接到sql中去
                    value.append(keyValue);
                } else {
                    //将值包装为字符串
                    value.append("'").append(keyValue).append("'");
                }
            }

            //最终组装sql
            if (count == 1) {
                sql.append(") values(").append(value).append(")");
            } else {
                sql.append(",(").append(value).append(")");
            }
        }

        //如果result为空，则表示当前所有记录为超过最大提交数
        result.add(sql);

        return result;
    }

    /**
     * 从record对象转化为更新sql语句
     * @param tableName 表名
     * @param primarykeys 主键数组
     * @param records 数据对象
     * @param batchCount 批量大小
     * @param getDbCurrentDateFields 需要获取数据库当前时间的字段，格式如:[createTime,insertTime]
     */
    public static List<StringBuffer> getUpdateSqlFromRecord(String tableName, String[] primarykeys, List<Record> records, int batchCount, String[] getDbCurrentDateFields) {
        List result = new ArrayList();
        StringBuffer update = new StringBuffer().append("update `").append(tableName).append("` set ");
        StringBuffer sql = new StringBuffer();
        for (int i = 0, count = 1; i < records.size(); i++, count++) {
            StringBuffer value = new StringBuffer();
            StringBuffer primaryKeyValue = new StringBuffer();

            Record record = records.get(i);

            //判断是否大于最大批量处理数
            if (count > batchCount) {
                count = 1;
                //存储上一个sql
                result.add(sql);

                //重置一下sql
                sql = new StringBuffer();
            }

            //遍历每个字段，拼接sql语句
            Iterator iterator = record.getColumns().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry e = (Map.Entry) iterator.next();
                //表示不是第一个字段了
                if (value.length() > 0) {
                    value.append(", ");
                }

                //当前字段名
                String keyField = (String) e.getKey();

                //如果是日期类型  需要格式化
                Object keyValue = e.getValue();
                if (keyValue instanceof Date) {
                    keyValue = DateUtil.formatDate((Date) keyValue, "yyyy-MM-dd HH:mm:ss");
                }

                //判断当前字段是否是获取数据库当前时间
                if (StringUtil.isContained(getDbCurrentDateFields, keyField)) {
                    keyValue = getDbCurrentDateSql();
                } else {
                    keyValue = new StringBuffer("'").append(keyValue).append("'").toString();
                }

                //判断当前字段是否为主键
                if (StringUtil.isContained(primarykeys, keyField)) {
                    primaryKeyValue.append(keyField).append("=").append(keyValue);
                } else {
                    //将值包装为字符串
                    value.append(keyField).append("=").append(keyValue);
                }
            }

            //最终组装sql
            sql.append(update).append(value).append(" where ").append(primaryKeyValue).append(";");
        }

        //如果result为空，则表示当前所有记录未超过最大提交数
        if (StringUtil.isNotEmpty(sql)) {
            result.add(sql);
        }

        return result;
    }

    /**
     * 获取当前数据库的系统当前时间
     */
    public static Date getDbCurrentDate() {
        return Db.queryDate(getDbCurrentDateSqlStr());
    }

    /**
     * 获取不同数据的系统时间sql语句
     */
    public static String getDbCurrentDateSqlStr() {
        String sql = "";

        //获取当前的方言对象
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                sql = "SELECT SYSDATE()";
            } else if (dialect instanceof OracleDialect) {
                sql = "SELECT SYSDATE FROM dual";
            } else if (dialect instanceof SqlServerDialect) {
                sql = "SELECT GETDATE()";
            }
        }

        return sql;
    }

    /**
     * 根据数据库方言获取数据的当前时间函数
     */
    public static String getDbCurrentDateSql() {
        String sql = "";

        //获取当前的方言对象
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                sql = " SYSDATE() ";
            } else if (dialect instanceof OracleDialect) {
                sql = " sysdate ";
            } else if (dialect instanceof SqlServerDialect) {
                sql = " GETDATE() ";
            }
        }

        return sql;
    }

    /**
     * 根据数据库方言获取数据的当前时间sql语句
     * @param alias 别名
     */
    public static String getDbCurrentDateSql(String alias) {
        String sql = "";

        //获取当前的方言对象
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                sql = String.format("select SYSDATE() as %s", alias);
            } else if (dialect instanceof OracleDialect) {
                sql = String.format("select sysdate as \"%s\" from dual", alias);
            } else if (dialect instanceof SqlServerDialect) {
                sql = String.format("select GETDATE() as %s", alias);
            }
        }

        return sql;
    }

    /**
     * 获取当前的方言名称
     */
    public static String getCurrentDialectName() {
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                return "mysql";
            } else if (dialect instanceof OracleDialect) {
                return "oracle";
            } else if (dialect instanceof SqlServerDialect) {
                return "sqlserver";
            }
        }

        return null;
    }

    /**
     * 批量更新
     * @param tableName 表名
     * @param primaryKey 主键
     * @param recordList 数据集对象
     * @param batchSize 批量大小
     * @return 更新数据量
     */
    public static int batchUpdate(String tableName, String primaryKey, List<Record> recordList, int batchSize) {
        return MathUtil.sumResultCount(Db.batchUpdate(tableName, primaryKey, recordList, batchSize));
    }

    /**
     * 批量更新数据
     * @param sqlList sql列表
     * @param batchSize 批量大小
     */
    public static int batch(List<String> sqlList, int batchSize) {
        int[] resultArr = Db.batch(sqlList, batchSize);
        return MathUtil.sumResultCount(resultArr);
    }

    /**
     * 批量更新数据
     * @param sql sql
     * @param paras 更新参数
     * @param batchSize 批量大小
     * @return 更新数据量
     */
    public static int batch(String sql, Object[][] paras, int batchSize) {
        int result = 0;
        int[] resultArr = Db.batch(sql, paras, batchSize);
        result = MathUtil.sumResultCount(resultArr);
        return result;
    }

    /**
     * 补足jfinal只有二维数组参数的问题，提供list参数
     * @param sql sql
     * @param paras 参数
     * @param batchSize 批量大小
     */
    public static int batch(String sql, List<Object[]> paras, int batchSize) {
        int result = 0;
        int[] resultArr = batch(Db.use().getConfig(), sql, paras, batchSize);
        result = MathUtil.sumResultCount(resultArr);
        return result;
    }

    /**
     * 补足jfinal只有二维数组参数的问题，提供list参数
     * @param config 配置对象
     * @param sql sql
     * @param paras 参数
     * @param batchSize 批量大小
     */
    private static int[] batch(Config config, String sql, List<Object[]> paras, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, paras, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null) {
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    LogKit.error(e.getMessage(), e);
                }
            }

            config.close(conn);
        }
    }

    /**
     * 补足jfinal只有二维数组参数的问题，提供list参数
     * @param config 配置对象
     * @param conn 连接
     * @param sql sql
     * @param paras 参数对象
     * @param batchSize 批量大小
     * @throws SQLException SQLException
     */
    private static int[] batch(Config config, Connection conn, String sql, List<Object[]> paras, int batchSize) throws SQLException {
        if (paras == null || paras.size() == 0) {
            return new int[0];
        }

        if (batchSize < 1) {
            throw new IllegalArgumentException("The batchSize must more than 0.");
        }

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int[] result = new int[paras.size()];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i=0; i<paras.size(); i++) {
            for (int j=0; j<paras.get(i).length; j++) {
                Object value = paras.get(i)[j];
                if (value instanceof Date) {
                    if (value instanceof java.sql.Date) {
                        pst.setDate(j + 1, (java.sql.Date)value);
                    } else if (value instanceof java.sql.Timestamp) {
                        pst.setTimestamp(j + 1, (java.sql.Timestamp)value);
                    } else {
                        Date d = (Date)value;
                        pst.setTimestamp(j + 1, new java.sql.Timestamp(d.getTime()));
                    }
                }
                else {
                    pst.setObject(j + 1, value);
                }
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false) {
                    conn.commit();
                }
                for (int k=0; k<r.length; k++) {
                    result[pointer++] = r[k];
                }
            }
        }

        int[] r = pst.executeBatch();
        if (isInTransaction == false) {
            conn.commit();
        }

        for (int k=0; k<r.length; k++) {
            result[pointer++] = r[k];
        }

        close(pst);
        return result;
    }

    /**
     * 关闭连接
     */
    private static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }
    }

    /**
     * 根据数据库区分字段
     * @param field 字段属性
     * @return 别名字段
     */
    public static String field(String field) {
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect instanceof OracleDialect) {
            return  field + " as \"" + field + "\"";
        }

        return field;
    }

    /**
     * 获取模板中的sql语句
     * @param key 模板键
     * @return 模板sql语句
     */
    public static String getSql(String key) {
        return getSql(key, true);
    }

    /**
     * 获取模板sql语句
     * @param key 键
     * @param isExistNameSpace 是否存在命名空间
     * @return sql语句
     */
    public static String getSql(String key, boolean isExistNameSpace) {
        if (isExistNameSpace) {
            if (StringUtil.isEmpty(getCurrentDialectName())) {
                return null;
            }
            return Db.getSql(getCurrentDialectName() + "." + key);
        } else {
            return Db.getSql(key);
        }
    }

    /**
     * 根据sql模板获取sqlPara
     * @param key 键
     * @param kv 对象映射
     * @return sql参数
     */
    public static SqlPara getSqlPara(String key, Map kv) {
        return getSqlPara(key, kv, true);
    }

    /**
     * 根据sql模板获取sqlPara
     * @param key 键
     * @param kv 对象映射
     * @param isExistNameSpace 是否存在命名空间
     * @return sql参数
     */
    public static SqlPara getSqlPara(String key, Map kv, boolean isExistNameSpace) {
        if (isExistNameSpace) {
            if (StringUtil.isEmpty(getCurrentDialectName())) {
                return null;
            }
            return Db.getSqlPara(getCurrentDialectName() + "." + key, kv);
        } else {
            return Db.getSqlPara(key, kv);
        }
    }

    /**
     * 获取数据库方言时间字符串
     * @param dateTime 时间
     * @return 查询时间字符串
     */
    public static String getQueryDatetimeStr(Date dateTime) {
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                return "'" + DateUtil.formatDate(dateTime, DateUtil.format_yyyy_MM_dd_HHmmss) + "'";
            } else if (dialect instanceof OracleDialect) {
                return "to_date('" + DateUtil.formatDate(dateTime, DateUtil.format_yyyy_MM_dd_HHmmss) + "','yyyy-MM-dd hh24:mi:ss')";
            }
        }

        return null;
    }

}
