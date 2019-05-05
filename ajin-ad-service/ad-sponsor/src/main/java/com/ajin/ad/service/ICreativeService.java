package com.ajin.ad.service;

import com.ajin.ad.exception.AdException;
import com.ajin.ad.vo.CreativeRequest;
import com.ajin.ad.vo.CreativeResponse;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 19:56
 */
public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request) throws AdException;
}
