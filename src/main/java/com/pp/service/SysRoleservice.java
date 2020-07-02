package com.pp.service;

import com.pp.pojo.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pp
 * @since 2020-06-11
 */
public interface SysRoleservice extends IService<SysRole> {

    List<Integer> queryRolePermissionIdsByRid(Integer roleid);
//保存角色和菜单权限之间的关系
    void saveRolePermission(Integer rid, Integer[] ids);

    List<Integer> queryUserRoleIdsByRid(Integer id);

    void deleteUser_role_byid(Integer uid);

}
