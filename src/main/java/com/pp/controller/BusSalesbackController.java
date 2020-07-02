package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusCustomer;
import com.pp.pojo.BusGoods;
import com.pp.pojo.BusSales;
import com.pp.pojo.BusSalesback;
import com.pp.service.BusCustomerservice;
import com.pp.service.BusGoodsservice;
import com.pp.service.BusSalesbackservice;
import com.pp.vo.SalesbackVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pp
 * @since 2020-06-19
 */
@Controller
@RequestMapping("/busSalesback")
@ResponseBody
public class BusSalesbackController {
    @Autowired
    private BusSalesbackservice salesbackservice;
    @Autowired
    private BusGoodsservice goodsservice;
    @Autowired
    private BusCustomerservice customerservice;
    @RequestMapping("/loadAllSalesback")
    public DataGridView loadAllSalesback(SalesbackVo salesbackVo){
        //查询所有
        IPage<BusSalesback> page = new Page<>(salesbackVo.getPage(), salesbackVo.getLimit());
        QueryWrapper<BusSalesback> wrapper = new QueryWrapper<>();
        wrapper.eq(salesbackVo.getCustomerid() != null && salesbackVo.getCustomerid() != 0, "customerid", salesbackVo.getCustomerid());
        wrapper.eq(salesbackVo.getGoodsid() != null && salesbackVo.getGoodsid() != 0, "goodsid", salesbackVo.getGoodsid());
        wrapper.ge(salesbackVo.getStartTime() != null, "salestime", salesbackVo.getStartTime());
        wrapper.le(salesbackVo.getEndTime() != null, "salestime", salesbackVo.getEndTime());
        wrapper.like(StringUtils.isNotBlank(salesbackVo.getOperateperson()), "operateperson", salesbackVo.getOperateperson());
        wrapper.like(StringUtils.isNotBlank(salesbackVo.getRemark()), "remark", salesbackVo.getRemark());
        salesbackservice.page(page, wrapper);
        List<BusSalesback> records = page.getRecords();
        for (BusSalesback record :
                records
        ) {
            //查询出来这个商品的销售商
            BusCustomer customerByid = customerservice.getById(record.getCustomerid());
            BusGoods goodbyId = goodsservice.getById(record.getGoodsid());
            record.setCustomername(customerByid.getCustomername());
            record.setGoodsname(goodbyId.getGoodsname());
            record.setSize(goodbyId.getSize());
        }

        return new DataGridView(page.getTotal(), records);


    }
    @RequestMapping("DeleteSalesback")
    public ResultObj DeleteSalesback(Integer id,Integer number){
        try {
            BusSalesback salesbackbyId = salesbackservice.getById(id);
            BusGoods goodsbyId = goodsservice.getById(salesbackbyId.getGoodsid());
            goodsbyId.setNumber(goodsbyId.getNumber()-number);
            goodsservice.updateById(goodsbyId);
            salesbackservice.removeById(id);

            return  ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}
