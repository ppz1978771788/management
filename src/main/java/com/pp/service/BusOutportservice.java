package com.pp.service;

import com.pp.pojo.BusOutport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pp
 * @since 2020-06-15
 */
public interface BusOutportservice extends IService<BusOutport> {

    void addOutPort(Integer id, Integer number, String remark);
}
