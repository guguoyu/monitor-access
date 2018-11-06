package com.gtja.monitor.aspect;


import com.gtja.monitor.dto.RequestDataDto;
import com.gtja.monitor.resource.RequestCount;
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
import java.util.concurrent.BlockingDeque;

import com.gtja.monitor.annotation.MonitorRequest;

/**
 * 此类为一个切面类，主要作用就是对接口的请求进行拦截
 * 拦截的方式，只需要在指定接口方法上面加上{@link MonitorRequest}注解即可
 *
 * @author guguoyu
 * @version 2.0
 * @since 2018/10/15
 * @since 1.8
 */
@Aspect//加上次注解，意思是说此类为一个切面类
@Component
public class RequestAspect {

    @Autowired
    private RequestCount requestCount;

    //使用org.slf4j.Logger,这是spring实现日志的方法
    private final static Logger logger = LoggerFactory.getLogger(RequestAspect.class);

    /**
     * 当调用接口（被{@link MonitorRequest}注解修饰）的时候，会先执行{@link #doBefore(JoinPoint)}方法.<br>
     * 此方法的逻辑：<br>
     * 1.先获取请求地址和当前系统的时间戳<br>
     * 2.将请求地址和系统时间戳封装到{@link RequestDataDto requestDataDto}中<br>
     * 3.将封装好的数据放入到{@link RequestCount#linkedBlockingDeque}队列中
     *
     * @param joinPoint 连接点，就是被拦截点
     */
    @Before(value = "@annotation(com.gtja.monitor.annotation.MonitorRequest)")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        HttpServletRequest request = attributes.getRequest();
        //获取当前请求的毫秒值
        long requestTimeMillis = System.currentTimeMillis();
        //获取接口的请求地址
        String requestURL = request.getRequestURL().toString();
        logger.info("请求地址URL:" + requestURL + ",请求的时间戳:" + requestTimeMillis);
        //将请求地址和请求时间存入实体类中
        RequestDataDto requestDataDto = new RequestDataDto(requestURL, requestTimeMillis);
        //获取阻塞队列
        BlockingDeque<RequestDataDto> linkedBlockingDeque = requestCount.getLinkedBlockingDeque();
        try {
            //向队列中存放数据
            linkedBlockingDeque.put(requestDataDto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
