package com.pp.config;

import cn.hutool.extra.template.engine.thymeleaf.ThymeleafTemplate;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Scanner;

public class quickCode {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        //代码生成器对象
        TemplateConfig templateConfig = new TemplateConfig();
        AutoGenerator mpg = new AutoGenerator();

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //配置策略
        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        //当前的项目目录
        String property = System.getProperty("user.dir");
        //生成的路径
        gc.setOutputDir(property+"/src/main/java");
        //时候覆盖之前的
        gc.setFileOverride(false);
        //创建完是否打开目录
        gc.setOpen(false);
        //去掉service 的I前缀
        gc.setServiceName("%sservice");
        gc.setIdType(IdType.ID_WORKER);
        gc.setAuthor("pp");
        mpg.setGlobalConfig(gc) ;
        //设置数据源
        DataSourceConfig dc = new DataSourceConfig();
        dc.setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://localhost:3306/warehouse?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("peng123456");
        mpg.setDataSource(dc);
        //包的配置
        PackageConfig pc = new PackageConfig();
//        setModuleName("block")
        pc.setParent("com.pp").setController("controller").setEntity("pojo").setMapper("mapper").setService("service").setServiceImpl("service.impl").setXml("mapper.xml");;
        mpg.setPackageInfo(pc);
        //策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude("bus_salesback");

        //自动填充
//        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
//        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
//        ArrayList<TableFill> tableFills =new ArrayList<>();
//        tableFills.add(gmtCreate);
//        tableFills.add(gmtModified);
//        strategy.setTableFillList(tableFills);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
