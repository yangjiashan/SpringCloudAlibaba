package com.yang.study;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MyDataStreamSourceFromFile {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> fileSource = env.fromSource(FileSource.forRecordStreamFormat(new TextLineInputFormat(), new Path(MyDataStreamSourceFromFile.class.getClassLoader().getResource("input/word.txt").getPath())).build(), WatermarkStrategy.noWatermarks(), "fileSource");

        fileSource.print();

        env.executeAsync();


    }

}
