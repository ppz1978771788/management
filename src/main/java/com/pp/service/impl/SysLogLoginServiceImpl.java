package com.pp.service.impl;

import com.pp.pojo.SysLogLogin;
import com.pp.mapper.SysLogLoginMapper;
import com.pp.service.SysLogLoginservice;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements SysLogLoginservice {

}
