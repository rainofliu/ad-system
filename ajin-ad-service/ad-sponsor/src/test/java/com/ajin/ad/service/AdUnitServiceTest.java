package com.ajin.ad.service;

import com.ajin.ad.Application;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.vo.AdUnitRequest;
import com.ajin.ad.vo.AdUnitResponse;
import com.ajin.ad.vo.CreativeUnitRequest;
import com.ajin.ad.vo.CreativeUnitResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/26 15:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdUnitServiceTest {

    @Autowired
    private IAdUnitService adUnitService;

    @Test
    public void testCreateUnit() throws Exception {

        AdUnitRequest request = new AdUnitRequest();
        request.setPlanId(10L);
        request.setUnitName("第三个推广单元");
        request.setPositionType(1);
        request.setBudget(1000000000L);
        AdUnitResponse response = adUnitService.createUnit(request);
        System.out.println(response);
    }

    @Test
    public void testUpdateUnit() throws AdException {

        AdUnitRequest request = new AdUnitRequest();
        request.setId(13L);
        request.setPlanId(10L);
        request.setUnitName("第三个推广单元");
        request.setPositionType(2);
        AdUnitResponse response = adUnitService.updateUnit(request);
        System.out.println(response);
    }

    @Test
    public void testCreateCreativeUnit() throws AdException {

        CreativeUnitRequest request = new CreativeUnitRequest();
        List<CreativeUnitRequest.CreativeUnitItem> list = new ArrayList<>();
        request.setUnitItems(list);
        CreativeUnitRequest.CreativeUnitItem item =
                new CreativeUnitRequest.CreativeUnitItem(10L, 12L);
        list.add(item);
        CreativeUnitResponse response = adUnitService.createCreativeUnit(request);
        System.out.println(response);
    }

}
