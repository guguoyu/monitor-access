package com.gtja.monitor.dto;

/**
 * 此实体类用来存储某个接口 在某一分钟时间段 最大访问量
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/25 14:49
 */
public class MaxRequestCountDto {
    //请求地址
    private String requestURL;
    //某一分钟时间段的起始时间
    private String startTime;
    //某一分钟时间段的结束时间
    private String endTime;
    //某一分钟时间段的最大访问量
    private Integer maxCount;

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public String toString() {
        return "MaxRequestCountDto{" +
                "requestURL='" + requestURL + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", maxCount=" + maxCount +
                '}';
    }
}
