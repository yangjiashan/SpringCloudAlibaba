package com.yang.study;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.net.URL;


public class BoundedStreamWordCount {

    public static void main(String[] args) throws Exception {

        // 1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        URL resource = BatchWordCount.class.getClassLoader().getResource("input/word.txt");
        // 2.读取文件
        DataStreamSource<String> dataStreamSource = env.readTextFile(resource.getPath());

        // 3.转换计算
        SingleOutputStreamOperator<Tuple2<String, Long>> outputStreamOperator = dataStreamSource.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 4.分组
        KeyedStream<Tuple2<String, Long>, String> keyedStream = outputStreamOperator.keyBy(data -> data.f0);

        // 5.求和
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyedStream.sum(1);

        sum.print();

        env.execute();


    }
}
