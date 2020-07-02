package com.pp.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.Constast;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.BusProvider;
import com.pp.service.BusProviderservice;
import com.pp.vo.ProviderVo;
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
@RequestMapping("/busProvider")
@ResponseBody
public class BusProviderController {
    /*
    * 供应商的增删改查
    * */

    @Autowired
    private BusProviderservice providerservice;
    @RequestMapping("/loadAllProvider")
    public DataGridView loadAllProvider(ProviderVo providerVo){
        IPage<BusProvider> page = new Page<>(providerVo.getPage(),providerVo.getLimit());
        QueryWrapper<BusProvider> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()),"providername",providerVo.getProvidername());
        wrapper.like(StringUtils.isNotBlank(providerVo.getPhone()),"phone",providerVo.getPhone());
        wrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()),"connectionperson",providerVo.getConnectionperson());
        providerservice.page(page,wrapper);
        System.out.println(page.getRecords().toString());
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    //添加操作
    @RequestMapping("/addProvider")
    public ResultObj addProvider(ProviderVo providerVo){
        try {
            providerservice.save(providerVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    //更新操作
    @RequestMapping("/updateProvider")
    public ResultObj updateProvider(ProviderVo providerVo){
        try {
            providerservice.updateById(providerVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //单行删除
    @RequestMapping("/deleteProvider")
    public  ResultObj deleteProvider(Integer id){
        try {
            providerservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultObj.DELETE_ERROR;
        }

    }
    //批量删除
    @RequestMapping("/batchDeleteProvider")
    public ResultObj batchDeleteProvider(ProviderVo providerVo){
        try {
            List<Integer> resultList = new ArrayList<>(providerVo.getIds().length);
            for (Integer s : providerVo.getIds()) {
                resultList.add(s);
            }
            providerservice.removeByIds(resultList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    //查找所有的供应商
@RequestMapping("loadAllProviderForSelect")
    public DataGridView loadAllProviderForSelect(){
    QueryWrapper<BusProvider> wrapper = new QueryWrapper<>();
    wrapper.eq("available", Constast.AVAILABLE_TRUE);
    List<BusProvider> list = providerservice.list(wrapper);
    return new DataGridView(list);
}

}
