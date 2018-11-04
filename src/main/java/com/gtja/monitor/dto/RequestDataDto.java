package com.gtja.monitor.dto;

/**
 * 此类主要用来存储 请求地址和请求时间戳
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/11/2 11:35
 */
public class RequestDataDto {
    /*请求地址*/
    private String requestURL;
    /*请求的时间戳*/
    private Long requestTimeMillis;

    public RequestDataDto() {

    }

    public RequestDataDto(String requestURL, Long requestTimeMillis) {
        this.requestURL = requestURL;
        this.requestTimeMillis = requestTimeMillis;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public Long getRequestTimeMillis() {
        return requestTimeMillis;
    }

    public void setRequestTimeMillis(Long requestTimeMillis) {
        this.requestTimeMillis = requestTimeMillis;
    }
}
