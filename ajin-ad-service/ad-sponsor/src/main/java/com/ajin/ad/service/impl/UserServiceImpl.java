package com.ajin.ad.service.impl;

import com.ajin.ad.constant.Constants;
import com.ajin.ad.dao.AdUserRepository;
import com.ajin.ad.entity.AdUser;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IUserService;
import com.ajin.ad.utils.CommonUtils;
import com.ajin.ad.vo.CreateUserRequest;
import com.ajin.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 16:49
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final AdUserRepository userRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AdUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
        // 请求参数为空，抛出异常
        if (!request.validate()) {
            throw new AdException(Constants.ErrMsg.REQUEST_PARAM_ERROR);
        }
        // 查询用户名是否已存在;如果已存在，则抛出异常
        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdException(Constants.ErrMsg.SAME_NAME_ERROR);
        }
        // 新增AdUser
        AdUser newUser = userRepository.save(new AdUser(request.getUsername(),
                CommonUtils.md5(request.getUsername())));
        // 返回参数
        return new CreateUserResponse(
                newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}
