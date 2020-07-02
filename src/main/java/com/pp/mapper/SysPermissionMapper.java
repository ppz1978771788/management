package com.pp.mapper;

import com.pp.pojo.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pp
 * @since 2020-06-06
 */
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    @Select("select max(ordernum) from sys_permission where type='permission' ")
    public Integer FindMaxOrderNum();

    void deleteRolePermissionByPid(@Param("id") Serializable id);
}
