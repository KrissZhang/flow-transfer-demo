package com.self.flowtransfer.web;

import com.self.flowtransfer.plugin.scheduledthread.ScheduledThreadPlugin;
import com.self.flowtransfer.util.DateUtil;
import com.self.flowtransfer.util.DbUtil;
import com.jfinal.config.*;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目主配置类
 */
public class WebConfig extends JFinalConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void configConstant(Constants constants) {
        constants.setEncoding("UTF-8");
        constants.setJsonFactory(new JacksonFactory());
        constants.setJsonDatePattern(DateUtil.format_yyyy_MM_dd_HHmmss);
    }

    @Override
    public void configRoute(Routes routes) {

    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        //数据源配置
        loadPropertyFile("database.properties");
        String dbConfig = getProperty("database.register", "");
        String[] dataBases = dbConfig.split(",");
        for (String dbKey : dataBases) {
            if (StrKit.isBlank(dbKey)) {
                continue;
            }

            //加载数据源插件
            DruidPlugin dp = new DruidPlugin(
                    getProperty(dbKey + ".jdbcUrl"),
                    getProperty(dbKey + ".user"),
                    getProperty(dbKey + ".password"),
                    getProperty(dbKey + ".driver"));

            ActiveRecordPlugin arp;
            if ("main".equals(dbKey)) {
                arp = new ActiveRecordPlugin(dp);
                //添加sql模板文件
                arp.addSqlTemplate("sqltemplate/main.sql");
            } else {
                arp = new ActiveRecordPlugin(dbKey, dp);
            }
            arp.setDialect(DbUtil.getDbDialectByDriver(getProperty(dbKey + ".driver")));
            arp.getEngine().setDevMode(false);

            plugins.add(dp);
            plugins.add(arp);
        }

        //缓存插件
        plugins.add(new EhCachePlugin());

        //定时线程控件
        ScheduledThreadPlugin scheduledThreadPlugin = ScheduledThreadPlugin.builder()
                .enableConfigFile("schedule.properties")
                .build();
        plugins.add(scheduledThreadPlugin);
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
