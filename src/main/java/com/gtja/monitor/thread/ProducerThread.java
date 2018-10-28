package com.gtja.monitor.thread;

import com.gtja.monitor.resource.RequestCount;

/**
 * 此类为一个生产线程，创建此线程对象时，将请求的数据接收过来
 * 然后执行run方法的时候，将数据写入到内存中（也就是requestCount内部）
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/22
 */
public class ProducerThread implements Runnable {

    /**
     * 此处不能用@Autowired的方式注入进来
     * 因为是此类是new出来的，不是工程启动的时候注入到spring容器
     */
    private RequestCount requestCount;
    /*请求地址*/
    private String requestURL;
    /*请求的时间戳（精确到毫秒）*/
    private Long currentTimeMillis;


    /**
     * 创建一个生产线程对象，并接收请求的数据，包括请求地址，请求的时间，存放数据的对象
     *
     * @param requestURL        接口请求的地址，例如http://localhost:8080/testURL
     * @param currentTimeMillis 请求的时间戳（精确到毫秒），也就是请求时系统的时间
     * @param requestCount      请求的数据都保存在此对象中
     */
    public ProducerThread(String requestURL, Long currentTimeMillis, RequestCount requestCount) {
        this.requestURL = requestURL;
        this.currentTimeMillis = currentTimeMillis;
        this.requestCount = requestCount;
    }

    @Override
    public void run() {
        requestCount.put(requestURL, currentTimeMillis);
    }
}
