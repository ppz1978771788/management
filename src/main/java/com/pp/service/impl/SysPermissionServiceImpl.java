package com.pp.service.impl;

import com.pp.pojo.SysPermission;
import com.pp.mapper.SysPermissionMapper;
import com.pp.service.SysPermissionservice;
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
 * @since 2020-06-06
 */
@Service
@Transactional
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionservice {
    @Override
    public boolean removeById(Serializable id) {
        //根据权限或菜单ID删除权限表和角色关系表里面的数据
        SysPermissionMapper mapper = this.getBaseMapper();
        mapper.deleteRolePermissionByPid(id);
        return super.removeById(id);
    }
}
