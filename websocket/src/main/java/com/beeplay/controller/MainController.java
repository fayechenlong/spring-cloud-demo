package com.beeplay.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 三个静态页面
 */
@Controller
public class MainController {

    @RequestMapping("/index")
    private String index(){
        return "index";
    }
    @RequestMapping("/index1")
    private String index1(){
        return "index1";
    }
    @RequestMapping("/index2")
    private String index2(){
        return "index2";
    }
}
