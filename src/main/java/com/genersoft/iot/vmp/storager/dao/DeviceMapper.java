package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.DeviceAlgorithm;
import com.genersoft.iot.vmp.vmanager.bean.ResourceBaceInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于存储设备信息
 */
@Mapper
@Repository
public interface DeviceMapper {

    @Select("SELECT " +
            "deviceId, " +
            "coalesce(custom_name, name) as name, " +
            "password, " +
            "manufacturer, " +
            "model, " +
            "firmware, " +
            "transport," +
            "streamMode," +
            "ip," +
            "sdpIp," +
            "port," +
            "hostAddress," +
            "expires," +
            "registerTime," +
            "keepaliveTime," +
            "createTime," +
            "updateTime," +
            "charset," +
            "subscribeCycleForCatalog," +
            "subscribeCycleForMobilePosition," +
            "mobilePositionSubmissionInterval," +
            "subscribeCycleForAlarm," +
            "ssrcCheck," +
            "geoCoordSys," +
            "treeType," +
            "online" +
            " FROM device WHERE deviceId = #{deviceId}")
    Device getDeviceByDeviceId(String deviceId);

    @Insert("INSERT INTO device (" +
                "deviceId, " +
                "name, " +
                "manufacturer, " +
                "model, " +
                "firmware, " +
                "transport," +
                "streamMode," +
                "ip," +
                "sdpIp," +
                "port," +
                "hostAddress," +
                "expires," +
                "registerTime," +
                "keepaliveTime," +
                "createTime," +
                "updateTime," +
                "charset," +
                "subscribeCycleForCatalog," +
                "subscribeCycleForMobilePosition," +
                "mobilePositionSubmissionInterval," +
                "subscribeCycleForAlarm," +
                "ssrcCheck," +
                "geoCoordSys," +
                "treeType," +
                "online" +
            ") VALUES (" +
                "#{deviceId}," +
                "#{name}," +
                "#{manufacturer}," +
                "#{model}," +
                "#{firmware}," +
                "#{transport}," +
                "#{streamMode}," +
                "#{ip}," +
                "#{sdpIp}," +
                "#{port}," +
                "#{hostAddress}," +
                "#{expires}," +
                "#{registerTime}," +
                "#{keepaliveTime}," +
                "#{createTime}," +
                "#{updateTime}," +
                "#{charset}," +
                "#{subscribeCycleForCatalog}," +
                "#{subscribeCycleForMobilePosition}," +
                "#{mobilePositionSubmissionInterval}," +
                "#{subscribeCycleForAlarm}," +
                "#{ssrcCheck}," +
                "#{geoCoordSys}," +
                "#{treeType}," +
                "#{online}" +
            ")")
    int add(Device device);

    @Update(value = {" <script>" +
                "UPDATE device " +
                "SET updateTime='${updateTime}'" +
                "<if test=\"name != null\">, name='${name}'</if>" +
                "<if test=\"manufacturer != null\">, manufacturer='${manufacturer}'</if>" +
                "<if test=\"model != null\">, model='${model}'</if>" +
                "<if test=\"firmware != null\">, firmware='${firmware}'</if>" +
                "<if test=\"transport != null\">, transport='${transport}'</if>" +
                "<if test=\"ip != null\">, ip='${ip}'</if>" +
                "<if test=\"port != null\">, port=${port}</if>" +
                "<if test=\"hostAddress != null\">, hostAddress='${hostAddress}'</if>" +
                "<if test=\"online != null\">, online=${online}</if>" +
                "<if test=\"registerTime != null\">, registerTime='${registerTime}'</if>" +
                "<if test=\"keepaliveTime != null\">, keepaliveTime='${keepaliveTime}'</if>" +
                "<if test=\"expires != null\">, expires=${expires}</if>" +
                "<if test=\"algorithm != null\">, algorithm=${algorithm}</if>" +
                "WHERE deviceId='${deviceId}'"+
            " </script>"})
    int update(Device device);

    @Update(value = {" <script>" +
            "UPDATE device " +
            "SET updateTime='${updateTime}'" +
            "<if test=\"algorithm != null\">, algorithm=${algorithm}</if>" +
            "WHERE deviceId='${deviceId}'"+
            " </script>"})
    int updateAlgorithm(Device device);

    @Select("SELECT " +
            "deviceId, " +
            "coalesce(custom_name, name) as name, " +
            "password, " +
            "manufacturer, " +
            "model, " +
            "firmware, " +
            "transport," +
            "streamMode," +
            "ip," +
            "sdpIp," +
            "port," +
            "hostAddress," +
            "expires," +
            "registerTime," +
            "keepaliveTime," +
            "createTime," +
            "updateTime," +
            "charset," +
            "subscribeCycleForCatalog," +
            "subscribeCycleForMobilePosition," +
            "mobilePositionSubmissionInterval," +
            "subscribeCycleForAlarm," +
            "ssrcCheck," +
            "geoCoordSys," +
            "treeType," +
            "online," +
            "algorithm," +
            "(SELECT count(0) FROM device_channel WHERE deviceId=de.deviceId) as channelCount  FROM device de")
    @Results({
            @Result(property = "deviceId", column = "deviceId"),
            @Result(
                    property = "algorithms",
                    column = "deviceId", //根据device表中的deviceId来查询 此用户所拥有的算法信息
                    javaType = List.class, //返回此用户所拥有订单的List集合类型
                    many = @Many(select = "com.genersoft.iot.vmp.storager.dao.DeviceAlgorithmMapper.listByDeviceId") //根据user数据表中id 在orders表中来查询此订单
            )
    })
    List<Device> getDevices();

