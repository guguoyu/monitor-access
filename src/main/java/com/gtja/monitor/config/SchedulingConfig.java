package com.gtja.monitor.config;


import com.gtja.monitor.resource.RequestCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private RequestCount requestCount;

    @Scheduled(fixedRate = 1000)
    public void update() {
        requestCount.update();
    }
}
