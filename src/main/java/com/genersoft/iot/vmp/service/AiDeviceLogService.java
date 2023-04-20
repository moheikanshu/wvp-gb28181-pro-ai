package com.genersoft.iot.vmp.service;

import com.genersoft.iot.vmp.gb28181.bean.AiDeviceLog;

/**
 * @description: Ai算法日志记录
 * @author: Liny
 * @email: 2930251092@qq.com
 * @date: 2023/4/20 9:51
 */
public interface AiDeviceLogService {

    boolean addAiDeviceLog(AiDeviceLog aiDeviceLog);
}
