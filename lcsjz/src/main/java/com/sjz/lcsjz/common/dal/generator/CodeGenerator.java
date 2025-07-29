package com.sjz.lcsjz.common.dal.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {

    /**
     * 数据库连接 URL
     */
    private static final String JDBC_URL = "jdbc:postgresql://ep-steep-tree-a1lm9jhg-pooler.ap-southeast-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require";
    /**
     * 数据库用户名
     */
    private static final String USERNAME = "neondb_owner";
    /**
     * 数据库密码
     */
    private static final String PASSWORD = "npg_LQk3bJgNntz9"; // 请替换为你的最新密码

    public static void main(String[] args) {
        // 1. 获取当前项目的根目录，以实现路径的动态化，避免硬编码
        String projectPath = System.getProperty("user.dir")+"/lcsjz";

        FastAutoGenerator.create(JDBC_URL, USERNAME, PASSWORD)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("enhe") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            // 【修正】输出目录应指向项目的 java 源码根目录
                            .outputDir(projectPath + "/src/main/java");
                })
                // 数据源配置
                .dataSourceConfig(builder ->
                        // 【修正】在 URL 中添加 currentSchema=market_data 是更通用的做法
                        // 如果不加，也可以在这里通过 .schema("market_data") 指定
                        builder.schema("market_data").typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                // 包配置
                .packageConfig(builder ->
                        builder.parent("com.sjz.lcsjz") // 【修正】设置基础包名
                                .entity("common.dal.entity") // Entity 类的子包
                                .mapper("common.dal.mapper") // Mapper 接口的子包
                                .service("core.service") // 【修改】Service 接口的子包，移到 core 层
                                .serviceImpl("core.service.impl") // 【修改】Service 实现类的子包，移到 core 层
                                // 【修正】将 Mapper XML 文件输出到 resources 目录
                                // 这样配置可以实现 Mapper 接口和 XML 文件在编译后位于同一路径下，符合最佳实践，无需额外配置 mapper-locations
                                .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper"))
                )
                // 策略配置
                .strategyConfig(builder ->
                        builder.addInclude("items", "market_snapshots", "price_ticks") // 设置需要生成的表名
                                .entityBuilder().enableLombok() // 启用 Lombok
                                // .javaTemplate("/templates/entity.java") // 如果你有自定义模板，请确保它们在 resources/templates 目录下
                                .serviceBuilder() // Service 策略配置
                                // .serviceTemplate("/templates/service.java")
                                // .serviceImplTemplate("/templates/serviceImpl.java")
                                .mapperBuilder().enableMapperAnnotation() // 为 Mapper 接口添加 @Mapper 注解
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板
                .execute();
    }
}
