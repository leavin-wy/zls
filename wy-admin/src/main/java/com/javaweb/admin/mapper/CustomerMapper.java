package com.javaweb.admin.mapper;

import com.javaweb.admin.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 客户信息 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
public interface CustomerMapper extends BaseMapper<Customer> {
    @Select("select date_format(td_time,'%Y-%m-%d %H:%i:%s') " +
            "from sys_tandian where cust_id = ${custId} order by td_time desc limit 0,1")
    String getLastTandianTime(@Param("custId")Integer custId);

    @Select("select count(1) from sys_tandian where cust_id = ${custId}")
    Integer getTandianNum(@Param("custId")Integer custId);

    @Select("SELECT TIMESTAMPDIFF(month,#{birthday},now()) AS diffMonth")
    Integer getAgeForMonth(@Param("birthday")Date birthday );

    @Select("select date_format(gt_time,'%Y-%m-%d %H:%i:%s') 'gtTime', gt_desc 'gtDesc' " +
            "from sys_goutong where cust_id = ${custId} order by gt_time desc limit 0,1")
    Map<String,Object> getLastGoutongInfo(@Param("custId")Integer custId);
}
