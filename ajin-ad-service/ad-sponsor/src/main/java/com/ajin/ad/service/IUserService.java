package com.ajin.ad.service;

import com.ajin.ad.entity.AdUser;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.vo.CreateUserRequest;
import com.ajin.ad.vo.CreateUserResponse;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 16:42
 */
public interface IUserService {
    /**
     * <h2>创建用户</h2>
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;

    List<AdUser> getAllUsers();
}
