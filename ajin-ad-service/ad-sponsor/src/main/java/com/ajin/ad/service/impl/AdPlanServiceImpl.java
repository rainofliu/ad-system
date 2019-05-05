package com.ajin.ad.service.impl;

import com.ajin.ad.constant.CommonStatus;
import com.ajin.ad.constant.Constants;
import com.ajin.ad.dao.AdPlanRepository;
import com.ajin.ad.dao.AdUserRepository;
import com.ajin.ad.entity.AdPlan;
import com.ajin.ad.entity.AdUser;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IAdPlanService;
import com.ajin.ad.utils.CommonUtils;
import com.ajin.ad.vo.AdPlanGetRequest;
import com.ajin.ad.vo.AdPlanRequest;
import com.ajin.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 17:37
 */
@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {

    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {

        // 判断请求参数是否合法
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        // 确保关联的 User 对象时存在的
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());

        // 如果adUser不存在，则抛出异常（找不到数据记录）
        if (!adUser.isPresent()) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }

        // 查询推广计划是否已经存在
        AdPlan oldPlan = planRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());

        if (oldPlan != null) {
            throw new AdException(Constants.ErrMsg.SAME_NAME_PLAN_ERROR);
        }

        // 保存推广计划
        AdPlan newAdPlan = planRepository.save(
                new AdPlan(request.getUserId(), request.getPlanName(),
                        CommonUtils.parseStringDate(request.getStartDate()),
                        CommonUtils.parseStringDate(request.getEndDate())
                )
        );

        // 返回响应数据
        return new AdPlanResponse(newAdPlan.getId(),
                newAdPlan.getPlanName()
        );
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {

        // 参数校验
        if (!request.validate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        return planRepository.findAllByIdInAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {

        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());

        if (plan == null) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }

        // 类似mybatis的动态更新
        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null) {
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }

        plan.setUpdateTime(new Date());
        // plan必须包含主键信息，否则视为插入操作
        plan = planRepository.save(plan);

        return new AdPlanResponse(plan.getId(), plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {

        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());

        if (plan == null) {
            throw new AdException(Constants.ErrMsg.CAN_NOT_FIND_RECORD);
        }

        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        planRepository.save(plan);
    }
}
