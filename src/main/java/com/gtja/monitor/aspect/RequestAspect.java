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

/**
 * 此类为一个切面类，主要作用就是对接口的请求进行拦截
 * 拦截的方式，只需要在指定接口方法上面加上@MonitorRequest注解即可
 *
 * @author guguoyu
 * @since 2018/10/15
 * @version 2.0
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
