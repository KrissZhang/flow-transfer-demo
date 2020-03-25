package com.self.flowtransfer.enumobj;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 上传业务类型枚举
 */
public enum UploadType {

    /**
     * 通用上传业务
     */
    INTRANSACTION_COMMON_REQ("TRANSACTION_COMMON_REQ", "", "ADMIN", "通用上传业务", "T_TABLE_1");

    /**
     * 业务类型编码
     */
    private final String code;

    /**
     * 校验接口编码
     */
    private final String validateCode;

    /**
     * 发送者
     */
    private final String sender;

    /**
     * 业务类型名称
     */
    private final String name;

    /**
     * 处理表名
     */
    private final String tableName;

    UploadType(String code, String validateCode, String sender, String name, String tableName){
        this.code = code;
        this.validateCode = validateCode;
        this.sender = sender;
        this.name = name;
        this.tableName = tableName;
    }

    public String getCode() {
        return code;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public String getSender() {
        return sender;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * 通过业务名称获取上传业务类型
     * @param serviceName 业务名称
     * @return 上传业务类型
     */
    public static UploadType getUploadTypeByServiceName(String serviceName){
        return Arrays.asList(UploadType.values()).stream().filter(uploadType -> uploadType.getName().equals(serviceName)).collect(Collectors.toList()).get(0);
    }

}
