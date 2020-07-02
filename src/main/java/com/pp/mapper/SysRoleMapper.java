package com.pp.mapper;

import com.pp.pojo.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pp
 * @since 2020-06-11
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    void deleteRolePermissionByRid(@Param("id") Serializable id);

    void deleteRoleUserByRid(@Param("id")Serializable id);

    List<Integer> queryRolePermissionIdsByRid(@Param("id") Integer roleid);

    void saveRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    List<Integer> queryUserRoleIdsByRid(@Param("id")Integer id);

    void deleteUser_role_byid(@Param("uid")Integer uid);

}
