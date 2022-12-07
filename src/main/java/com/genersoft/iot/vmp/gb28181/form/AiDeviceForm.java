package com.genersoft.iot.vmp.gb28181.form;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author: 
 * @date: 2022/11/30 15:56
 */
public class AiDeviceForm {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "IP")
    private String  ip;

    @Schema(description = "端口")
    private int port;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
