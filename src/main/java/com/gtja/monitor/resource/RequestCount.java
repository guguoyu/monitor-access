package com.gtja.monitor.resource;

import com.gtja.monitor.dto.MaxRequestCountDto;
import com.gtja.monitor.dto.RequestDataDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类是用来存放请求的数据，目前包含以下两部分
 * 1.属性requestMap存放的是每次请求的数据，此属性的注解已作了详细的说明
 * 2.属性maxRequestCountMap存放的是每个请求，在某个时间段的 最大访问量
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/25
 */
@Component
public class RequestCount {

    /**
     * 用来存储每次请求的数据
     * key为请求地址，
     * value为一个 子map，其中 子map中的key为请求时间的时间戳，子map中的value为对应的请求次数
     */
    private Map<String, Map<Long, Integer>> requestMap = new HashMap<>();


    /**
     * 用来保存每个请求 在某个时间段 的最大访问量
     * key为请求的地址，例如：http:127.0.0.1:8080/testrequest
     * value为一个实体类MaxRequestCountDto,其中的属性有请求地址，开始时间，结束时间，以及 此时间段的最大访问量
     */
    private Map<String, MaxRequestCountDto> maxRequestCountMap = new HashMap<>();


    /**
     * 此方法主要是将请求的数据统计到内存中
     * @param requestURL    请求地址
     * @param curTimeMillis 请求的时间戳（毫秒值）
     */
    public synchronized void put(String requestURL, Long curTimeMillis) {
        Map<Long, Integer> curTimeCountMap = this.requestMap.get(requestURL);
        if (null == curTimeCountMap) {//curTimeCountMap为null，说明是第一次请求，或者数据被复制为null
            //为空，则直接创建一个新对象，
            curTimeCountMap = new HashMap<Long, Integer>();
            //并将当前时间作为key，1作为次数存入
            curTimeCountMap.put(curTimeMillis, 1);
            //存入到内存中
            this.requestMap.put(requestURL, curTimeCountMap);
        } else {//curTimeCounMap != null
            //获取该请求对应的时间戳的访问次数
            Integer requestCount = curTimeCountMap.get(curTimeMillis);
            if (null == requestCount) {
                curTimeCountMap.put(curTimeMillis, 1);
            } else {
                curTimeCountMap.put(curTimeMillis, requestCount + 1);
            }
        }
    }

    /**
     *
     * @param curTimeMillis
     */
    public synchronized void update(Long curTimeMillis){

    }

}
