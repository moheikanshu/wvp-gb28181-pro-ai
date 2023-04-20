package com.genersoft.iot.vmp.web.gb28181;

import com.alibaba.fastjson2.JSONObject;
import com.genersoft.iot.vmp.gb28181.bean.AiDeviceLog;
import com.genersoft.iot.vmp.service.AiDeviceLogService;
import com.genersoft.iot.vmp.service.impl.AiDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: Ai算法日志
 * @author: Liny
 * @email: 2930251092@qq.com
 * @date: 2023/4/20 10:08
 */
@RestController
@RequestMapping("/api/aiDeviceLog")
public class AiDeviceLogController {

    @Autowired
    private AiDeviceLogService aiDeviceLogService;


    @PostMapping("/add")
    public JSONObject add(@RequestBody String str){
        JSONObject res = new JSONObject();
        AiDeviceLog aiDeviceLog = new AiDeviceLog();
        JSONObject jsonObject = JSONObject.parseObject(str);
        Integer contentId = jsonObject.getInteger("content_id");
        Integer aiDeviceId = jsonObject.getInteger("ai_device_id");
        Integer type = jsonObject.getInteger("type");
        String content = JSONObject.toJSONString(jsonObject);
        Long timeStamp = System.currentTimeMillis() / 1000;
        if(contentId == null  || content == null || type == null || aiDeviceId == null){
            res.put("code" , 400);
            res.put("message" , "参数异常");
            return res;
        }
        try {
            aiDeviceLog.setContent(content);
            aiDeviceLog.setContentId(contentId);
            aiDeviceLog.setTimeStamp(timeStamp);
            aiDeviceLog.setType(type);
            aiDeviceLog.setAiDeviceId(aiDeviceId);
            aiDeviceLogService.addAiDeviceLog(aiDeviceLog);
            res.put("code" , 200);
            res.put("message" , "操作成功");
        } catch (Exception e) {
            res.put("code" , 500);
            res.put("message" , "操作异常");
        }
        return res;
    }
}
