package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.gb28181.bean.AiDevice;
import com.genersoft.iot.vmp.gb28181.bean.AiDeviceLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: Ai算法日志记录
 * @author: Liny
 * @email: 2930251092@qq.com
 * @date: 2023/4/20 9:43
 */
@Mapper
public interface AiDeviceLogMapper {

    @Insert("insert into ai_device_log(content , ai_device_id , content_id , time_stamp , type) values(#{content} , #{aiDeviceId} , #{contentId} , #{timeStamp} , #{type})")
    int addAiDeviceLog(AiDeviceLog aiDeviceLog);
}
