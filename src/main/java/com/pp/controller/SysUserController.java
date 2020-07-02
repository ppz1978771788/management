package com.pp.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.PinyinUtils;
import com.pp.common.ResultObj;
import com.pp.pojo.SysDept;
import com.pp.pojo.SysRole;
import com.pp.pojo.SysUser;
import com.pp.service.SysDeptservice;
import com.pp.service.SysRoleservice;
import com.pp.service.SysUserservice;
import com.pp.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pp
 * @since 2020-06-04
 */
@Controller
@RequestMapping("/sysUser")
@ResponseBody
public class SysUserController {
    @Autowired
    private SysUserservice userservice;
    @Autowired
    private SysDeptservice deptservice;
    @Autowired
    private SysRoleservice roleservice;
    /*
    * 用户查询
    * */
    @RequestMapping("/loadAllUser")
    public DataGridView loadAllUser(UserVo userVo){
        IPage<SysUser> page = new Page<>(userVo.getPage(),userVo.getLimit());
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("type",1);
        wrapper.eq(userVo.getDeptid()!=null,"deptid",userVo.getDeptid());
        wrapper.eq(StringUtils.isNotBlank(userVo.getName()),"name",userVo.getName());
        wrapper.eq(StringUtils.isNotBlank(userVo.getAddress()),"address",userVo.getAddress());
        IPage<SysUser> page1 = userservice.page(page, wrapper);
        List<SysUser> list =page.getRecords();
        System.out.println(page1.getRecords());
        String deptname=null;
        String lodername=null;
        for (SysUser user :
                list) {
            Integer deptid=user.getDeptid();
        if (deptid!=null){
            //把所属的部门名称显示出来
            SysDept finddeptBydeptid = deptservice.getById(deptid);
             deptname = finddeptBydeptid.getTitle();
        }
        Integer mgr=user.getMgr();
        if (mgr!=null){
            //证明有上级领导，将上级领导的名字查询出来并赋值给当前这个用户
            SysUser findidBymgr = userservice.getById(mgr);
             lodername=findidBymgr.getName();
        }
        user.setDeptname(deptname);
        user.setLeadername(lodername);
        }

        return new DataGridView(list);
    }
    //排序码
    @RequestMapping("/loadUserMaxOrderNum")
    public Integer loadUserMaxOrderNum(){
        //从数据库中查询
        return userservice.loadUserMaxOrderNum()+1;
    }
    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){
        //根据传回来的部门Id找到上级领导
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(deptid!=null,"deptid",deptid);
        wrapper.eq("available", Constast.AVAILABLE_TRUE);
        wrapper.eq("type",1);
        List<SysUser> list=userservice.list(wrapper);
        return new DataGridView(list);
    }
    @RequestMapping("/changeChineseToPinyin")
    public String changeChineseToPinyin(String username){
        //讲中文转换为小写字母
        if (username!=null) {
            String pingYin = PinyinUtils.getPingYin(username);
            return pingYin;
        }else
        {
            return "";
        }
    }
    @RequestMapping("addUser")
    public ResultObj addUser(UserVo userVo){
        try {
            userVo.setType(Constast.USER_TYPE_NORMAL);//设置类型
            userVo.setHiredate(new Date());
            String salt=IdUtil.simpleUUID().toLowerCase();
            userVo.setSalt(salt);
            userVo.setPwd(new Md5Hash(Constast.USER_DEFULT_PWD,salt,2).toString());
            userservice.save(userVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    @RequestMapping("/updateUser")
    public ResultObj updateUser(UserVo userVo){
        try {
            userservice.updateById(userVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    @RequestMapping("deleteUser")
    public ResultObj deleteUser (Integer id){
        try {
            roleservice.deleteUser_role_byid(id);
            userservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    //根据当前获取的领导编号，获取领导的部门
@RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id){
        return new DataGridView(userservice.getById(id));
}
//重置密码
    @RequestMapping("resetPwd")
    public ResultObj resetPwd(Integer id){
        //从新加盐
        try {
            SysUser user =  userservice.getById(id);
            String salt=IdUtil.simpleUUID().toLowerCase();
            user.setSalt(salt);
            user.setPwd(new Md5Hash(Constast.USER_DEFULT_PWD,salt,2).toString());
            userservice.updateById(user);
            return ResultObj.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.RESET_ERROR;
        }
    }
     @RequestMapping("initRoleByUserId")
     public DataGridView initRoleByUserId(Integer id){
        //1.查询所有的可用的角色
         QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
         wrapper.eq("available",Constast.AVAILABLE_TRUE);
         List<Map<String, Object>> maps = roleservice.listMaps(wrapper);
         //根据ID查询自己已拥有的角色，
       List<Integer> currentUserRoleIds= roleservice.queryUserRoleIdsByRid(id);
         for (Map<String, Object> map :
                 maps) {
             Boolean LAY_CHECKED=false;
             Integer roleId = (Integer) map.get("id");
             for (Integer rid :
                     currentUserRoleIds ) {
                 if (map.get("id")==rid){
                     LAY_CHECKED=true;
                     break;
                 }

             }
             //设置LAY_CHECKED
             map.put("LAY_CHECKED", LAY_CHECKED);
             
         }
         return new DataGridView(Long.valueOf(maps.size()),maps);
        }
        /*
        * 保存用户关系

        * */
        @RequestMapping("saveUserRole")
        public ResultObj saveUserRole(@RequestParam("uid") Integer uid,@RequestParam("ids")Integer[] rids){
            System.out.println("***********************************************************************************************");
            System.out.println("穿过来的uid"+uid+"传过来的rids"+rids.toString());
            System.out.println("***********************************************************************************************");
            try {
                userservice.saveUserRole(uid,rids);
                return ResultObj.DISPATCH_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ResultObj.DISPATCH_ERROR;
            }
        }
}
