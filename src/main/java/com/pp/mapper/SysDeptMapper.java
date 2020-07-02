package com.pp.mapper;

import com.pp.pojo.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pp
 * @since 2020-06-09
 */@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {
     @Select("select max(ordernum) from sys_dept  ")
 public Integer FindMaxOrderNum();
}
