package com.self.flowtransfer.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;

/**
 * 测试Controller
 */
public class TestController extends Controller {

    /**
     * 请求入口方法
     * @return 请求结果
     */
    public void index(){
        //请求参数1
        String param1 = getPara("param1");

        //请求参数2
        String param2 = getPara("param2");

        JSONObject obj = new JSONObject();
        obj.put("p1", param1);
        obj.put("p2", param2);

        renderJson(obj);
    }

}
