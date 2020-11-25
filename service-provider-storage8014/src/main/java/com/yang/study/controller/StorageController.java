package com.yang.study.controller;

import com.yang.study.entity.Storage;
import com.yang.study.service.IdGeneratorSnowflake;
import com.yang.study.service.StorageService;
import com.yang.study.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Resource
    private IdGeneratorSnowflake idGeneratorSnowflake;

    /***
     * 扣减库存
     * @param storage
     * @return
     */
    @PostMapping("/storage/minisStorage")
    public CommonResult minisStorage(@RequestBody Storage storage) {
//        int i = 1/0;
        storageService.upadate(storage);
        return new CommonResult(200, "库存扣减成功");
    }

    /**
     * 生成id,通过雪花算法
     *
     * @return
     */
    @GetMapping("/snowflake")
    public String getIDBySnowflake() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            threadPool.submit(() -> {
                System.out.println(idGeneratorSnowflake.snowflakeId());
            });
        }
        threadPool.shutdown();
        return "hello snowflake";
    }
}
