package com.genersoft.iot.vmp.gb28181.bean;

/**
 * @description: Ai算法日志
 * @author: Liujun
 * @email: 2930251092@qq.com
 * @date: 2023/4/19 15:39
 */
public class AiDeviceLog {
    private Integer id ;

    private String content;

    private Integer contentId;

    private Integer type;

    private Long timeStamp;

    private Integer aiDeviceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getAiDeviceId() {
        return aiDeviceId;
    }

    public void setAiDeviceId(Integer aiDeviceId) {
        this.aiDeviceId = aiDeviceId;
    }
}
