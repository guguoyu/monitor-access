package com.gtja.monitor.controller;

import com.gtja.monitor.annotation.MonitorRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @MonitorRequest
    @RequestMapping("/testaspect")
    public String test(){
        return "this is a test";
    }

    @MonitorRequest
    @RequestMapping("/testaspect1")
    public String test1(){
        return "this is a test1";
    }
}
