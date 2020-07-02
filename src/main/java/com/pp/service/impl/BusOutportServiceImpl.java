package com.pp.service.impl;

import com.pp.mapper.BusInportMapper;
import com.pp.pojo.BusInport;
import com.pp.pojo.BusOutport;
import com.pp.mapper.BusOutportMapper;
import com.pp.service.BusInportservice;
import com.pp.service.BusOutportservice;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pp
 * @since 2020-06-15
 */
@Service
public class BusOutportServiceImpl extends ServiceImpl<BusOutportMapper, BusOutport> implements BusOutportservice {
    @Autowired
    private BusInportMapper inportMapper;
/*
* id:退货商品的Id
* number:退货数量
* remark:备注
* */
    @Override
    public void addOutPort(Integer id, Integer number, String remark) {
        BusInport busInport = inportMapper.selectById(id);
        BusOutport busOutport = new BusOutport();
        busOutport.setProviderid(busInport.getProviderid());
        busOutport.setPaytype(busInport.getPaytype());
        busOutport.setOutputtime(new Date());
        busOutport.setOperateperson(busInport.getOperateperson());
        busOutport.setOutportprice(number*busInport.getInportprice());
        System.out.println("---------------------------------------------------");
        System.out.println(number*busInport.getInportprice());
        busOutport.setNumber(number);
        busOutport.setRemark(remark);
        busOutport.setGoodsid(busInport.getGoodsid());
        this.baseMapper.insert(busOutport);

    }
}
