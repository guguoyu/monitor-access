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
 * 此类实现了ApplicationRunner
 * 只要工程一启动，就会执行此类下的run方法
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/22
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(RequestCount.class);
    @Autowired
    private RequestCount requestCount;

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
