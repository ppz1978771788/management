package com.pp.mapper;

import com.pp.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pp
 * @since 2020-06-04
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    Integer loadUserMaxOrderNum();

    void insertUserRole(@Param("uid") Integer uid,@Param("rid") Integer rid);
}
