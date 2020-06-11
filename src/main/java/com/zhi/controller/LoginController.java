package com.zhi.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zhi.shiro.component.UserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {


    @Autowired
    DefaultKaptcha defaultKaptcha;

    @RequestMapping("/tologin")
    public String tologin() {
        return "/login";
    }

    @RequestMapping("/toregister")
    public String toregister() {
        return "/register";
    }

    @RequestMapping("/index")
    public String toindex() {
        return "/index";
    }


    @RequestMapping("/noAuth")
    public String tonoAuth() {
        return "/noAuth";
    }

    @RequestMapping("/first")
    public String tofirst() {
        return "/first";
    }

    @RequestMapping("/login/in/{flag}")
    public String login(String username, String password, Model model, @PathVariable("flag") Integer flag) {
        /**
         * 使用shiro编写认证操作
         */
        //获取subject:当前执行用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UserToken token = null;
        //读取用户身份
        if (flag.equals(1)) {
            token = new UserToken(username, password, "Admin");
        } else {
            token = new UserToken(username, password, "User");
        }

        token.setRememberMe(false);
        //执行登录方法
        try {
            subject.login(token);

            //验证码


            System.out.println("登陆成功");
            //登录成功
            //重定向：跳转到first页面
            return "redirect:/first";
        } catch (UnknownAccountException e) {
            //登录失败:用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //登录失败:用户名不存在
            model.addAttribute("msg", "密码错误");
            //请求路径
            return "login";
        }
    }
}