    @Delete("DELETE FROM device WHERE deviceId=#{deviceId}")
    int del(String deviceId);

    @Update("UPDATE device SET online=0")
    int outlineForAll();

    @Select("SELECT " +
            "deviceId, " +
            "coalesce(custom_name, name) as name, " +
            "password, " +
            "manufacturer, " +
            "model, " +
            "firmware, " +
            "transport," +
            "streamMode," +
            "ip," +
            "sdpIp," +
            "port," +
            "hostAddress," +
            "expires," +
            "registerTime," +
            "keepaliveTime," +
            "createTime," +
            "updateTime," +
            "charset," +
            "subscribeCycleForCatalog," +
            "subscribeCycleForMobilePosition," +
            "mobilePositionSubmissionInterval," +
            "subscribeCycleForAlarm," +
            "ssrcCheck," +
            "geoCoordSys," +
            "treeType," +
            "online " +
            " FROM device WHERE online = 1")
    List<Device> getOnlineDevices();
    @Select("SELECT " +
            "deviceId, " +
            "coalesce(custom_name, name) as name, " +
            "password, " +
            "manufacturer, " +
            "model, " +
            "firmware, " +
            "transport," +
            "streamMode," +
            "ip," +
            "sdpIp," +
            "port," +
            "hostAddress," +
            "expires," +
            "registerTime," +
            "keepaliveTime," +
            "createTime," +
            "updateTime," +
            "charset," +
            "subscribeCycleForCatalog," +
            "subscribeCycleForMobilePosition," +
            "mobilePositionSubmissionInterval," +
            "subscribeCycleForAlarm," +
            "ssrcCheck," +
            "geoCoordSys," +
            "treeType," +
            "online" +
            " FROM device WHERE ip = #{host} AND port=${port}")
    Device getDeviceByHostAndPort(String host, int port);

    @Update(value = {" <script>" +
            "UPDATE device " +
            "SET updateTime='${updateTime}'" +
            "<if test=\"name != null\">, custom_name='${name}'</if>" +
            "<if test=\"password != null\">, password='${password}'</if>" +
            "<if test=\"streamMode != null\">, streamMode='${streamMode}'</if>" +
            "<if test=\"ip != null\">, ip='${ip}'</if>" +
            "<if test=\"sdpIp != null\">, sdpIp='${sdpIp}'</if>" +
            "<if test=\"port != null\">, port=${port}</if>" +
            "<if test=\"charset != null\">, charset='${charset}'</if>" +
            "<if test=\"subscribeCycleForCatalog != null\">, subscribeCycleForCatalog=${subscribeCycleForCatalog}</if>" +
            "<if test=\"subscribeCycleForMobilePosition != null\">, subscribeCycleForMobilePosition=${subscribeCycleForMobilePosition}</if>" +
            "<if test=\"mobilePositionSubmissionInterval != null\">, mobilePositionSubmissionInterval=${mobilePositionSubmissionInterval}</if>" +
            "<if test=\"subscribeCycleForAlarm != null\">, subscribeCycleForAlarm=${subscribeCycleForAlarm}</if>" +
            "<if test=\"ssrcCheck != null\">, ssrcCheck=${ssrcCheck}</if>" +
            "<if test=\"geoCoordSys != null\">, geoCoordSys=#{geoCoordSys}</if>" +
            "<if test=\"treeType != null\">, treeType=#{treeType}</if>" +
            "<if test=\"mediaServerId != null\">, mediaServerId=#{mediaServerId}</if>" +
            "WHERE deviceId='${deviceId}'"+
            " </script>"})
    int updateCustom(Device device);

    @Insert("INSERT INTO device (" +
            "deviceId, " +
            "custom_name, " +
            "password, " +
            "sdpIp, " +
            "createTime," +
            "updateTime," +
            "charset," +
            "ssrcCheck," +
            "geoCoordSys," +
            "treeType," +
            "online" +
            ") VALUES (" +
            "#{deviceId}," +
            "#{name}," +
            "#{password}," +
            "#{sdpIp}," +
            "#{createTime}," +
            "#{updateTime}," +
            "#{charset}," +
            "#{ssrcCheck}," +
            "#{geoCoordSys}," +
            "#{treeType}," +
            "#{online}" +
            ")")
    void addCustomDevice(Device device);

    @Select("select count(1) as total, sum(online) as online from device")
    ResourceBaceInfo getOverview();

    @Update("  UPDATE ai_device a\n" +
            "            JOIN (SELECT da.algorithmId, GROUP_CONCAT(DISTINCT dc.gb_push_streams_addr SEPARATOR '|') AS url\n" +
            "                  FROM device_channel dc\n" +
            "                           LEFT JOIN device_algorithm da ON dc.deviceId = da.deviceId\n" +
            "                           LEFT JOIN device de ON da.deviceId = de.deviceId\n" +
            "                  WHERE de.algorithm = 1\n" +
            "                  GROUP BY da.algorithmId) ai ON a.id = ai.algorithmId\n" +
            "        SET a.url = ai.url" +
            " WHERE (1 = 1);")
    int setAiDeviceUrl();

    @Update("UPDATE ai_device a SET a.url = null WHERE (1);")
    int setAiDeviceUrlIsNull();
}
