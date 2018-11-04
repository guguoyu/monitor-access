package com.gtja.monitor.controller;

import com.gtja.monitor.annotation.MonitorRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @MonitorRequest
    @RequestMapping("/monitor")
    public String test(){
        return "this is a test";
    }


    @RequestMapping("/nomonitor")
    public String test1(){
        return "this is a test1";
    }
}
