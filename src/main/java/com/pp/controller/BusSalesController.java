package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusCustomer;
import com.pp.pojo.BusGoods;
import com.pp.pojo.BusSales;
import com.pp.service.BusCustomerservice;
import com.pp.service.BusGoodsservice;
import com.pp.service.BusSalesservice;
import com.pp.service.BusProviderservice;
import com.pp.vo.InportVo;
import com.pp.vo.SalesVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/sales")
@ResponseBody
public class BusSalesController {
    @Autowired
    private BusSalesservice salesservice;
    @Autowired
    BusProviderservice providerservice;
    @Autowired
    BusGoodsservice goodsservice;
    @Autowired
    BusCustomerservice customerservice;
    @RequestMapping("/loadAllInport")
    public DataGridView loadAllInport(SalesVo salesVo) {
        IPage<BusSales> page = new Page<>(salesVo.getPage(), salesVo.getLimit());
        QueryWrapper<BusSales> wrapper = new QueryWrapper<>();
        wrapper.eq(salesVo.getCustomerid() != null && salesVo.getCustomerid() != 0, "customerid", salesVo.getCustomerid());
        wrapper.eq(salesVo.getGoodsid() != null && salesVo.getGoodsid() != 0, "goodsid", salesVo.getGoodsid());
        wrapper.ge(salesVo.getStartTime() != null, "salestime", salesVo.getStartTime());
        wrapper.le(salesVo.getEndTime() != null, "salestime", salesVo.getEndTime());
        wrapper.like(StringUtils.isNotBlank(salesVo.getOperateperson()), "operateperson", salesVo.getOperateperson());
        wrapper.like(StringUtils.isNotBlank(salesVo.getRemark()), "remark", salesVo.getRemark());
        salesservice.page(page, wrapper);
        List<BusSales> records = page.getRecords();
        for (BusSales record :
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
    @RequestMapping("DeleteSales")
    public ResultObj DeleteSales(Integer id){
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@id"+id);
            salesservice.removeById(id);

            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}
