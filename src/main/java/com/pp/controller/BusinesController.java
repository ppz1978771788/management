package com.pp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 账户管理
* */
@Controller
@RequestMapping("/bus")
public class BusinesController {
    @RequestMapping("/toCustomerManager")
    public String toCustomerManager(){
        return "business/customer/customerManager";
    }
    @RequestMapping("/toProviderManager")
    public String toProviderManager(){
        return "business/provider/providerManager";
    }
    @RequestMapping("/toGoodsManager")
    public String toGoodsManager(){
        return "business/Goods/GoodsManager";
    }
    @RequestMapping("/toInportManager")
    public String toInportManager(){
        return "business/inport/InportManager";
    }
    @RequestMapping("toOutportManager")
    public String toOutportManager() {
        return "business/outport/outportManager";
    }
    @RequestMapping("toSalesManager")
    public String toSalesManage() {
        return "business/sales/salesManager";
    }
    @RequestMapping("toSalesbackManager")
    public String toSalesbackManager() {
        return "business/salesback/salesbackManager";
    }
}
