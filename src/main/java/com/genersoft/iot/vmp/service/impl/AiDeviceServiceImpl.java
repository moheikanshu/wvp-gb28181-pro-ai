package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.gb28181.bean.AiDevice;
import com.genersoft.iot.vmp.service.IAiDeviceService;
import com.genersoft.iot.vmp.storager.dao.AiDeviceMapper;
import com.genersoft.iot.vmp.storager.dao.UserMapper;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: 
 * @date: 2022/11/30 15:24
 */
@Service
public class AiDeviceServiceImpl implements IAiDeviceService {

    @Resource
    private AiDeviceMapper aiDeviceMapper;

    @Override
    public void save(AiDevice aiDevice) {
        if(aiDevice.getId() == null || aiDevice.getId() == 0){
            aiDeviceMapper.add(aiDevice);
        }else {
            aiDeviceMapper.update(aiDevice);
        }
    }

    @Override
    public List<AiDevice> getAll() {
        return aiDeviceMapper.list();
    }

    @Override
    public PageInfo<AiDevice> list(int page, int count) {
        PageHelper.startPage(page, count);
        List<AiDevice> devices = aiDeviceMapper.list();
        return new PageInfo<>(devices);
    }

    @Override
    public int delete(Integer id) {
        return aiDeviceMapper.delete(id);
    }
}
