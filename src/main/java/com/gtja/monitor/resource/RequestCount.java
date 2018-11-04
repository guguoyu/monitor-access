package com.gtja.monitor.resource;

import com.gtja.monitor.dto.MaxRequestCountDto;
import com.gtja.monitor.dto.RequestDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 此类是用来存放请求的数据，目前包含以下两个属性
 * 1.属性requestMap存放的是每次请求的数据，此属性的注解已作了详细的说明
 * 2.属性maxRequestCountMap存放的是每个请求，在某个时间段的 最大访问量
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/25
 */
@Component
public class RequestCount {

    private final Logger logger = LoggerFactory.getLogger(RequestCount.class);

    private BlockingDeque<RequestDataDto> linkedBlockingDeque = new LinkedBlockingDeque<>(100000);

    public BlockingDeque<RequestDataDto> getLinkedBlockingDeque() {
        return linkedBlockingDeque;
    }

    /**
     * 用来存储每次请求的数据
     * key为请求地址，例如：http:127.0.0.1:8080/testrequest
     * value为一个 子map，其中 子map中的key为请求时间的时间戳，子map中的value为对应的请求次数
     */
    private Map<String, Map<Long, Integer>> requestMap = new ConcurrentHashMap<>();


    /**
     * 用来保存每个请求 在某个时间段 的最大访问量
     * key为请求的地址，例如：http:127.0.0.1:8080/testrequest
     * value为一个实体类MaxRequestCountDto,其中的属性有请求地址，开始时间，结束时间，以及 此时间段的最大访问量
     */
    private Map<String, MaxRequestCountDto> maxRequestCountMap = new ConcurrentHashMap<>();


    public RequestDataDto take() throws InterruptedException {
        RequestDataDto requestDataDto = this.linkedBlockingDeque.take();
        return requestDataDto;
    }

    /**
     * 此方法主要是将请求的队列中数据统计到requestMap中
     */
    public void put(String requestURL, Long requestTimeMillis) {
        Map<Long, Integer> curTimeCountMap = this.requestMap.get(requestURL);
        if (null == curTimeCountMap) {//curTimeCountMap为null，说明是第一次请求
            //为空，则直接创建一个新对象，
            curTimeCountMap = new ConcurrentHashMap<Long, Integer>();
            //并将当前时间作为key，1作为次数存入
            curTimeCountMap.put(requestTimeMillis, 1);
            //存入到内存中
            this.requestMap.put(requestURL, curTimeCountMap);
        } else {//curTimeCounMap != null
            //获取该请求对应的时间戳的访问次数
            Integer requestCount = curTimeCountMap.get(requestTimeMillis);
            if (null == requestCount) {
                curTimeCountMap.put(requestTimeMillis, 1);
            } else {
                curTimeCountMap.put(requestTimeMillis, requestCount + 1);
            }
        }
    }

    /**
     * 此方法的作用：
     * 1.从requestMap中清理掉一些过期的数据，只保留每个接口最近一分钟的访问数据
     * 2.计算每个接口最近一分钟的访问数量maxTemp，并与内存保存的最大访问数量进行比较，
     * 如果maxTemp＞内存中保存的，则更新内存数据
     * 此方法会有一个定时任务每隔1秒过来执行
     */
    public void update(long curTimeMillis) {
        /*计算出1分钟之前的时间毫秒值*/
        long oneMinuteAgoMillis = curTimeMillis - 1000 * 60;

        Set<String> keySet = requestMap.keySet();
        /*遍历请求地址,keySet中存储的为每个请求地址*/
        for (String requestURL : keySet) {
            /*获取到请求的访问时间和对应的次数*/
            Map<Long, Integer> curTimeCountMap = requestMap.get(requestURL);
            /**
             * 遍历请求的访问时间，判断时间是否在最近1分钟内
             * 如果不在最近一分钟内，则删除
             * 如果在最近一分钟内，则累加到临时变量maxTemp中
             */
            int maxTemp = 0;
            Set<Long> curTimes = curTimeCountMap.keySet();
            for (Long curTime : curTimes) {
                if (curTime.longValue() < oneMinuteAgoMillis) {
                    curTimeCountMap.remove(curTime);
                } else {
                    maxTemp += curTimeCountMap.get(curTime);
                }
            }
            if (maxTemp > 0) {
                /*将当前时间毫秒值和1分钟之前的毫秒值转换成指定的格式*/
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentDate = new Date(curTimeMillis);
                Date oneMinuteAgoDate = new Date(oneMinuteAgoMillis);
                String startTime = sdf.format(oneMinuteAgoDate);
                String endTime = sdf.format(currentDate);
                /*获取该请求的最大访问量的对象*/
                MaxRequestCountDto maxRequestCountDto = maxRequestCountMap.get(requestURL);
                /*如果maxRequestCountDto为null，说明是第一次存储*/
                if (null == maxRequestCountDto) {
                    maxRequestCountDto = new MaxRequestCountDto();
                    maxRequestCountDto.setRequestURL(requestURL);
                    maxRequestCountDto.setStartTime(startTime);
                    maxRequestCountDto.setEndTime(endTime);
                    maxRequestCountDto.setMaxCount(maxTemp);
                    /*最后存入到内存中*/
                    maxRequestCountMap.put(requestURL, maxRequestCountDto);
                } else {
                    /*如果maxRequestCountDto不为null,说明内存中已存在之前记录的最大访问量，只需拿当前计算的最大值与内存的最大值进行判断即可*/
                    if (maxTemp > maxRequestCountDto.getMaxCount()) {
                        maxRequestCountDto.setStartTime(startTime);
                        maxRequestCountDto.setEndTime(endTime);
                        maxRequestCountDto.setMaxCount(maxTemp);
                        logger.info(maxRequestCountMap.toString());
                    }
                }
            }
        }
    }

}
