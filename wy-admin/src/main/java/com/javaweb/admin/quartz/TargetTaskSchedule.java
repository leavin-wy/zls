package com.javaweb.admin.quartz;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaweb.system.constant.TargetConstant;
import com.javaweb.admin.entity.Customer;
import com.javaweb.system.entity.Target;
import com.javaweb.admin.mapper.CustomerMapper;
import com.javaweb.system.mapper.TargetMapper;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.system.entity.Admin;
import com.javaweb.system.mapper.AdminMapper;
import com.javaweb.system.utils.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author leavin
 * @date 2023-04-07 14:33
 * @desc
 */
@Component
public class TargetTaskSchedule {
    private final static Log log = LogFactory.getLog(TargetTaskSchedule.class);

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TargetMapper targetMapper;

    @Scheduled(cron = "0 0/5 6-23 * * ? ")
    public void work(){
        List<Admin> admins = adminMapper.selectList(new QueryWrapper<>());
        for(Admin admin : admins){
            boolean flag = AdminUtils.hasRoleAnyMatch(admin.getId(), "运营","销售");
            if(!flag) continue;
            String today = DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD);

            //当日不同渠道新增数
            List<Map> sourceCount = customerMapper.selectSourceCount(admin.getId(),today);
            LambdaQueryWrapper<Target> targetDel = new LambdaQueryWrapper<>();
            targetDel.eq(Target::getDataTime,today);
            targetDel.eq(Target::getUserId,admin.getId());
            targetMapper.delete(targetDel);

            sourceCount.stream().forEach(source->{
                String[] sourceArr = TargetConstant.CUSTOMER_SOURCE_LIST.get(source.get("source")).split("\\|");
                insertTarget(today,admin.getId(),admin.getRealname(),sourceArr[0],sourceArr[1],((Long)source.get("count")).intValue());
            });
            //当日新增总数
            int allCount = sourceCount.stream().mapToInt(map -> ((Long) map.get("count")).intValue()).sum();
            insertTarget(today,admin.getId(),admin.getRealname(),"1006","当日新增总数",allCount);

            //联系客资    2001-当日联系总数  2002-当日回复数  2003-未到店联系数  2004-未到店回复数  2005-成交数
            List<Map> goutongCount = customerMapper.selectGontongCount(admin.getId(),today);

            //2001-当日联系总数
            int allGoutongCount = goutongCount.stream().mapToInt(map -> ((Long) map.get("lxCount")).intValue()).sum();
            insertTarget(today,admin.getId(),admin.getRealname(),"2001","当日联系总数",allGoutongCount);

            //2002-当日回复数
            int allReplyCount = goutongCount.stream().filter(map->(Integer) map.get("replyFlag")==2).mapToInt((map->((Long)map.get("lxCount")).intValue())).sum();
            insertTarget(today,admin.getId(),admin.getRealname(),"2002","当日回复数",allReplyCount);

            //2003-当日未到店联系数
            int wddlxCount = customerMapper.selectWddlxCount(admin.getId(),today);
            insertTarget(today,admin.getId(),admin.getRealname(),"2003","当日未到店联系数",wddlxCount);

            //2004-当日未到店回复数
            int wddhfCount = customerMapper.selectWddhfCount(admin.getId(),today);
            insertTarget(today,admin.getId(),admin.getRealname(),"2004","当日未到店回复数",wddhfCount);

            //2005-当日成交数
            LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Customer::getCustType,7);
            queryWrapper.eq(Customer::getCompleteTime,today);
            queryWrapper.eq(Customer::getCreateUser,admin.getId());
            Integer completeCount = customerMapper.selectCount(queryWrapper);
            insertTarget(today,admin.getId(),admin.getRealname(),"2005","当日成交数",completeCount);

            //2006-当日到店数
            int ddCount = customerMapper.selectDdCount(admin.getId(),today);
            insertTarget(today,admin.getId(),admin.getRealname(),"2006","当日到店数",ddCount);

            //2007-当日首次到店数
            int scddCount = customerMapper.selectScddCount(admin.getId(),today);
            insertTarget(today,admin.getId(),admin.getRealname(),"2007","当日首次到店数",scddCount);

        }
    }

    /**
     * 插入指标结果表
     * @param dataTime
     * @param userId
     * @param userName
     * @param targetCode
     * @param targetName
     * @param targetValue
     */
    private void insertTarget(String dataTime,Integer userId, String userName
            ,String targetCode,String targetName,Integer targetValue){
        Target target = new Target();
        target.setDataTime(dataTime);
        target.setUserId(userId);
        target.setUserName(userName);
        target.setTargetCode(targetCode);
        target.setTargetName(targetName);
        target.setTargetValue(targetValue);
        targetMapper.insert(target);
    }
}
