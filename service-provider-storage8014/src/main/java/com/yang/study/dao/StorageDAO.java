package com.yang.study.dao;

import com.yang.study.entity.Storage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageDAO {


    /**
     * 1 扣减库存
     * @param storage
     * @return
     */
    int update(Storage storage);
}
