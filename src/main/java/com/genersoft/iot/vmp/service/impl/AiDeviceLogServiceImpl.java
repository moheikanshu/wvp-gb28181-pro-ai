package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.gb28181.bean.AiDeviceLog;
import com.genersoft.iot.vmp.service.AiDeviceLogService;
import com.genersoft.iot.vmp.storager.dao.AiDeviceLogMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Liny
 * @email: 2930251092@qq.com
 * @date: 2023/4/20 9:52
 */
@Service
public class AiDeviceLogServiceImpl implements AiDeviceLogService {
    @Autowired
    private AiDeviceLogMapper  aiDeviceLogMapper;

    @Override
    public boolean addAiDeviceLog(AiDeviceLog aiDeviceLog) {
        return aiDeviceLogMapper.addAiDeviceLog(aiDeviceLog) > 0;
    }
}
