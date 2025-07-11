package com.ms.ORDER_SERVICE.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthFeignClient {

    @GetMapping("/auth/count")
    long getTotalUsers();
}
