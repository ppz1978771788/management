package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.AppFileUtils;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusGoods;
import com.pp.pojo.BusProvider;
import com.pp.service.BusGoodsservice;
import com.pp.service.BusProviderservice;
import com.pp.vo.GoodsVo;
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
 * @since 2020-06-14
 */
@Controller
@RequestMapping("/goods")
@ResponseBody
public class BusGoodsController {
    /*
     * 供应商的增删改查
     * */

    @Autowired
    private BusGoodsservice goodsservice;
    @Autowired
    private BusProviderservice providerservice;
    @RequestMapping("/loadAllGoods")
    public DataGridView loadAllGoods(GoodsVo goodsVo){
        IPage<BusGoods> page = new Page<>(goodsVo.getPage(),goodsVo.getLimit());
        QueryWrapper<BusGoods> wrapper = new QueryWrapper<>();
        wrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
        wrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()),"goodsname",goodsVo.getGoodsname());
        wrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()),"productcode",goodsVo.getProductcode());
        wrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()),"Promitcode",goodsVo.getPromitcode());
        wrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()),"description",goodsVo.getDescription());
        wrapper.like(StringUtils.isNotBlank(goodsVo.getSize()),"size",goodsVo.getSize());
        goodsservice.page(page,wrapper);
        List<BusGoods> records = page.getRecords();
        for (BusGoods record:
                records
             ) {

            BusProvider byId = providerservice.getById(record.getProviderid());
            if (null != byId) {
                record.setProvidername(byId.getProvidername());
            }
        }
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    //添加操作
    @RequestMapping("/addGoods")
    public ResultObj addGoods(GoodsVo goodsVo){
        try {
            if(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().endsWith("_temp")) {
                String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
                goodsVo.setGoodsimg(newName);
            }
            goodsservice.save(goodsVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    //更新操作
    @RequestMapping("/updateGoods")
    public ResultObj updateGoods(GoodsVo goodsVo){
        try {
            //说明是不默认图片
            if(!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG))) {
                if(goodsVo.getGoodsimg().endsWith("_temp")) {
                    String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
                    goodsVo.setGoodsimg(newName);
                    //删除原先的图片s
                    String oldPath=this.goodsservice.getById(goodsVo.getId()).getGoodsimg();
                    AppFileUtils.removeFileByPath(oldPath);
                }
            }
            goodsservice.updateById(goodsVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //单行删除
    @RequestMapping("/deleteGoods")
    public  ResultObj deleteGoods(Integer id){
        try {
            goodsservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultObj.DELETE_ERROR;
        }

    }
    //加载所有的商品
    @RequestMapping("/loadAllGoodsForSelect")
    public DataGridView loadAllGoodsForSelect(){
        List<BusGoods> list = goodsservice.list();
        for (BusGoods l:
                list
             ) {
            l.setProvidername(providerservice.getById(l.getProviderid()).getProvidername());
        }
        return new DataGridView(list);
    }
    @RequestMapping("loadGoodsByProviderId")
    public DataGridView loadGoodsByProviderId(Integer id){
        //根据Id 获取当前供应商下的所有商品
        QueryWrapper<BusGoods> wrapper=new QueryWrapper<>();
        List<BusGoods> list = goodsservice.list(wrapper);
        return new DataGridView(list);

    }
}
