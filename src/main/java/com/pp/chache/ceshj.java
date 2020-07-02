package com.pp.chache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class ceshj {
    @Around("execution(* com.pp.service.impl.SysDeptServiceImpl.cs(..))")
    public Object ceshi(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("前置通知");
        String args = (String)joinPoint.getArgs()[0];
        System.out.println("这里是获得第一个参数的"+args);
        Object proceed = joinPoint.proceed();
        System.out.println("这里是放行的通知得到的结果"+proceed);
            return "我很好";
    }

}
