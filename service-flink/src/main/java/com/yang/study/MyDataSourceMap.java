package com.yang.study;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class MyDataSourceMap {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        DataStreamSource<Integer> dataSource = env.fromCollection(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        SingleOutputStreamOperator<String> streamOperator = dataSource.map((Integer i) -> {
            return i + "-i";
        });
        streamOperator.print();

        env.execute();

    }



}
