package com.javaweb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.system.entity.Target;
import com.javaweb.system.mapper.TargetMapper;
import com.javaweb.system.service.IIndexService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.system.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leavin
 * @date 2023-04-09 12:13
 * @desc
 */
@Service
public class IndexServiceImpl implements IIndexService {
    @Autowired
    private TargetMapper targetMapper;

    /**
     * 查询首页统计数据
     * @param flag 1：当日  2：本周  3本月
     * @return
     */
    @Override
    public JsonResult maininitbase(String flag) {
        String today = DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD);
        List<Map<String, Object>> targets = targetMapper.selectListForCurrDay(ShiroUtils.getAdminId(),today);
        Map<String, Object> result = targets.stream().collect(Collectors.toMap(t->(String)t.get("target_code"),t->t.get("target_value")));
        return JsonResult.success("查询成功",result);
    }
}
