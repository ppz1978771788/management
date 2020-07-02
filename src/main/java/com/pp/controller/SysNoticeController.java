package com.pp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.common.DataGridView;
import com.pp.common.ResultObj;
import com.pp.pojo.SysNotice;
import com.pp.pojo.SysUser;
import com.pp.service.SysNoticeservice;
import com.pp.vo.NoticeVo;
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
 * @since 2020-06-08
 */
@Controller
@RequestMapping("/sysNotice")
@ResponseBody
public class SysNoticeController {
    @Autowired
    private SysNoticeservice noticeservice;
    @RequestMapping("/loadAllNotice")
    public DataGridView loadAllNotice(NoticeVo noticeVo){
        IPage<SysNotice> page = new Page<>(noticeVo.getPage(),noticeVo.getLimit());
        QueryWrapper<SysNotice> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        wrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        wrapper.ge(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        wrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());
        noticeservice.page(page, wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
    //添加操作
    @RequestMapping("/addNotice")
    public ResultObj addNotice(NoticeVo noticeVo, HttpSession session){
        try {
            noticeVo.setCreatetime(new Date());
            SysUser user = (SysUser) session.getAttribute("user");
            noticeVo.setOpername(user.getLoginname());
            noticeservice.save(noticeVo);

            System.out.println(ResultObj.ADD_SUCCESS.toString());
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    //更新操作
    @RequestMapping("/updateNotice")
    public ResultObj updateNotice(NoticeVo noticeVo, HttpSession session){
        try {
            noticeVo.setCreatetime(new Date());
            noticeservice.updateById(noticeVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    //单行删除
    @RequestMapping("/deleteNotice")
    public  ResultObj deleteNotice(Integer id){
        try {
            noticeservice.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultObj.DELETE_ERROR;
        }

    }
    //批量删除
    @RequestMapping("/batchDeleteNotice")
    public ResultObj batchDeleteNotice(NoticeVo noticeVo){
        try {
            List<Integer> resultList = new ArrayList<>(noticeVo.getIds().length);
            for (Integer s : noticeVo.getIds()) {
                resultList.add(s);
            }
            noticeservice.removeByIds(resultList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}
