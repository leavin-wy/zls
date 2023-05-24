package com.javaweb.admin.service;

import com.javaweb.admin.entity.Customer;
import com.javaweb.admin.query.CustomerQuery;
import com.javaweb.common.common.IBaseService;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.easypoi.ExcelBaseParam;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 客户信息 服务类
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
public interface ICustomerService extends IBaseService<Customer> {
    JsonResult uploadExcel(HttpServletRequest request);

    void downloadExcel(ExcelBaseParam excelBaseParam);

    //定时获取沟通计划消息 以后考虑使用消息队列直接推送
    void pushCustGontongNotice();

    JsonResult delCustById(Integer id);
}