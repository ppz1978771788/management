package com.pp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pp.common.Constast;
import com.pp.pojo.SysRole;
import com.pp.service.SysRoleservice;
import com.pp.service.impl.SysDeptServiceImpl;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.aspectj.lang.annotation.Around;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class ManagementApplicationTests {
@Autowired
private SysDeptServiceImpl ss;
@Autowired
private SysRoleservice roleservice;
    @Test
    void contextLoads() {
    }

}
