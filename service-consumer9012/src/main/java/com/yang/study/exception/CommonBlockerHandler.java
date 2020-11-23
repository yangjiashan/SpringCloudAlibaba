package com.yang.study.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class CommonBlockerHandler {
	public static String process(BlockException exception) {
		return "资源限流服务";
	}
}
