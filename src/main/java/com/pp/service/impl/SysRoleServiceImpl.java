package com.pp.service.impl;

import com.pp.pojo.SysRole;
import com.pp.mapper.SysRoleMapper;
import com.pp.service.SysRoleservice;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pp
 * @since 2020-06-11
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleservice {
    @Override
    public boolean removeById(Serializable id) {
        this.baseMapper.deleteRolePermissionByRid(id);
        this.baseMapper.deleteRoleUserByRid(id);
        return super.removeById(id);
    }

    @Override
    public List<Integer> queryRolePermissionIdsByRid(Integer roleid) {
        return this.baseMapper.queryRolePermissionIdsByRid(roleid);
    }

    @Override
    public void saveRolePermission(Integer rid, Integer[] ids) {
        //根据获得的rid删除所有的权限，避免下次添加的时候重复

        SysRoleMapper baseMapper = this.getBaseMapper();
        baseMapper.deleteRolePermissionByRid(rid);
        for (Integer pid :ids
                ) {
            baseMapper.saveRolePermission(rid,pid);
        }

    }

    @Override
    public List<Integer> queryUserRoleIdsByRid(Integer id) {
        return this.baseMapper.queryUserRoleIdsByRid(id);
    }

    @Override
    public void deleteUser_role_byid(Integer uid) {
        this.baseMapper.deleteUser_role_byid(uid);
    }


}
