package com.pp.common;

import com.pp.pojo.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {
    private SysUser user;
    private List<String> roles;
    private List<String> permissions;
}
