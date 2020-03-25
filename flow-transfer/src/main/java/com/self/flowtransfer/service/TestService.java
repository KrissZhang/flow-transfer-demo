package com.self.flowtransfer.service;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.self.flowtransfer.dao.TestDao;

import java.util.List;

/**
 * 测试Service
 */
public class TestService {

    private static TestDao testDao = new TestDao();

    /**
     * 获取指定范围内的测试数据
     * @param start 起始位置
     * @param end 结束位置
     * @return 测试数据
     */
    public JSONArray getTestDataForPage(int start, int end){
        JSONArray array = new JSONArray();

        List<Record> list = testDao.queryTestListPaging(start, end);
        list.forEach(record -> {
            array.add(JsonKit.toJson(record));
        });

        return array;
    }

}
