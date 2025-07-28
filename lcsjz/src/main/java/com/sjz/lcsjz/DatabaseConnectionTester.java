package com.sjz.lcsjz;


import com.sjz.lcsjz.common.dal.mapper.ItemMapper; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动时执行，用于测试并强制初始化数据库连接。
 */
@Component // 将这个类声明为一个Spring组件
public class DatabaseConnectionTester implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionTester.class);

    // 注入您的Mapper接口
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("CommandLineRunner 正在执行，尝试测试数据库连接...");
        try {
            // 我们调用一个最简单的Mapper方法来强制Spring初始化数据库连接池。
            // selectCount会执行一条 "SELECT COUNT(*) FROM market_data.items" SQL语句。
            Long count = itemMapper.selectCount(null);
            
            // 如果上面这行代码没有抛出异常，就证明连接成功了！
            log.info("************************************************************");
            log.info("数据库连接成功！");
            log.info("在 'items' 表中找到 {} 条记录。", count);
            log.info("************************************************************");

        } catch (Exception e) {
            log.error("************************************************************");
            log.error("数据库连接失败！请检查您的配置或网络。", e);
            log.error("************************************************************");
        }
    }
}