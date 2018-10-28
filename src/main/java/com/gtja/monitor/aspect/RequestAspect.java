package com.gtja.monitor.aspect;


import com.gtja.monitor.resource.RequestCount;
import com.gtja.monitor.thread.ProducerThread;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 此类为一个切面类，主要作用就是对接口的请求进行拦截
 * 拦截的方式，只需要在指定接口方法上面加上@MonitorRequest注解即可
 *
 * @author guguoyu
 * @since 2018/10/15
 * @version 1.0
 */
@Aspect
@Component
public class RequestAspect {

    @Autowired
    private RequestCount requestCount;

    //使用org.slf4j.Logger,这是spring实现日志的方法
    private final static Logger logger = LoggerFactory.getLogger(RequestAspect.class);


    /**
     * 表示在执行被@MonitorRequest注解修饰的方法之前 会执行doBefore()方法
     *
     * @param joinPoint 连接点，就是被拦截点
     */
    @Before(value = "@annotation(com.gtja.monitor.annotation.MonitorRequest)")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        HttpServletRequest request = attributes.getRequest();

        //URL：根据请求对象拿到访问的地址
        logger.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        logger.info("method=" + request.getMethod());
        //ip：获取到访问
        logger.info("ip=" + request.getRemoteAddr());
        //获取被拦截的类名和方法名
        logger.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        logger.info("参数=" + joinPoint.getArgs().toString());


        //获取当前请求的毫秒值
        long currentTimeMillis = System.currentTimeMillis();
        //获取接口的请求地址
        String requestURL = request.getRequestURL().toString();
        logger.info("请求地址URL:" + requestURL + ",请求的时间戳:" + currentTimeMillis);
        //新建一个线程，并且启动
        ProducerThread producerThread = new ProducerThread(requestURL, currentTimeMillis, requestCount);
        new Thread(producerThread).start();


    }
}
