package com.genersoft.iot.vmp.vmanager.AI;

import com.genersoft.iot.vmp.gb28181.bean.AiDevice;
import com.genersoft.iot.vmp.gb28181.bean.PlatformCatalog;
import com.genersoft.iot.vmp.gb28181.form.AiDeviceForm;
import com.genersoft.iot.vmp.service.IAiDeviceService;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import com.genersoft.iot.vmp.utils.DateUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author: 
 * @date: 2022/11/30 14:50
 */
@Tag(name  = "AI设备管理")
@CrossOrigin
@RestController
@RequestMapping("/api/ai/device")
public class DeviceController {

    @Autowired
    private IAiDeviceService aiDeviceService;

    @Operation(summary = "新增修改")
    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody AiDeviceForm aiDeviceForm) {
        AiDevice device = new AiDevice();
        BeanUtils.copyProperties(aiDeviceForm, device);
        device.setCreateTime(DateUtil.getNow());
        device.setUpdateTime(DateUtil.getNow());
        aiDeviceService.save(device);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    @Parameter(name = "id", description = "Id", required = true)
    public void delete(@RequestParam Integer id){
        aiDeviceService.delete(id);
    }

    @GetMapping("/all")
    @Operation(summary = "查询所有数据")
    public List<AiDevice> all(){
        // 获取当前登录用户id
        return aiDeviceService.getAll();
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询")
    @Parameter(name = "page", description = "当前页", required = true)
    @Parameter(name = "count", description = "每页查询数量", required = true)
    public PageInfo<AiDevice> users(int page, int count) {
        return aiDeviceService.list(page, count);
    }
}
