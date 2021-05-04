package com.ftj.service.impl;

import com.ftj.bean.UserAddress;
import com.ftj.service.OrderService;
import com.ftj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 1、导入dubbo依赖（2.6.2）
 * 2、配置服务提供者
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserService userService;

    public List<UserAddress> initOrder(String userId) {

        // TODO Auto-generated method stub
        //1、查询用户的收货地址
        List<UserAddress> addressList = userService.getUserAddressList(userId);
        for (UserAddress userAddress : addressList) {
            System.out.println(userAddress);
        }
        return addressList;
    }
}
