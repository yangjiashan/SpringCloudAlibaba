package com.yang.study.serviceImpl;

import com.yang.study.dao.StorageDAO;
import com.yang.study.entity.Storage;
import com.yang.study.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageDAO storageDAO;

    @Override
    public int upadate(Storage storage) {
        return storageDAO.update(storage);
    }


}
