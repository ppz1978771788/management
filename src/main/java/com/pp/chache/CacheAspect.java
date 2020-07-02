package com.pp.chache;

import com.pp.pojo.SysDept;
import com.pp.pojo.SysUser;
import com.pp.vo.DeptVo;
import com.pp.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {
    //日志输出
    private Log log= LogFactory.getLog(CacheAspect.class);
    //声明一个缓存容器
    private Map<String,Object> CACHE_CONTAINER=new HashMap<>();
    //声明切面表达式
    private static final String POINTCUT_DEPT_UPDATE="execution(* com.pp.service.impl.SysDeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_GET="execution(* com.pp.service.impl.SysDeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_DELETE="execution(* com.pp.service.impl.SysDeptServiceImpl.removeById(..))";
    private static final String POINTCUT_DEPT_ADD="execution(* com.pp.service.impl.SysDeptServiceImpl.save(..))";
    private static final String CACHE_DEPT_PROFIX="dept:";
/*
* 查询切入
* */
@Around(value = POINTCUT_DEPT_GET)
    public Object chacheDeptGet(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
    Integer object = (Integer) joinPoint.getArgs()[0];
    Object res1 = CACHE_CONTAINER.get(CACHE_DEPT_PROFIX + object);
    System.out.println("---------------------------------------------------------------------------------------------------------------");
    System.out.println("当前取出来的参数"+res1);
    System.out.println(CACHE_CONTAINER);
    System.out.println("---------------------------------------------------------------------------------------------------------------");
    if (res1!=null){
        //证明里面有数据
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("从缓存中找到部门对象");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        return res1;
    }else{
        //里面没有数据
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("从缓存中找到部门对象，去数据库查询");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        SysDept res2=(SysDept) joinPoint.proceed();
        CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+res2.getId(),res2);
        return res2;
    }
}
//更新切入
@Around(value = POINTCUT_DEPT_UPDATE)
public Object chacheDeptupdate(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
    DeptVo deptVo = (DeptVo) joinPoint.getArgs()[0];
    //执行结果
    Boolean isSuccess = (Boolean) joinPoint.proceed();
    if (isSuccess) {
        //成功之后更新缓存
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("部门对象缓存对象已更新");
        SysDept dept = (SysDept) CACHE_CONTAINER.get(CACHE_DEPT_PROFIX + deptVo.getId());
        System.out.println(deptVo.toString().equals(dept.toString()));
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        if (null == dept) {
            dept = new SysDept();
        }
            BeanUtils.copyProperties(deptVo, dept);
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX + dept.getId(), dept);


    }
return  isSuccess;
}
    @Around(value = POINTCUT_DEPT_DELETE)
    public Object chacheDeptdelete(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //执行结果
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //成功之后更新缓存
       CACHE_CONTAINER.remove(CACHE_DEPT_PROFIX+id);
        }
        return  isSuccess;
        //部门添加切入

    }
    @Around(value = POINTCUT_DEPT_ADD)
    public Object chacheDeptadd(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
        SysDept dept = (SysDept) joinPoint.getArgs()[0];
        //执行结果
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //成功之后更新缓存
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+dept.getId(),dept);
        }
        return  isSuccess;
        //部门添加切入

    }
    //声明切面表达式
    private static final String POINTCUT_USER_UPDATE="execution(* com.pp.service.impl.SysUserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_GET="execution(* com.pp.service.impl.SysUserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_DELETE="execution(* com.pp.service.impl.SysUserServiceImpl.removeById(..))";
    private static final String POINTCUT_USER_ADD="execution(* com.pp.service.impl.SysUserServiceImpl.save(..))";
    private static final String CACHE_USER_PROFIX="user:";

    /*
     * 查询切入
     * */
    @Around(value = POINTCUT_USER_GET)
    public Object chacheuserGet(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        Object res1 = CACHE_CONTAINER.get(CACHE_USER_PROFIX + object);
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("当前取出来的参数"+res1);
        System.out.println(CACHE_CONTAINER);
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        if (res1!=null){
            //证明里面有数据
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("从缓存中找到部门了");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            return res1;
        }else{
            //里面没有数据
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("缓存中没有数据，去数据库查询");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            SysUser res2=(SysUser) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_USER_PROFIX+res2.getId(),res2);
            return res2;
        }
    }
    //更新切入
    @Around(value = POINTCUT_USER_UPDATE)
    public Object chacheUSERupdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        SysUser userVo =  (SysUser) joinPoint.getArgs()[0];
        //执行结果
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //成功之后更新缓存
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.println("部门对象已经成功更新");
            SysUser user = (SysUser) CACHE_CONTAINER.get(CACHE_USER_PROFIX + userVo.getId());
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            if (null == user) {
                user = new SysUser();
            }
                BeanUtils.copyProperties(userVo, user);
                CACHE_CONTAINER.put(CACHE_USER_PROFIX + user.getId(), user);


        }
        return  isSuccess;
    }
    @Around(value = POINTCUT_USER_DELETE)
    public Object chacheUSERdelete(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        //执行结果
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //成功之后更新缓存
            CACHE_CONTAINER.remove(CACHE_USER_PROFIX+id);
        }
        return  isSuccess;
        //部门添加切入

    }
    @Around(value = POINTCUT_USER_ADD)
    public Object chacheUSERadd(ProceedingJoinPoint joinPoint) throws Throwable {
//取出第一个参数
        SysUser user = (SysUser) joinPoint.getArgs()[0];
        //执行结果
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            //成功之后更新缓存
            CACHE_CONTAINER.put(CACHE_USER_PROFIX+user.getId(),user);
        }
        return  isSuccess;
        //部门添加切入

    }
}
