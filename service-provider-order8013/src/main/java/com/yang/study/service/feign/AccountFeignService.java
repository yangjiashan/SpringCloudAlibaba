package com.yang.study.service.feign;

import com.yang.study.entity.Account;
import com.yang.study.vo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-provider-account8015")
public interface AccountFeignService {

    /**
     * 扣减余额
     *
     * @param account
     * @return
     */
    @PostMapping("/account/minusAccountMoney")
    CommonResult minusAccountMoney(@RequestBody Account account);
}
