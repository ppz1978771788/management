package com.pp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.*;
import com.pp.mapper.SysPermissionMapper;
import com.pp.pojo.SysDept;
import com.pp.pojo.SysPermission;
import com.pp.pojo.SysUser;
import com.pp.service.SysPermissionservice;
import com.pp.service.SysRoleservice;
import com.pp.service.SysUserservice;
import com.pp.vo.DeptVo;
import com.pp.vo.PermissionVo;
import com.zaxxer.hikari.util.DriverDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.reflect.generics.tree.Tree;

import javax.jnlp.PersistenceService;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/*
* 菜单管理控制器
* */
@Controller
@RequestMapping("/menu")
@ResponseBody
public class MenuController {
    @Autowired
    private SysPermissionservice permissionservice;
    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private SysUserservice userservice;
    @Autowired
    private SysRoleservice roleservice;
    @RequestMapping("/loadIndexLeftMenuJosn")
    public DataGridView loadIndexLeftMenuJosn(PermissionVo permissionVo, HttpSession session){
            //查询所有菜单
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("type","menu").eq("available",1);
        SysUser user = (SysUser) session.getAttribute("user");
        List<SysPermission> list;
        if (user.getType()== Constast.USER_TYPE_SUPPER){
            //线行的json类型
             list = permissionservice.list(wrapper);
        }else {
            //根据用户的ID +角色+权限查询出应该显示的菜单页面
            Integer id = user.getId();
            List<Integer> Rids = roleservice.queryUserRoleIdsByRid(id);
            HashSet<Integer> pids = new HashSet<>();
                for (Integer rid :
                        Rids) {
                    List<Integer> Permissions = roleservice.queryRolePermissionIdsByRid(rid);
                  pids.addAll(Permissions);
                }
           if (pids!=null&&pids.size()>0){
               //有权限
               wrapper.in("id",pids);
               list = permissionservice.list(wrapper);
           }else {
               list = new ArrayList<>();
           }
        }
        if (list!=null&&list.size()>0) {
            //将没有层级关系的json数据变成有层级关系的数据
            List<TreeNode> treeNodes = new ArrayList<>();
            for (SysPermission p :
                    list) {
                Integer id = p.getId();
                Integer pid = p.getPid();
                String title = p.getTitle();
                String icon = p.getIcon();
                String href = p.getHref();
                Boolean spread = p.getOpen() == 1 ? true : false;
                treeNodes.add(new TreeNode(id, pid, title, icon, href, spread));

            }
            List<TreeNode> list2 = TreeNodeBuider.build(treeNodes, 1);
            return new DataGridView(list2);
        }
        else {
            return new DataGridView(list);
        }
    }
//菜单管理开始
@RequestMapping("/loadMenuManagerLeftTreeJson")
public DataGridView loadMenuManagerLeftTreeJson(){
    //查询树
    QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
    wrapper.eq("type",Constast.TYPE_MNEU);
    List<SysPermission> list = permissionservice.list(wrapper);
    List<TreeNode> treenodes =new ArrayList<>();
    for (SysPermission l :
            list) {
        treenodes.add(new TreeNode(l.getId(),l.getPid(),l.getTitle(),l.getOpen()==1?true:false));
    }
    System.out.println("-----------------------------");
    return new DataGridView(treenodes);
}
    @RequestMapping("/loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo){
        IPage<SysPermission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq(permissionVo.getId()!=null, "id", permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid", permissionVo.getId());
        wrapper.eq("type", Constast.TYPE_MNEU);//只能查询菜单
        wrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        wrapper.orderByAsc("ordernum");
        permissionservice.page(page, wrapper);
        System.out.println("----------------------------------");
        System.out.println("这里执行了");
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    @RequestMapping("/loadMenuMaxOrderNum")
    public Integer loadMenuMaxOrderNum(){
        return permissionMapper.FindMaxOrderNum()+1;
    }
    @RequestMapping("/addMenu")
    public ResultObj addMenu(PermissionVo permissionVo){
        try {
            permissionVo.setType(Constast.TYPE_MNEU);
            permissionservice.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    @RequestMapping("/updateMenu")
    public ResultObj updateMenu(PermissionVo permissionVo){
        try {
            permissionservice.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //查询当前部门有没有子部门
    @RequestMapping("checkMenuHasChildrenNode")
    public Boolean checkMenuHasChildrenNode(PermissionVo PermissionVo){
        //根据Id判断
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("type","menu");
        wrapper.eq("pid",PermissionVo.getId());
        List<SysPermission> list = permissionservice.list(wrapper);
        if (list.size()>0){
            //存在子节点
            return true;
        }
        else {
            //不存在子结点
            return  false;
        }
    }
    @RequestMapping("deleteMenu")
    public ResultObj deleteMenu(Integer id){
        try {
            permissionservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }


    }


    //菜单管理结束






}
