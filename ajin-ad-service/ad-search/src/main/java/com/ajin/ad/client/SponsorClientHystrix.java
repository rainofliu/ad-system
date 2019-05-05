package com.ajin.ad.client;

import com.ajin.ad.client.vo.AdPlan;
import com.ajin.ad.client.vo.AdPlanGetRequest;
import com.ajin.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/12 11:17
 */
@Component
public class SponsorClientHystrix implements SponsorClient {

    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        return new CommonResponse<>(-1, "eureka-client-ad-sponsor error");
    }
}
