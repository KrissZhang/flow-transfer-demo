package com.self.flowtransfer.enumobj;

/**
 * 响应结果
 */
public enum ResponseStatusEnum {

    SUCCESS(200,"接收成功"),
    SYS_INTERVAL_ERROR(501,"服务器遇到错误，无法完成请求"),
    //------------------系统错误 700-799---------------------
    SYS_AUTHENTICATION_FAILURE(700,"鉴权失败"),
    SYS_INVALID_PARAM(701,"参数解析异常"),
    SYS_REQUEST_DATA_ERROR(702,"请求数据格式错误"),
    SYS_DB_SAVE_ERROR(703,"数据入库异常"),
    SYS_INVALID_MD5(704,"md5校验失败"),
    SYS_API_UNAUTHORIZED(705,"接口未授权"),
    SYS_LOGIN_ERROR(706,"登录失败，用户名或密码错误"),
    SYS_API_ERROR(707,"业务类型不存在"),
    SYS_NULLABLE_REQUEST_FILE(708,"请求文件为空"),
    SYS_DB_PRIMARY_ERROR(709,"数据入库主键冲突"),
    SYS_FILE_TYPE_NOTSUPPORTED(710,"不支持此类文件"),
    SYS_DOWNLOAD_FILE_NULL_ERROR(711,"下载文件为空文件"),
    SYS_RESPONSEFILE_NOTFOUND(712,"响应文件不存在"),
    SYS_DOWNLOAD_FILE_UNHANDLED_ERROR(713,"下载文件还未处理好"),
    SYS_INVALID_REQUEST_FILE_NAME(714,"请求文件名格式不正确"),
    SYS_NOT_NEW_FILE_ERROR(715,"没有更新的文件"),
    //------------------业务错误 800-999--------------------
    NO_DATA_ERROR(800,"暂无数据"),
    BIZ_READ_FILE_ERROR(801,"文件读取异常"),
    BIZ_UNKNOWN_EXCEPTION(999,"未知异常");

    private int code;
    private String msg;

    private ResponseStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
