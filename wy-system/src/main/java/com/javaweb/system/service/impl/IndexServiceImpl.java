package com.javaweb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.system.entity.Admin;
import com.javaweb.system.entity.Target;
import com.javaweb.system.mapper.AdminMapper;
import com.javaweb.system.mapper.TargetMapper;
import com.javaweb.system.service.IIndexService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.system.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 查询首页统计数据
     * @param flag 1：当日  2：本周  3本月
     * @return
     */
    @Override
    public JsonResult maininitbase(String flag) {
        String today = DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD);
        List<Map<String, Object>> targets = new ArrayList<>();
        String startDt = "";
        String endDt = "";
        if(StringUtils.equals("1",flag)){
            //当日
            startDt = today;
            endDt = today;
        }else if(StringUtils.equals("2",flag)){
            //本周
            startDt = DateUtils.getThisWeekMonday();
            endDt = today;
        }else if(StringUtils.equals("3",flag)){
            //当月
            startDt = DateUtils.parseDateToStr(DateUtils.YYYY_MM,new Date())+"-01";
            endDt = today;
        }
        if(ShiroUtils.getAdminInfo().getDataType()==1){
            //全权用户看到所有数据
            targets = targetMapper.selectBaseList(startDt,endDt);
        }else if(ShiroUtils.getAdminInfo().getDataType()==2){
            //私有权限只看自己的数据
            targets = targetMapper.selectBaseListByUser(ShiroUtils.getAdminId(),startDt,endDt);
        }
        Map<String, Object> result = targets.stream().collect(Collectors.toMap(t->(String)t.get("target_code"),t->t.get("target_value")));
        return JsonResult.success("查询成功",result);
    }

    /**
     * 查询首页统计数据
     * @param flag 1：当日  2：本周  3本月
     * @return
     */
    @Override
    public JsonResult maininitxzfb(String flag,String[] codes) {
        //String[] codes = new String[]{"user","1001","1002","1003","1004","1005","2001","2002","2003","2004","2006","2007"};
        String today = DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD);
        String startDt = "";
        String endDt = "";
        if(StringUtils.equals("1",flag)){
            //当日
            startDt = today;
            endDt = today;
        }else if(StringUtils.equals("2",flag)){
            //本周
            startDt = DateUtils.getThisWeekMonday();
            endDt = today;
        }else if(StringUtils.equals("3",flag)){
            //当月
            startDt = DateUtils.parseDateToStr(DateUtils.YYYY_MM,new Date())+"-01";
            endDt = today;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        if(ShiroUtils.getAdminInfo().getDataType()==1){
            //全权用户看到所有数据
            List<Admin> admins = adminMapper.selectList(new QueryWrapper<>());
            for(Admin admin : admins){
                boolean isFlag = AdminUtils.hasRoleAnyMatch(admin.getId(), "运营","销售");
                if(!isFlag) continue;
                Map<String, Object> map = new HashMap<>();
                map.put("user",admin.getRealname());
                List<Map<String, Object>> targets = targetMapper.selectBaseListByUser(admin.getId(),startDt,endDt);
                Map<String, Object> targetMap = targets.stream().filter(t->Arrays.asList(codes).contains(t.get("target_code")))
                        .collect(Collectors.toMap(t->(String)t.get("target_code"),t->t.get("target_value")));
                int total = targets.stream().filter(t->Arrays.asList(codes).contains(t.get("target_code")))
                        .mapToInt(t->((BigDecimal) t.get("target_value")).intValue()).sum();
                map.put("total",total);
                map.putAll(targetMap);
                result.add(map);
            }
        }else if(ShiroUtils.getAdminInfo().getDataType()==2){
            //私有权限只看自己的数据
            Map<String, Object> map = new HashMap<>();
            map.put("user",ShiroUtils.getAdminInfo().getRealname());
            List<Map<String, Object>> targets = targetMapper.selectBaseListByUser(ShiroUtils.getAdminId(),startDt,endDt);
            Map<String, Object> targetMap = targets.stream().filter(t->Arrays.asList(codes).contains(t.get("target_code")))
                    .collect(Collectors.toMap(t->(String)t.get("target_code"),t->t.get("target_value")));
            int total = targets.stream().filter(t->Arrays.asList(codes).contains(t.get("target_code")))
                    .mapToInt(t->((BigDecimal) t.get("target_value")).intValue()).sum();
            map.put("total",total);
            map.putAll(targetMap);
            result.add(map);
        }
        return JsonResult.success("查询成功",result);
    }


}
