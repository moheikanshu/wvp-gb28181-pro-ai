package com.genersoft.iot.vmp.service;

import com.genersoft.iot.vmp.gb28181.bean.AiDevice;
import com.genersoft.iot.vmp.gb28181.bean.ParentPlatform;
import com.genersoft.iot.vmp.media.zlm.dto.MediaServerItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author: 
 * @date: 2022/11/30 15:21
 */
public interface IAiDeviceService {

    void save(AiDevice aiDevice);

    List<AiDevice> getAll(String  deviceId);

    PageInfo<AiDevice> list(int page, int count);

    int delete(Integer id);
}
