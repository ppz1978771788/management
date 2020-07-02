package com.pp.config;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pp.common.ActiverUser;
import com.pp.common.Constast;
import com.pp.pojo.SysPermission;
import com.pp.pojo.SysRole;
import com.pp.pojo.SysUser;
import com.pp.service.SysPermissionservice;
import com.pp.service.SysRoleservice;
import com.pp.service.SysUserservice;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//自定义UserRealm
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private SysUserservice userservice;
    @Autowired
    @Lazy
    private SysRoleservice roleservice;
    @Autowired
    @Lazy
    private SysPermissionservice permissionservice;
//授权和认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前的用户进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        ActiverUser activerUser = (ActiverUser) principalCollection.getPrimaryPrincipal();
        SysUser user = activerUser.getUser();
        List<String> permissions = activerUser.getPermissions();
        List<String> roles = activerUser.getRoles();
        if (user.getType()==Constast.USER_TYPE_SUPPER){
            info.addStringPermission("*:*");
        }
        else
        {
            //普通用户
            if (null!=permissions&&permissions.size()>0){
                info.addStringPermissions(permissions);
            }else {
                info.addStringPermissions(new ArrayList<>());
            }
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //用户认证
        //获取当前用户
        //封装用户的登录数据
//因为是全局的关系，所以存在整个生命周期
        UsernamePasswordToken usertoken=(UsernamePasswordToken)token;
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        //token.getPrincipal() 得到用户名
        wrapper.eq("loginname",(token.getPrincipal().toString()));
        SysUser user = userservice.getOne(wrapper);
        //判断用户名是否存在
        if (null!=user){
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
            List<String> roles = activerUser.getRoles();
            List<String> permissionsAll =new ArrayList<>();
            //根据当前用户的ID设置当前角色的所有角色和权限
            QueryWrapper<SysPermission> role_permission_wrapper = new QueryWrapper<>();
            role_permission_wrapper.eq("type", Constast.TYPE_PERMISSION);
            role_permission_wrapper.eq("available",Constast.AVAILABLE_TRUE);
            Integer userId = user.getId();
            List<Integer> rids = roleservice.queryUserRoleIdsByRid(userId);
            HashSet<Integer> pids = new HashSet<>();
            for (Integer rid :
                    rids) {
//                SysRole role = roleservice.getById(rid);
//                roles.add(role.getName());
                List<Integer> permissions = roleservice.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissions);
            }
            //此时的pids保存的都是该用户的所有权限
            if (pids!=null&&pids.size()>0){
                //证明此用户是有权限的，根据当前的pid查出code
                role_permission_wrapper.in("id",pids);
                List<SysPermission>   ps = permissionservice.list(role_permission_wrapper);
                for (SysPermission p:
                     ps) {
                    permissionsAll.add(p.getPercode());
                }
            }
            activerUser.setPermissions(permissionsAll);
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(activerUser,user.getPwd(),credentialsSalt,this.getName());
            return info;
        }
         return  null;//shiro底层就会抛出UnknowAccountExceptin


    }
}
