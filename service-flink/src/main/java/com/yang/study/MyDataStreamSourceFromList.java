package com.yang.study;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class MyDataStreamSourceFromList {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        DataStreamSource<Integer> dataSource = env.fromCollection(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        dataSource.print();

        env.execute();

    }






}
