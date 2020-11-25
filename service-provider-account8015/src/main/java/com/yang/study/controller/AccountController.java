package com.yang.study.controller;

import com.yang.study.entity.Account;
import com.yang.study.service.AccountService;
import com.yang.study.vo.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 扣减余额
     *
     * @param account
     * @return
     */
    @PostMapping("/account/minusAccountMoney")
    public CommonResult minusAccountMoney(@RequestBody Account account) {
        accountService.update(account);
        return new CommonResult(200, "余额扣减成功成功");
    }

}
