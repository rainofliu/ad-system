package com.ajin.ad.controller;

import com.ajin.ad.entity.AdUser;
import com.ajin.ad.exception.AdException;
import com.ajin.ad.service.IUserService;
import com.ajin.ad.vo.CreateUserRequest;
import com.ajin.ad.vo.CreateUserResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 20:29
 */
@Slf4j
@RestController
public class UserOPController {

    private final IUserService userService;

    @Autowired
    public UserOPController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {

        log.info("ad-sponsor:createUser-> {}", JSON.toJSONString(request));

        return userService.createUser(request);
    }

    @PostMapping("/get/users")
    public List<AdUser> getAllUsers() {

        return userService.getAllUsers();
    }

}
