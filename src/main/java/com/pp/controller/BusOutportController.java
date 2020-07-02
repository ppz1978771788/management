package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusGoods;
import com.pp.pojo.BusInport;
import com.pp.pojo.BusOutport;
import com.pp.pojo.BusProvider;
import com.pp.service.BusGoodsservice;
import com.pp.service.BusOutportservice;
import com.pp.service.BusProviderservice;
import com.pp.vo.OutportVo;
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
 * @since 2020-06-15
 */
@Controller
@RequestMapping("/outport")
@ResponseBody
public class BusOutportController {
    @Autowired
    private BusOutportservice outportservice;
    @Autowired
    private BusGoodsservice goodsservice;
    @Autowired
    private BusProviderservice providerservice;
    @RequestMapping("/loadAllOutport")
    public DataGridView loadAllOutport(OutportVo outportVo){
        IPage<BusOutport> page = new Page<>(outportVo.getPage(), outportVo.getLimit());
        QueryWrapper<BusOutport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(outportVo.getProviderid()!=null&&outportVo.getProviderid()!=0,"providerid",outportVo.getProviderid());
        queryWrapper.eq(outportVo.getGoodsid()!=null&&outportVo.getGoodsid()!=0,"goodsid",outportVo.getGoodsid());
        queryWrapper.ge(outportVo.getStartTime()!=null, "outputtime", outportVo.getStartTime());
        queryWrapper.le(outportVo.getEndTime()!=null, "outputtime", outportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()), "operateperson", outportVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()), "remark", outportVo.getRemark());
        queryWrapper.orderByDesc("outputtime");
        this.outportservice.page(page, queryWrapper);
        List<BusOutport> records = page.getRecords();
        for (BusOutport outport : records) {
            BusProvider provider = this.providerservice.getById(outport.getProviderid());
            if(null!=provider) {
                outport.setProvidername(provider.getProvidername());
            }
            BusGoods goods = this.goodsservice.getById(outport.getGoodsid());
            if(null!=goods) {
                outport.setGoodsname(goods.getGoodsname());
                outport.setSize(goods.getSize());
            }
        }
        return new DataGridView(page.getTotal(), records);
    }
    @RequestMapping("/addOutport")
    public ResultObj addOutport(Integer id,Integer number,String remark){
        try {
            outportservice.addOutPort(id,number,remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }
    @RequestMapping("executeOuport")
    public ResultObj executeOuport(Integer id,Integer number){
        try {
            //更改数量
            BusOutport inportid = outportservice.getById(id);
            BusGoods goodid = goodsservice.getById(inportid);
            goodid.setNumber(goodid.getNumber()-number);
            goodsservice.updateById(goodid);
            //进行单行删除
            outportservice.removeById(id);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

}
