package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.gb28181.bean.AiDevice;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: 
 * @date: 2022/11/30 15:25
 */
@Mapper
@Repository
public interface AiDeviceMapper {

    @Delete("DELETE FROM ai_device WHERE id=#{id}")
    int delete(Integer id);


    @Insert("INSERT INTO ai_device (name, url, ip, port, createTime, updateTime) VALUES" +
            "('${name}', '${url}', '${ip}', '${port}', '${createTime}', '${updateTime}')")
    int add(AiDevice aiDevice);

    @Update(value = {" <script>" +
            "UPDATE ai_device " +
            "SET updateTime='${updateTime}' " +
            "<if test=\"name != null\">, name='${name}'</if>" +
            "<if test=\"url != null\">, url='${url}'</if>" +
            "<if test=\"ip != null\">, ip='${ip}'</if>" +
            "<if test=\"port != null\">, port='${port}'</if>" +
            "WHERE id=#{id}" +
            " </script>"})
    int update(AiDevice aiDevice);

    @Select(value = {" <script>" +
            " SELECT * FROM ai_device " +
            " WHERE 1=1 " +
            " <if test=\"deviceId != null\" >  AND id in(select algorithmId from device_algorithm where deviceId = '${deviceId}') </if>" +
            " ORDER BY id desc " +
            " </script>"})
    List<AiDevice> list(String deviceId);
}
