package com.gtja.monitor.runner;

import com.gtja.monitor.dto.RequestDataDto;
import com.gtja.monitor.resource.RequestCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 此类实现了{@link ApplicationRunner}
 * 只要工程一启动，就会执行此类下的{@link #run(ApplicationArguments)}方法
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/26
 * @since 1.8
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(RequestCount.class);
    @Autowired
    private RequestCount requestCount;

    /**
     * 此方法是一个消费者线程，是一个无限循环的流程，其逻辑如下：<br>
     * 1.首先从队列中获取实体类对象{@link RequestDataDto requestDataDto}，其包含字段有：请求地址、请求时间戳<br>
     * 2.然后将请求地址、请求时间戳作为入参，调用{@link RequestCount requestCount}的{@link RequestCount#put(String, Long)}方法，将数据存入内存中<br>
     * 3.最后调用{@link RequestCount requestCount}的{@link RequestCount#update(long)}方法，更新最大访问量数据.<br>
     *
     * @param args 就相当于我们main函数的args
     * @throws Exception 从linkedBlockingQueue队列中获取数据可能会出现异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            //从队列中获取数据（如果队列中没有数据则会阻塞）
            RequestDataDto requestDataDto = requestCount.take();
            logger.info("从队列中获取的数据==requestURL:" + requestDataDto.getRequestURL() + "==requestTime:" + requestDataDto.getRequestTimeMillis());
            //将拿出来的数据放入内存requestMap中
            requestCount.put(requestDataDto.getRequestURL(), requestDataDto.getRequestTimeMillis());
            long curTime = System.currentTimeMillis();
            /*计算最大访问量*/
            requestCount.update(curTime);
        }
    }
}
