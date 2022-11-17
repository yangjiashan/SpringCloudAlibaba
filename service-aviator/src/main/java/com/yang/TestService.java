package com.yang;

import com.googlecode.aviator.AviatorEvaluator;

import java.util.HashMap;
import java.util.Map;

public class TestService {


    public static void main(String[] args) {
        // 输出的是6.8
        System.out.println(AviatorEvaluator.execute("1 + 2.8 + 3"));

        String name = "鹿骁俸";
        Map<String, Object> env = new HashMap<>();
        env.put("name", name);
        // 输出的是你的名字是：鹿骁俸
        System.out.println(AviatorEvaluator.execute("'你的名字是：' + name", env));
        // Aviator 2.2 开始新增加一个exec方法, 可以更方便地传入变量并执行, 而不需要构造env这个map了
        System.out.println(AviatorEvaluator.exec("'你的名字是：' + name", name));

        env.put("a", 5);
        env.put("b", 4);
        // 输出的是6.333333333333333
        System.out.println(AviatorEvaluator.execute("a + b / 3.0", env));
        // 推荐的使用方式
        System.out.println(AviatorEvaluator.compile("a + b / 3.0").execute(env));
    }
}
