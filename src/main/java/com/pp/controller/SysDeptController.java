package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.common.TreeNode;
import com.pp.mapper.SysDeptMapper;
import com.pp.pojo.SysDept;
import com.pp.service.SysDeptservice;
import com.pp.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * @since 2020-06-09
 */
@Controller
@RequestMapping("/sysDept")
@ResponseBody
public class SysDeptController {
    @Autowired
    private SysDeptservice deptservice;
    @Autowired
    private SysDeptMapper deptMapper;
    @RequestMapping("/loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(){
        //查询树
        List<SysDept> list = deptservice.list();
        List<TreeNode> treenodes =new ArrayList<>();
        for (SysDept l :
                list) {
            treenodes.add(new TreeNode(l.getId(),l.getPid(),l.getTitle(),l.getOpen()==1?true:false));
        }
        return new DataGridView(treenodes);
    }
    @RequestMapping("/loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo){
        IPage<SysDept> page = new Page<>(deptVo.getPage(), deptVo.getLimit());
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),"title",deptVo.getTitle());
        wrapper.like(StringUtils.isNotBlank(deptVo.getAddress()),"address",deptVo.getAddress());
        wrapper.like(StringUtils.isNotBlank(deptVo.getRemark()),"remark",deptVo.getRemark());
        wrapper.eq(deptVo.getId()!=null,"id",deptVo.getId()).or().eq(deptVo.getId()!=null,"pid",deptVo.getId());
        deptservice.page(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    @RequestMapping("/loadDeptMaxOrderNum")
    public Integer loadDeptMaxOrderNum(){
        return deptMapper.FindMaxOrderNum()+1;
    }
    @RequestMapping("addDept")
    public ResultObj addDept(DeptVo deptVo){
        try {
            deptVo.setCreatetime(new Date());
            deptservice.save(deptVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    @RequestMapping("updateDept")
    public ResultObj updateDept(DeptVo deptVo){
        try {
            deptservice.updateById(deptVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //查询当前部门有没有子部门
    @RequestMapping("checkDeptHasChildrenNode")
    public Boolean checkDeptHasChildrenNode(DeptVo deptVo){
        //根据Id判断
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",deptVo.getId());
        List<SysDept> list = deptservice.list(wrapper);
        if (list.size()>0){
            //存在子节点
            return true;
        }
        else {
            //不存在子结点
            return  false;
        }
    }
    @RequestMapping("deleteDept")
    public ResultObj deleteDept(Integer id){
        try {
            System.out.println("-----------------------------------------------------"+id);
            deptservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }


    }
}
