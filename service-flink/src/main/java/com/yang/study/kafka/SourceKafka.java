package com.yang.study.kafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.sink.TopicSelector;
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
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/***
 * 跟kafka部署的版本要对应，否则会报错，这边用的是3.2.0
 */
public class SourceKafka {

    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder().setBootstrapServers("10.231.6.115:9092")
                .setTopics("first")
                .setGroupId("test-consumer-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers("10.231.6.115:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder().setTopic("second").setValueSerializationSchema(new SimpleStringSchema()).build())
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .setTransactionalIdPrefix("prefix-")
                .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
                .build();

        DataStreamSource<String> stream = env.fromSource(kafkaSource, WatermarkStrategy.forMonotonousTimestamps(), "kafkaSource");

        SingleOutputStreamOperator<Tuple2<String, Long>> streamOperator = stream.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        SingleOutputStreamOperator<String> process = streamOperator.keyBy(tuple -> tuple.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(3)))
                .process(new ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>() {
                    @Override
                    public void process(String key, ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow>.Context context, Iterable<Tuple2<String, Long>> iterable, Collector<String> collector) throws Exception {
                        System.out.println("===========================");
                        System.out.println("当前key" + key);
                        System.out.println("当前时间戳：" + context.currentProcessingTime());
                        Instant instant = Instant.ofEpochMilli(context.currentProcessingTime());
                        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        System.out.println("当前时间戳：" + localDateTime.format(formatter));
                        System.out.println("当前水位线：" + context.currentWatermark());
                        System.out.println("窗口开始时间：" + context.window().getStart());
                        System.out.println("窗口结束时间：" + context.window().getEnd());
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Tuple2<String, Long> element : iterable) {
                            System.out.println("窗口内数据：");
                            System.out.println(element);
                            stringBuilder.append(element).append(" ");
                        }
                        System.out.println("===========================");
                        collector.collect(stringBuilder.toString());
                    }
                });


        process.print();
        process.sinkTo(kafkaSink);
        env.execute();
    }
}
