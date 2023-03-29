package com.genersoft.iot.vmp.vmanager.gb28181.vo;

/**
 * @description: VO
 * @author: Liny
 * @email: 2930251092@qq.com
 * @date: 2023/3/29 13:58
 */
public class DeviceChannelVO {
    /**
     * 设备ID
     */
    private String channelId;

    /**
     * 通道ID
     */
    private String deviceID;


    /**
     * 推流地址
     */
    private String gbPushStreamsAddr;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getGbPushStreamsAddr() {
        return gbPushStreamsAddr;
    }

    public void setGbPushStreamsAddr(String gbPushStreamsAddr) {
        this.gbPushStreamsAddr = gbPushStreamsAddr;
    }
}
