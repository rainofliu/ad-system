package com.ajin.ad.controller;

import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.ICreativeService;
import com.ajin.ad.vo.CreativeRequest;
import com.ajin.ad.vo.CreativeResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:57
 */
@Slf4j
@RestController
public class CreativeOPController {

    private final ICreativeService creativeService;

    @Autowired
    public CreativeOPController(ICreativeService creativeService) {
        this.creativeService = creativeService;
    }

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws AdException {

        log.info("ad-sponsor：createCreative -> {}", JSON.toJSONString(request));

        return creativeService.createCreative(request);

    }
}
