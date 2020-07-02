package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.common.TreeNode;
import com.pp.mapper.SysPermissionMapper;
import com.pp.pojo.SysPermission;
import com.pp.service.SysPermissionservice;
import com.pp.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pp
 * @since 2020-06-06
 */
@Controller
@RequestMapping("/sysPermission")
@ResponseBody
public class SysPermissionController {
    @Autowired
    private SysPermissionservice permissionservice;
    @Autowired
    private SysPermissionMapper permissionMapper;
    //权限管理开始
    @RequestMapping("/loadPermissionManagerLeftTreeJson")
    public DataGridView loadPermissionManagerLeftTreeJson(){
        //查询树
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("type", Constast.TYPE_MNEU);
        List<SysPermission> list = permissionservice.list(wrapper);
        List<TreeNode> treenodes =new ArrayList<>();
        for (SysPermission l :
                list) {
            treenodes.add(new TreeNode(l.getId(),l.getPid(),l.getTitle(),l.getOpen()==1?true:false));
        }
        return new DataGridView(treenodes);
    }
    @RequestMapping("/loadAllPermission")
    public DataGridView loadAllPermission(PermissionVo permissionVo){
        IPage<SysPermission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq(permissionVo.getTitle()!=null, "id", permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid", permissionVo.getId());
        wrapper.eq("type", Constast.TYPE_PERMISSION);//只能查询权限
        wrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        wrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "title", permissionVo.getPercode());

        wrapper.orderByAsc("ordernum");
        permissionservice.page(page, wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    @RequestMapping("/loadPermissionMaxOrderNum")
    public Integer loadPermissionMaxOrderNum(){
        return permissionMapper.FindMaxOrderNum()+1;
    }
    @RequestMapping("/addPermission")
    public ResultObj addPermission(PermissionVo permissionVo){
        try {
            permissionVo.setType(Constast.TYPE_PERMISSION);
            permissionservice.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    @RequestMapping("/updatePermission")
    public ResultObj updatePermission(PermissionVo permissionVo){
        try {
            permissionservice.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    @RequestMapping("deletePermission")
    public ResultObj deletePermission(Integer id){
        try {
            permissionservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    //权限管理结束


}
