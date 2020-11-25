package com.yang.study.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id;
    private String user_id;
    private String commodity_code;
    private int count;
    private int money;
}
