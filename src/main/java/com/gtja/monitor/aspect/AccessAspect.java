package com.gtja.monitor.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AccessAspect {

    //使用org.slf4j.Logger,这是spring实现日志的方法
    private final static Logger logger = LoggerFactory.getLogger(AccessAspect.class);


    //定义aop扫描路径
    @Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping * *(..))")
    public void log() {

    }

    //记录开始时的日志
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求
        HttpServletRequest request = attributes.getRequest();

        //URL
        logger.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        logger.info("method=" + request.getMethod());
        //ip
        logger.info("ip=" + request.getRemoteAddr());
        //类方法
        logger.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        logger.info("参数=" + joinPoint.getArgs().toString());

    }
}
