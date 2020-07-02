package com.pp.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.pp.common.ActiverUser;
import com.pp.common.ResultObj;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/login")
    @ResponseBody
    public ResultObj Login(@RequestParam("loginname")String loginname,@RequestParam("pwd")String pwd, @RequestParam("code")String code, HttpSession session){
        //登录实现
       Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname,pwd);
        try {
            if (!code.equals(session.getAttribute("code"))&&code!=null){
                return ResultObj.CODE_ERROR;
            }else {
                subject.login(token);
//            subject.getPrincipal(); 获取身份
                ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
                session.setAttribute("user", activerUser.getUser());
                return ResultObj.LOGIN_SUCCESS;
            }
        } catch (AuthenticationException e) {
                return  ResultObj.LOGIN_ERROR_PASS;
        }
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }

return "index";
    }

}
