package com.pp.controller;

import cn.hutool.core.util.IdUtil;
import com.pp.common.ResultObj;
import com.pp.pojo.SysUser;
import com.pp.service.SysUserservice;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/change")
@ResponseBody
public class Changepwd {
    @Autowired
    SysUserservice userservice;
    @RequestMapping("/pwd")
    public ResultObj pwd(String oldpwd, String pwd1, String pwd2, HttpSession session){
        SysUser user = (SysUser) session.getAttribute("user");
        System.out.println("oldpwd"+oldpwd+"pw1"+pwd1+"pw2"+pwd2);
        System.out.println("/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*");
        String pwd = user.getPwd();
        //解密
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
        Md5Hash md5Hash = new Md5Hash(oldpwd, user.getSalt(), 2);
        String s = md5Hash.toString();
        System.out.println("/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*");
        System.out.println(s.toString());
        System.out.println(s.equals(user.getPwd()));
      if (!s.equals(user.getPwd())){
          return ResultObj.CHANGE_OLD_ERROR;
      }else {
          //判断密码一致不一致
          if (pwd2.equals(pwd1)) {
              System.out.println();
              //一致，从新加盐
              String salt= IdUtil.simpleUUID().toLowerCase();
              Md5Hash xinpwd = new Md5Hash(pwd1, salt, 2);
              SysUser byId = userservice.getById(user.getId());
              byId.setPwd(xinpwd.toString());
              byId.setSalt(salt);
              System.out.println("新密码之后的"+xinpwd.toString());
              System.out.println("新的盐"+salt);
              session.setAttribute("user",byId);
              userservice.updateById(byId);
              return ResultObj.CHANGE_SUCCESS;
          }
              return ResultObj.CHANGE_ERROR;

      }
        }
    }

