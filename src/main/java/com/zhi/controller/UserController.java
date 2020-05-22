package com.zhi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/add")
    public String user_add(){
        return "/user/add";
    }


    /**
     * 测试
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("hi");
        return "hhhhhok";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("/testThymeleaf")

    public String testThymeleaf(Model model){
        //把数据存入model
        model.addAttribute("name", "lyz");
        //返回test.html
        return "index";
    }

}
