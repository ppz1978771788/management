package com.pp.controller;

import com.pp.pojo.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller

@RequestMapping("sys")
public class SystemController {
    @RequestMapping("toLogin")
    public String toLogin(){
        return "system/index/login";
    }
    @RequestMapping("/index")
    public String index(){
        return "system/index/dd";
    }
    @RequestMapping("/toDeskManager")
    public String toDeskManager(){

        return "system/index/deskManager";
    }
    @RequestMapping("/toLogLoginManager")
    public String toLogLoginManager(){
        return "system/loginfo/loginfoManager";
    }
    @RequestMapping("/toNoticeManager")
    public String toNoticeManager(){
        return "system/notice/noticeManager";
    }

    /*
    * 部门管理
    * */
    @RequestMapping("/toDeptManager")
    public String toDeptManager(){
        return "system/dept/deptManager";
    }
    @RequestMapping("/toDeptLeft")
    public String toDeptLeft(){
        return "system/dept/deptLeft";
    }
    @RequestMapping("/toDeptRight")
    public String toDeptRight(){
        return "system/dept/deptRight";
    }
    /*
     * 菜单管理
     * */
    @RequestMapping("/toMenuManager")
    public String toMenuManager(){
        return "system/menu/menuManager";
    }
    @RequestMapping("/toMenuLeft")
    public String toMenuLeft(){
        return "system/menu/menuLeft";
    }
    @RequestMapping("/toMenuRight")
    public String toMenuRight(){
        return "system/menu/menuRight";
    }
    /*
     * 权限管理
     * */
    @RequestMapping("/toPermissionManager")
    public String topermissionManager(){
        return "system/permission/permissionManager";
    }
    @RequestMapping("/toPermissionLeft")
    public String topermissionLeft(){
        return "system/permission/permissionLeft";
    }
    @RequestMapping("/toPermissionRight")
    public String topermissionRight(){
        return "system/permission/permissionRight";
    }
    /*
    * 角色管理
    * */
    @RequestMapping("/toRoleManager")
    public String toRoleManager(){
        return "system/role/roleManager";
    }
    /*
     * 用户管理
     * */
    @RequestMapping("/toUserManager")
    public String toUserManager(){
        return "system/user/userManager";
    }
    @RequestMapping("/toChangePwd")
        public String toChangePwd(){
        return "system/user/changePwd";
    }
    @RequestMapping("toLogout")
    public String toLogout(){
        return "system/index/login";
    }
}
