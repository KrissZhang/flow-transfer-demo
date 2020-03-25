package com.self.flowtransfer.util;

import com.jfinal.kit.Prop;

/**
 * 配置工具类
 */
public class ConfigUtil {

    /**
     * 通用配置文件
     */
    public static Prop SERVER_CONF = new Prop("config.properties");

    /**
     * 获取服务根目录
     * @return 服务根目录
     */
    public static String getServerDirHome() {
        return SERVER_CONF.get("dir.home");
    }

}
