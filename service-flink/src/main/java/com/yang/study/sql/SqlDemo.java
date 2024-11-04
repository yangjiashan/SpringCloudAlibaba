package com.yang.study.sql;

import org.apache.flink.configuration.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.config.ExecutionConfigOptions;

import java.util.function.Supplier;

public class SqlDemo {

    public static void main(String[] args) {

        // 创建表环境 方式1
//        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
//        TableEnvironment tableEnv = TableEnvironment.create(settings);
//        Configuration configuration = tableEnv.getConfig().getConfiguration();
//        configuration.setInteger(ExecutionConfigOptions.TABLE_EXEC_RESOURCE_DEFAULT_PARALLELISM, 1);

        // 创建表环境 方式2
        Configuration configuration = new Configuration();
        configuration.setInteger(ExecutionConfigOptions.TABLE_EXEC_RESOURCE_DEFAULT_PARALLELISM, 1);
        configuration.setInteger(RestOptions.PORT, 8050);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        StreamTableEnvironment tableEnv =StreamTableEnvironment.create(env);

        // 注册来源表
        tableEnv.executeSql("create table sourcedata (\n" +
                "  id int, \n" +
                "  ts bigint,\n" +
                "  vc int\n" +
                ")with(\n" +
                "  'connector'='datagen',\n" +
                "  'rows-per-second'='1',\n" +
                "  'fields.id.kind'='random',\n" +
                "  'fields.id.min'='1',\n" +
                "  'fields.id.max' ='10',\n" +
                "  'fields.ts.kind' = 'sequence',\n" +
                "  'fields.ts.start' = '1',\n" +
                "  'fields.ts.end' = '1000000',\n" +
                "  'fields.vc.kind' = 'random',\n" +
                "  'fields.vc.min' = '1',\n" +
                "  'fields.vc.max' = '1000'\n" +
                ")");

        // 注册目标表
        tableEnv.executeSql("CREATE TABLE testout(\n" +
                "    id INT, \n" +
                "    vc bigint\n" +
                ") WITH (\n" +
                "'connector' = 'print'\n" +
                ");");

        // 执行查询
        Table table = tableEnv.sqlQuery("select id, count(vc) as sumvc from sourcedata where id >5 group by id; ");
        tableEnv.createTemporaryView("temp", table);
        Table filterTable = tableEnv.sqlQuery("select * from temp where id < 7");
        tableEnv.createTemporaryView("temp2", filterTable);

        tableEnv.executeSql("insert into testout select *from temp2");



    }


}
