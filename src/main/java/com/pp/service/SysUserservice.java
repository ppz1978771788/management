package com.pp.service;

import com.pp.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pp
 * @since 2020-06-04
 */
public interface SysUserservice extends IService<SysUser> {
    Integer loadUserMaxOrderNum();

    void saveUserRole(Integer uid, Integer[] rids);
}
