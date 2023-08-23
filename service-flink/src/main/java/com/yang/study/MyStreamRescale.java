package com.yang.study;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MyStreamRescale {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        DataStreamSource<String> stream = env.socketTextStream("hadoop102", 7777);;

        stream.rescale().print();

        env.execute();
    }

}
