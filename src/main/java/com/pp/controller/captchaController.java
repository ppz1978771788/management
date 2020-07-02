package com.pp.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ICaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/sys/captcha")
public class captchaController {
    @RequestMapping("/getcaptcha")
    public void captcha(HttpSession session, HttpServletResponse response) throws IOException {
        System.out.println("进入测试代码");
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(116, 36, 4, 10);
        String code = captcha.getCode();
        System.out.println(code);
        session.setAttribute("code",code);
        System.out.println(code);
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        outputStream.close();
    }
    }
