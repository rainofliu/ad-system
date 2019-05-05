package com.ajin.ad.client;

import com.ajin.ad.client.vo.AdPlan;
import com.ajin.ad.client.vo.AdPlanGetRequest;
import com.ajin.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: ajin
 * @Date: 2019/4/12 11:09
 */
// 定义调用的服务名称,指定服务降级
@FeignClient(value = "eureka-client-ad-sponsor",fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/adPlan", method = RequestMethod.POST)
    CommonResponse<List<AdPlan>> getAdPlans(@RequestBody
                                                    AdPlanGetRequest request);

}
