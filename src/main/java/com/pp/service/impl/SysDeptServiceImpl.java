package com.pp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pp.pojo.SysDept;
import com.pp.mapper.SysDeptMapper;
import com.pp.service.SysDeptservice;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pp
 * @since 2020-06-09
 */
@Service
@Transactional
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptservice {
    @Override
    public SysDept getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean update(SysDept entity, Wrapper<SysDept> updateWrapper) {
        return super.update(entity,updateWrapper);
    }

    @Override
    public boolean updateById(SysDept entity) {
        return super.updateById(entity);
    }
    public String cs(String name){
        return "你好";
    }

    @Override
    public boolean save(SysDept entity) {
        return super.save(entity);
    }
}
