package com.yang.study;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class MyDataSourceFilter {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        DataStreamSource<Integer> dataSource = env.fromCollection(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        SingleOutputStreamOperator<Integer> filter = dataSource.filter((Integer i) -> {
            if (i > 5) {
                return true;
            }
            return false;
        });
        filter.print();

        env.execute();

    }
}
