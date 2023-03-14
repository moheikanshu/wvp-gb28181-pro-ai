package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.gb28181.bean.DeviceAlgorithm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: qlj
 * @date: 2023/3/14 10:24
 */
@Mapper
@Repository
public interface DeviceAlgorithmMapper {

    @Delete("delete from device_algorithm where deviceId=#{deviceId}")
    int deleteByDeviceId(String deviceId);

    @Select("select * from device_algorithm where deviceId=#{deviceId}")
    List<DeviceAlgorithm> listByDeviceId(String deviceId);

    @Insert("<script> " +
            "INSERT IGNORE into device_algorithm " +
            "(deviceId, algorithmId)" +
            "values " +
            "<foreach collection='list' index='index' item='item' separator=','> " +
            "('${item.deviceId}', '${item.algorithmId}') "+
            "</foreach> " +
            "</script>")
    int batchAdd(List<DeviceAlgorithm> list);
}
