package com.self.flowtransfer.service.baseservice;

import com.self.flowtransfer.enumobj.UploadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * 公共上传服务
 */
public abstract class BaseUploadService {

    private static final Logger logger = LoggerFactory.getLogger(BaseUploadService.class);

    /**
     * 车牌模式池
     */
    protected static Pattern VEHICLEID_PATTERN = Pattern.compile("^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z0-9]{1}[A-Z0-9]{1}([京津沪渝桂蒙宁新藏冀晋辽吉黑苏浙皖赣闽鲁粤鄂湘豫川云贵陕甘青琼])?[A-NP-Z0-9]{1}[A-NP-Z0-9]{3}([A-NP-Z0-9挂学警港澳领试超外]{1}|应急)([A-NP-Z0-9外])?_(0|1|2|3|4|5|6|7|11|12))$|^(([A-Z0-9]{7})_(0|1|2|3|4|5|6|7|11|12))$|^默A00000_7$|^(应急[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z0-9]{1}[A-NP-Z0-9]{4}_(0|1|2|3|4|5|6|7|11|12))$");

    /**
     * 识别车牌ID模式池
     */
    protected static Pattern IDENTIFYVEHICLEID_PATTERN = Pattern.compile("^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z0-9]{1}[A-Z0-9]{1}([京津沪渝桂蒙宁新藏冀晋辽吉黑苏浙皖赣闽鲁粤鄂湘豫川云贵陕甘青琼])?[A-NP-Z0-9]{1}[A-NP-Z0-9]{3}([A-NP-Z0-9挂学警港澳领试超外]{1}|应急)([A-NP-Z0-9外])?_(0|1|2|3|4|5|6|9|11|12))$|^(([A-Z0-9]{7})_(0|1|2|3|4|5|6|9|11|12))$|^默A00000_9$|^(应急[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z0-9]{1}[A-NP-Z0-9]{4}_(0|1|2|3|4|5|6|9|11|12))$");

    /**
     * 错误信息模式池
     */
    private static Pattern ERROR_MSG_PATTERN = Pattern.compile("(.*\\n共有)(\\d)(条数据有错误.*\\n.*)(交易编号重复)");

    //上传业务类型
    protected UploadType uploadType;

    //id
    protected String jsonIdName = "id";

    //数据打包成功目录
    protected Path packageSuccessDir;

    //文件待压缩目录
    protected Path zippingDir;

    //文件待上传目录
    protected Path uploadingDir;

    //文件上传成功目录
    protected Path uploadSuccessDir;

    //文件上传失败目录
    protected Path uploadFailureDir;

    //上传响应目录
    protected Path uploadResDir;

    //上传响应成功目录
    protected Path uploadResSuccessDir;

    //TODO

}
