package com.mmm.clout.advertisementservice.apply.infrastructure.provider.feignclient;

import com.mmm.clout.advertisementservice.apply.domain.info.AdvertiserInfo;
import com.mmm.clout.advertisementservice.apply.domain.info.ClouterInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberFeignClient {

    @GetMapping("/advertisers/{advertiserId}")
    AdvertiserInfo getAdvertiser(@PathVariable Long advertiserId);

    @GetMapping("/clouters/{clouterId}")
    ClouterInfo getClouter(Long clouterId);
}
