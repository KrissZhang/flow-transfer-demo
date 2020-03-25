package com.self.flowtransfer.util;

import java.util.UUID;

/**
 * 32位UUID工具类
 */
public class UUIDUtil {

    /**
     * 获取UUID
     * @return UUID
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
