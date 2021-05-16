package com.ftj.service.impl;

import com.ftj.bean.UserAddress;
import com.ftj.service.UserService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 本地存根
 */
public class UserServiceStub implements UserService {

    private final UserService userService;

    /**
     * 传入的是userService的远程代理对象
     *
     * @param userService
     */
    public UserServiceStub(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.err.println("userServiceStub");
        if (!StringUtils.isEmpty(userId)) {
            return userService.getUserAddressList(userId);
        }
        return null;
    }
}
