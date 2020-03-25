package com.self.flowtransfer.controller;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.core.Controller;
import com.self.flowtransfer.service.test.TestService;

/**
 * 测试Controller
 */
public class TestController extends Controller {

    private static TestService service = new TestService();

    /**
     * 请求入口方法
     * @return 请求结果
     */
    public void index(){
        //请求参数
        String start = getPara("start");
        String end = getPara("end");

        JSONArray result = service.getTestDataForPage(Integer.parseInt(start), Integer.parseInt(end));
        renderJson(result);
    }

}
