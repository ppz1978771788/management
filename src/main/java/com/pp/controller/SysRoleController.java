package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.common.TreeNode;
import com.pp.pojo.SysPermission;
import com.pp.pojo.SysRole;
import com.pp.pojo.SysUser;
import com.pp.service.SysPermissionservice;
import com.pp.service.SysRoleservice;
import com.pp.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pp
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/sysRole")
@ResponseBody
public class SysRoleController {
    @Autowired
    private SysRoleservice roleservice;
    @Autowired
    private SysPermissionservice permissionservice;
    @RequestMapping("/loadAllRole")
    public DataGridView loadAllRole(RoleVo roleVo){
        IPage<SysRole> page = new Page<>(roleVo.getPage(),roleVo.getLimit());
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName());
        wrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark());
        wrapper.eq(roleVo.getAvailable()!=null,"available",roleVo.getAvailable());
        roleservice.page(page, wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    //添加操作
    @RequestMapping("/addRole")
    public ResultObj addRole(RoleVo RoleVo){
        try {
            RoleVo.setCreatetime(new Date());
            roleservice.save(RoleVo);
            System.out.println(ResultObj.ADD_SUCCESS.toString());
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    //更新操作
    @RequestMapping("/updateRole")
    public ResultObj updateRole(RoleVo RoleVo, HttpSession session){
        try {
            roleservice.updateById(RoleVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //单行删除
    @RequestMapping("/deleteRole")
    public  ResultObj deleteRole(Integer id){
        try {
            roleservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultObj.DELETE_ERROR;
        }

    }
    //查询出来当前用户所拥有的权限
    @RequestMapping("initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer roleId){
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<SysPermission> allPermissions = permissionservice.list(wrapper);
        //跟当前ID查询当前角色拥有的所有权限
        List<Integer> currentRolePermissions=roleservice.queryRolePermissionIdsByRid(roleId);
        List<SysPermission> currentPermissions=null;
        if (currentRolePermissions.size()>0){
            wrapper.in("id",currentRolePermissions);
           currentPermissions= permissionservice.list(wrapper);
        }else{
            currentPermissions=new ArrayList<>();
        }

        List<TreeNode> nodes =new ArrayList<>();
        for (SysPermission p1:allPermissions
             ) {
            String checkArr="0";
            for (SysPermission p2 :
                    currentPermissions) {
                if (p1.getId() == p2.getId()) {
                    //将这个checkArr
                    checkArr="1";
                }
            }
            nodes.add(new TreeNode(p1.getId(),p1.getPid(),p1.getTitle(),p1.getOpen()==null||p1.getOpen()==0?true:false,checkArr));
        }
        System.out.println("nodes的信息为"+nodes.toString());
        return  new DataGridView(nodes);
    }
    @RequestMapping("/saveRolePermission")
    public ResultObj saveRolePermission(Integer rid,Integer[] ids){
        try {
            roleservice.saveRolePermission(rid,ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;

        }

    }
}
