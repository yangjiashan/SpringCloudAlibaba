package com.yang.study.kafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.apache.flink.util.Collector;

import java.io.Serializable;

/***
 * 跟kafka部署的版本要对应，否则会报错，这边用的是3.2.0
 */
public class SourceKafkaSinkHive {



    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);

        tableEnv.executeSql("CREATE CATALOG my_hive_catalog WITH (" +
                "'type' = 'hive'," +
                "'default-database' = 'default'," +
                "'hive-conf-dir' = 'hdfs://hdfscluster/myconf'" +
                ")");

        tableEnv.executeSql("use catalog my_hive_catalog");

        tableEnv.executeSql("CREATE DATABASE IF NOT EXISTS my_database");
        tableEnv.executeSql("USE my_database");
        tableEnv.executeSql("CREATE TABLE IF NOT EXISTS key_word_test ( key string, `count` integer )");

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder().setBootstrapServers("10.231.6.115:9092")
                .setTopics("first")
                .setGroupId("test-consumer-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // 指定数据源并设置水位线
        DataStreamSource<String> stream = env.fromSource(kafkaSource, WatermarkStrategy.forMonotonousTimestamps(), "kafkaSource");

        SingleOutputStreamOperator<Tuple2<String, Long>> streamOperator = stream.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(StringUtils.SPACE);
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        SingleOutputStreamOperator<DataDO> process = streamOperator.keyBy(tuple -> tuple.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(60), Time.seconds(3)))
                .process(new ProcessWindowFunction<Tuple2<String, Long>, DataDO, String, TimeWindow>() {

                    @Override
                    public void process(String key, ProcessWindowFunction<Tuple2<String, Long>, DataDO, String, TimeWindow>.Context context, Iterable<Tuple2<String, Long>> iterable, Collector<DataDO> collector) throws Exception {
                        System.out.println("===========================");
                        System.out.println("当前key" + key);
                        System.out.println("当前时间戳：" + context.currentProcessingTime());
                        System.out.println("当前水位线：" + context.currentWatermark());
                        System.out.println("窗口开始时间：" + context.window().getStart());
                        System.out.println("窗口结束时间：" + context.window().getEnd());
                        int count = 0;
                        for (Tuple2<String, Long> element : iterable) {
                            System.out.println("窗口内数据：");
                            System.out.println(element);
                            count = count + element.f1.intValue();
                        }
                        System.out.println("===========================");
                        collector.collect(new DataDO(key, count));
                    }
                });

//        Table eventTable = tableEnv.fromDataStream(process,  Schema.newBuilder()
//                .column("key", "STRING")
//                .column("count", "INTEGER")
//                .build());

        // 自动映射DO到table
        Table eventTable = tableEnv.fromDataStream(process);

        // 表名用小写，驼峰会找不到表
        tableEnv.createTemporaryView("my_event_table", eventTable);
        TableResult showTables = tableEnv.executeSql("show tables");
        CloseableIterator<Row> collect = showTables.collect();
        System.out.println("==============table print start=============");
        while (collect.hasNext()) {
            Row next = collect.next();
            System.out.println(next.toString());
        }
        System.out.println("==============table print end=============");

        eventTable.insertInto("key_word_test").execute().await();

        // 用这种方式报异常： A view backed by a query operation has no serializable representation
//        tableEnv.executeSql("insert into key_word_test select my_event_table.`key`, my_event_table.`count` from my_event_table");

        process.print();
        env.execute();
    }



    public static class DataDO implements Serializable {
        private String key;
        private Integer count;

        public DataDO() {
        }

        public DataDO(String key, Integer count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}
