package com.ajin.ad.service.impl;

import com.ajin.ad.dao.CreativeRepository;
import com.ajin.ad.entity.AdCreative;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.ICreativeService;
import com.ajin.ad.vo.CreativeRequest;
import com.ajin.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:05
 */
@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {

        // 忽略参数校验的过程

        AdCreative creative = creativeRepository.save(
                request.convertToEntity()
        );

        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
