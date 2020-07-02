package com.pp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pp.pojo.SysUser;
import com.pp.mapper.SysUserMapper;
import com.pp.service.SysRoleservice;
import com.pp.service.SysUserservice;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pp
 * @since 2020-06-04
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserservice {
    @Autowired
    private SysRoleservice roleservice;
    @Override
    public SysUser getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean updateById(SysUser entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean save(SysUser entity) {
        return super.save(entity);
    }

    @Override
    public Integer loadUserMaxOrderNum() {
        return this.baseMapper.loadUserMaxOrderNum();
    }

    @Override
    public void saveUserRole(Integer uid, Integer[] rids) {
        //把之前的权限先删掉
        roleservice.deleteUser_role_byid(uid);
        //通过ids将用户新的权限保存
        if (rids!=null&&rids.length>0){
            for (Integer rid :
                    rids) {
                this.getBaseMapper().insertUserRole(uid,rid);
            }
        }
    }
}
