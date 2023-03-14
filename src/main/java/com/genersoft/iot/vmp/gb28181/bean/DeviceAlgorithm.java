package com.genersoft.iot.vmp.gb28181.bean;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author: qlj
 * @date: 2023/3/14 10:18
 */
@Schema(description = "设备算法")
public class DeviceAlgorithm {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "设备国标编号")
    private String deviceId;

    @Schema(description = "算法id")
    private Integer algorithmId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }
}
