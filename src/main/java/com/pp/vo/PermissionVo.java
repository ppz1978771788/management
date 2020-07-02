package com.pp.vo;

import com.pp.pojo.SysPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends SysPermission {
    private static final long serialVersionUID = 1L;
    private Integer limit=10;
    private Long count;
    private Integer page=1;

}
