package com.ajin.ad.service;

import com.ajin.ad.Application;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.vo.AdPlanGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @Author: ajin
 * @Date: 2019/4/26 15:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, // 标记启动程序
        webEnvironment = SpringBootTest.WebEnvironment.NONE) // 不设置web环境,仅对service层测试
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService planService;

    @Test
    public void testGetAdPlan() throws AdException {

        System.out.println(
                planService.getAdPlanByIds(
                        new AdPlanGetRequest(15L, Collections.singletonList(10L))
                )
        );
    }
}
