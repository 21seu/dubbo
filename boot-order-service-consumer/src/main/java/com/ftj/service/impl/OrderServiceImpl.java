package com.ftj.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ftj.bean.UserAddress;
import com.ftj.service.OrderService;
import com.ftj.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 1、导入dubbo依赖（2.6.2）
 * 2、配置服务提供者
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Reference(url = "127.0.0.1:20880", loadbalance = "random", timeout = 1000) //远程调用 如果配置url属性那么是直接绕过注册中心
            UserService userService;

    @HystrixCommand(fallbackMethod = "hello")
    @Override
    public List<UserAddress> initOrder(String userId) {

        // TODO Auto-generated method stub
        //1、查询用户的收货地址
        List<UserAddress> addressList = userService.getUserAddressList(userId);
        /*for (UserAddress userAddress : addressList) {
            System.out.println(userAddress);
        }
        System.out.println(addressList);*/
        return addressList;
    }

    public List<UserAddress> hello(String userId) {

        return Arrays.asList(new UserAddress(10, "测试地址", "1", "测试", "测试", "Y"));
    }
}
