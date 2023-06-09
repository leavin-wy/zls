package com.javaweb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.system.entity.Config;
import com.javaweb.system.mapper.ConfigMapper;
import com.javaweb.system.service.IConfigWebService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConfigWebServiceImpl implements IConfigWebService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * @param map 参数
     * @return
     */
    @Override
    public JsonResult configEdit(Map<String, Object> map) {

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            System.out.println("KEY:" + key + ",值：" + value);

            if (key.contains("checkbox")) {
                // 复选框
                String[] item = key.split("__");
                key = item[0];
            } else if (key.contains("upimage")) {
                // 单图上传
                String[] item = key.split("__");
                key = item[0];
                if (value.contains(CommonConfig.imageURL)) {
                    value = value.replaceAll(CommonConfig.imageURL, "");
                }
            } else if (key.contains("upimgs")) {
                // 多图上传
                String[] item = key.split("__");
                key = item[0];

                String[] stringsVal = value.split(",");
                List<String> stringList = new ArrayList<>();
                for (String s : stringsVal) {
                    if (s.contains(CommonConfig.imageURL)) {
                        stringList.add(s.replaceAll(CommonConfig.imageURL, ""));
                    } else {
                        // 已上传图片
                        stringList.add(s.replaceAll(CommonConfig.imageURL, ""));
                    }
                }
                value = StringUtils.join(stringList, ",");
            } else if (key.contains("ueditor")) {
                String[] item = key.split("__");
                key = item[0];
                // 处理富文本信息

            }
            // 更新信息
            QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag", key);
            Config config = configMapper.selectOne(queryWrapper);
            if (config == null) {
                continue;
            }
            config.setValue(value);
            configMapper.updateById(config);
        }
        return JsonResult.success();
    }
}
