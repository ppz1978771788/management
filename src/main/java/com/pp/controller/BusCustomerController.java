package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusCustomer;
import com.pp.pojo.SysUser;
import com.pp.service.BusCustomerservice;
import com.pp.vo.CustomerVo;
import com.pp.vo.CustomerVo;
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
 * @since 2020-06-14
 */
@Controller
@RequestMapping("/busCustomer")
@ResponseBody
public class BusCustomerController {
    @Autowired
    private BusCustomerservice customerservice;
    @RequestMapping("/loadAllCustomer")
    public DataGridView loadAllCustomer(CustomerVo customerVo){
        IPage<BusCustomer> page = new Page<>(customerVo.getPage(),customerVo.getLimit());
        QueryWrapper<BusCustomer> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()),"customername",customerVo.getCustomername());
        wrapper.like(StringUtils.isNotBlank(customerVo.getPhone()),"phone",customerVo.getPhone());
        wrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()),"connectionperson",customerVo.getConnectionperson());
        customerservice.page(page, wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    //添加操作
    @RequestMapping("/addCustomer")
    public ResultObj addCustomer(CustomerVo customerVo){
        try {
            customerservice.save(customerVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    //更新操作
    @RequestMapping("/updateCustomer")
    public ResultObj updateCustomer(CustomerVo customerVo){
        try {
            customerservice.updateById(customerVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //单行删除
    @RequestMapping("/deleteCustomer")
    public  ResultObj deleteCustomer(Integer id){
        try {
            customerservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultObj.DELETE_ERROR;
        }

    }
    //批量删除
    @RequestMapping("/batchDeleteCustomer")
    public ResultObj batchDeleteCustomer(CustomerVo customerVo){
        try {
            List<Integer> resultList = new ArrayList<>(customerVo.getIds().length);
            for (Integer s : customerVo.getIds()) {
                resultList.add(s);
            }
            customerservice.removeByIds(resultList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    //查出所有商品的名称
    @RequestMapping("loadAllCustomerForSelect")
    public DataGridView loadAllCustomerForSelect(){
        List<BusCustomer> list = customerservice.list();
        return new DataGridView(list);
    }
}
