package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.SysLogLogin;
import com.pp.service.SysLogLoginservice;
import com.pp.vo.LogLoginVo;
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
@RequestMapping("/sysLogLogin")
@ResponseBody
public class SysLogLoginController {
    @Autowired
    private SysLogLoginservice logLoginservice;
    @RequestMapping("/loadAllLogLoin")
    public DataGridView lologLogin(LogLoginVo logLoginVo){
        IPage<SysLogLogin> page = new Page<>(logLoginVo.getPage(),logLoginVo.getLimit());
        QueryWrapper<SysLogLogin> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(logLoginVo.getLoginname()),"loginname",logLoginVo.getLoginname());
        wrapper.like(StringUtils.isNotBlank(logLoginVo.getLoginip()),"loginip",logLoginVo.getLoginip());
        wrapper.ge(logLoginVo.getStartTime()!=null,"logintime",logLoginVo.getStartTime());
        wrapper.le(logLoginVo.getEndTime()!=null,"logintime",logLoginVo.getEndTime());
        IPage<SysLogLogin> loginIPage = logLoginservice.page(page, wrapper);
        return new DataGridView(loginIPage.getTotal(),loginIPage.getRecords());
    }
    //单行删除
    @RequestMapping("/deleteLogLogin")
    public ResultObj deleteLogLogin(Integer id){
        try {
            logLoginservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    @RequestMapping("/batchdeleteLogLogin")
    public ResultObj batchdeleteLogLogin(LogLoginVo logLoginVo){
        try {
            List<Integer> resultList = new ArrayList<>(logLoginVo.getIds().length);
            for (Integer s : logLoginVo.getIds()) {
                resultList.add(s);
            }
            logLoginservice.removeByIds(resultList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}
