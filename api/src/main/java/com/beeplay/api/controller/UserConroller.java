package com.beeplay.api.controller;

import com.beeplay.api.po.TestDemo;
import com.beeplay.api.service.TestDemoService;
import com.beeplay.core.redis.RedisManager;
import com.beeplay.core.utils.GfJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserConroller {

    @Autowired
    private TestDemoService testDemoService;
    @Autowired
    private RedisManager redisManager;


    @RequestMapping(value = "/find")
    @ResponseBody
    public String find() {
        return "hello word!";
    }

    @RequestMapping(value = "/name")
    public String name(String username,String id) {
        return username+id;
    }

    @RequestMapping(value = "/list")
    public String list(){
        List<TestDemo> list=testDemoService.listTestDemo();
        return GfJsonUtil.toJSONString(list);
    }
    @RequestMapping(value = "/save")
    public String save(){
        TestDemo ts=new TestDemo();
        ts.setName("你好啊！");
        testDemoService.saveTestDemo(ts);
        return "ok";
    }
    @RequestMapping(value = "/set")
    public String set(){
        redisManager.set("chen","hi");
        return "ok";
    }
    @RequestMapping(value = "/get")
    public String get(){
        return  redisManager.get("chen");
    }
}
