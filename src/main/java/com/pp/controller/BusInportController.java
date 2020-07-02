package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusGoods;
import com.pp.pojo.BusInport;
import com.pp.pojo.BusOutport;
import com.pp.pojo.SysUser;
import com.pp.service.BusGoodsservice;
import com.pp.service.BusInportservice;
import com.pp.service.BusProviderservice;
import com.pp.vo.InportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
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
@RequestMapping("/inport")
@ResponseBody
public class BusInportController {
    @Autowired
    private BusInportservice inportservice;
    @Autowired
    BusProviderservice providerservice;
    @Autowired
    BusGoodsservice goodsservice;

    @RequestMapping("/loadAllInport")
    public DataGridView loadAllInport(InportVo inportVo) {
        IPage<BusInport> page = new Page<>(inportVo.getPage(), inportVo.getLimit());
        QueryWrapper<BusInport> wrapper = new QueryWrapper<>();
        wrapper.eq(inportVo.getProviderid() != null && inportVo.getProviderid() != 0, "providerid", inportVo.getProviderid());
        wrapper.eq(inportVo.getGoodsid() != null && inportVo.getGoodsid() != 0, "goodsid", inportVo.getGoodsid());
        wrapper.ge(inportVo.getStartTime() != null, "inporttime", inportVo.getStartTime());
        wrapper.le(inportVo.getEndTime() != null, "inporttime", inportVo.getEndTime());
        wrapper.like(StringUtils.isNotBlank(inportVo.getOperateperson()), "operateperson", inportVo.getOperateperson());
        wrapper.like(StringUtils.isNotBlank(inportVo.getRemark()), "remark", inportVo.getRemark());
        inportservice.page(page, wrapper);
        List<BusInport> records = page.getRecords();
        for (BusInport record :
                records
        ) {
            BusGoods good = goodsservice.getById(record.getProviderid());
            record.setProvidername(providerservice.getById(record.getProviderid()).getProvidername());
            record.setGoodsname(good.getGoodsname());
            record.setSize(good.getSize());
        }
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    //商品进货添加
    @RequestMapping("addInport")
    public ResultObj addInport(InportVo inportVo, HttpSession session) {
        try {
            inportVo.setInporttime(new Date());
            SysUser user = (SysUser) session.getAttribute("user");
            inportVo.setOperateperson(user.getName());
            inportservice.save(inportVo);
            BusInport inportid = inportservice.getById(inportVo.getGoodsid());
            BusGoods goodid = goodsservice.getById(inportid);
            goodid.setNumber(goodid.getNumber()+inportVo.getNumber());
            goodsservice.updateById(goodid);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    @RequestMapping("updateInport")
    public ResultObj updateInport(InportVo inportVo) {
        try {
            inportservice.updateById(inportVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    @RequestMapping("/deleteInport")
    public ResultObj deleteInport(Integer id) {
        try {
            //通过id获取当前的所有信息
            BusInport inport = inportservice.getById(id);
            BusGoods goodid = goodsservice.getById(inport.getGoodsid());
            goodid.setNumber(goodid.getNumber()-inport.getNumber());
            goodsservice.updateById(goodid);
            inportservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


}