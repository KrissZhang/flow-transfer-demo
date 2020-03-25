package com.self.flowtransfer.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.self.flowtransfer.common.CommonAttr;

import java.util.List;

/**
 * 测试Dao
 */
public class TestDao {

    /**
     * 分页查询测试表数据
     * @return 测试表分页数据
     */
    public List<Record> queryTestListPaging(int start, int end){
        String sql = Db.getSql(CommonAttr.SQL_TEMPLATE_ORACLE + "queryTestListPaging");
        return Db.use(CommonAttr.DB_MAIN).find(sql, end, start);
    }

}
