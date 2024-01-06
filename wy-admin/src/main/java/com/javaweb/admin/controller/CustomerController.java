package com.javaweb.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.admin.constant.CustomerConstant;
import com.javaweb.admin.constant.GoutongConstant;
import com.javaweb.admin.mapper.CustomerMapper;
import com.javaweb.admin.vo.CustomerDownListVo;
import com.javaweb.admin.vo.CustomerListVo;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.Customer;
import com.javaweb.admin.query.CustomerQuery;
import com.javaweb.admin.service.ICustomerService;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.common.utils.easypoi.ExcelBaseParam;
import com.javaweb.common.utils.easypoi.ExcelDownloadUtil;
import com.javaweb.common.utils.easypoi.ExcelSimpleUtil;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.system.utils.ShiroUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.javaweb.common.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户信息 控制器
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(CustomerQuery query) {
        return customerService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:customer:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody Customer entity) {
        //新增
        LambdaQueryWrapper<Customer> custQueryWrapper = new LambdaQueryWrapper<>();
        custQueryWrapper.eq(Customer::getCustName,entity.getCustName());
        custQueryWrapper.eq(Customer::getNickName,entity.getNickName());
        custQueryWrapper.eq(Customer::getSex,entity.getSex());
        if(null != entity.getBirthday()){
            custQueryWrapper.eq(Customer::getBirthday,entity.getBirthday());
        }
        Integer count = customerMapper.selectCount(custQueryWrapper);
        if(count>0){
            return JsonResult.error("存在相同名字/昵称/性别/出生日期的客资");
        }
        return customerService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:customer:update")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody Customer entity) {
        return customerService.edit(entity);
    }

    /**
     * 获取记录详情
     *
     * @param id    记录ID
     * @param model 模型
     * @return
     */
    @Override
    public String edit(Integer id, Model model) {
        Map<String, Object> info = new HashMap<>();
        if (id != null && id > 0) {
            info = customerService.info(id);
        }
        model.addAttribute("info", info);
        return super.edit(id, model);
    }

    /**
     * 删除记录
     *
     * @param id 记录ID
     * @return
     */
    @RequiresPermissions("sys:customer:delete")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return customerService.delCustById(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @RequiresPermissions("sys:customer:batchDelete")
    @Log(title = "客户信息", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return customerService.delRealByIds(ids);
    }

    /**
     * 下载Excel模板
     */
    @Log(title = "客资信息登记模版下载", businessType = BusinessType.EXPORT)
    @GetMapping("/btnExport")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {

        ExcelBaseParam excelBaseParam = new ExcelBaseParam();
        excelBaseParam.setResponse(response)
                .setFileName("客资信息登记模板")
                .setSheetName("客资信息登记");
        customerService.downloadExcel(excelBaseParam);
    }

    /**
     * 上传Excel 批量导入数据
     */
    @Log(title = "客户信息导入", businessType = BusinessType.IMPORT)
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel(HttpServletRequest request) {
        JsonResult jsonResult = customerService.uploadExcel(request);
        return jsonResult;
    }

    /**
     * 客资信息导出
     */
    @Log(title = "客资信息导出", businessType = BusinessType.EXPORT)
    @GetMapping("/custExport")
    public void custExport(CustomerQuery customerQuery, HttpServletResponse response) {
        // 查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(customerQuery.getTandianFlag())){
            //是否有探店
            if(StringUtils.equals("1",customerQuery.getTandianFlag())){
                queryWrapper.eq(Customer::getTdNum,0);
            }else if(StringUtils.equals("2",customerQuery.getTandianFlag())){
                queryWrapper.eq(Customer::getTdNum,1);
            }else if(StringUtils.equals("3",customerQuery.getTandianFlag())){
                queryWrapper.gt(Customer::getTdNum,1);
            }
        }
        if(StringUtils.isNotEmpty(customerQuery.getInteractTimeStr())){
            //下次沟通时间
            queryWrapper.apply(" id in (select g.cust_id from sys_goutong g inner join (select max(id) id,cust_id from sys_goutong group by cust_id) t where g.id = t.id and DATE_FORMAT(g.interact_time,'%Y-%m-%d')='"+customerQuery.getInteractTimeStr()+"')");
        }
        if(StringUtils.isNotEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')>='"+customerQuery.getGtTimeStrStart()+"')");
        }else if(StringUtils.isEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')<='"+customerQuery.getGtTimeStrEnd()+"')");
        }else if(StringUtils.isNotEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')>='"+customerQuery.getGtTimeStrStart()+"' and DATE_FORMAT(gt_time,'%Y-%m-%d')<='"+customerQuery.getGtTimeStrEnd()+"')");
        }
        if(StringUtils.isNotEmpty(customerQuery.getReplyFlag())){
            queryWrapper.apply(" id in (select g.cust_id from sys_goutong g inner join (select max(id) id,cust_id from sys_goutong group by cust_id) t where g.id = t.id and  g.reply_flag="+customerQuery.getReplyFlag()+")");
        }
        if(StringUtils.isNotEmpty(customerQuery.getInviteTimeStr())) {
            //邀约时间
            queryWrapper.apply(" DATE_FORMAT(invite_time,'%Y-%m-%d')='"+customerQuery.getInviteTimeStr()+"'");
        }
        // 姓名/昵称
        if (!StringUtils.isEmpty(customerQuery.getKeywords())) {
            queryWrapper.and(wrapper->wrapper.like(Customer::getCustName, customerQuery.getKeywords()).or().like(Customer::getNickName, customerQuery.getKeywords()));
        }

        // 性别：1=男,2=女,3=未知
        if (!StringUtils.isEmpty(customerQuery.getSex())) {
            queryWrapper.eq(Customer::getSex, customerQuery.getSex());
        }
        // 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交,8=F,9=C+,10=成交未入读
        if (!StringUtils.isEmpty(customerQuery.getCustType())) {
            queryWrapper.eq(Customer::getCustType, customerQuery.getCustType());
        }
        // 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他
        if (!StringUtils.isEmpty(customerQuery.getSource())) {
            queryWrapper.eq(Customer::getSource, customerQuery.getSource());
        }
        if(AdminUtils.hasRoleAnyMatch(ShiroUtils.getAdminId(),"管理员","超级管理员")){
            //管理员看同部门下所有人数据
            queryWrapper.in(Customer::getCreateUser,AdminUtils.getAdminsByDep(ShiroUtils.getAdminInfo().getDeptId()));
        }else{
            //其他人只查自己创建的
            queryWrapper.eq(Customer::getCreateUser, ShiroUtils.getAdminId());
        }
        queryWrapper.eq(Customer::getMark, 1);
        queryWrapper.orderByDesc(Customer::getId);

        List<Customer> customers = customerMapper.selectList(queryWrapper);
        List<CustomerDownListVo> customerListVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(customers)) {
            customers.forEach(item -> {
                CustomerDownListVo customerListVo = new CustomerDownListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, customerListVo);
                // 性别描述
                if (customerListVo.getSex()!=null&&customerListVo.getSex()>0) {
                    customerListVo.setSexName(CustomerConstant.CUSTOMER_SEX_LIST.get(customerListVo.getSex()));
                }
                // 客户类型描述
                if (customerListVo.getCustType()!=null&&customerListVo.getCustType()>0) {
                    customerListVo.setCustTypeName(CustomerConstant.CUSTOMER_CUSTTYPE_LIST.get(customerListVo.getCustType()));
                }
                // 渠道描述
                if (customerListVo.getSource()!=null&&customerListVo.getSource()>0) {
                    customerListVo.setSourceName(CustomerConstant.CUSTOMER_SOURCE_LIST.get(customerListVo.getSource()));
                }
                // 创建人名称
                if (customerListVo.getCreateUser() != null && customerListVo.getCreateUser() > 0) {
                    customerListVo.setCreateUserName(AdminUtils.getName((customerListVo.getCreateUser())));
                }
                // 修改人名称
                if (customerListVo.getUpdateUser() != null && customerListVo.getUpdateUser() > 0) {
                    customerListVo.setUpdateUserName(AdminUtils.getName((customerListVo.getUpdateUser())));
                }
                //最后探店时间
                String lastTandianTime = customerMapper.getLastTandianTime(customerListVo.getId());
                if(StringUtils.isNotEmpty(lastTandianTime)){
                    customerListVo.setLastTandianTime(DateUtils.dateTime("yyyy-MM-dd",lastTandianTime));
                }
                //探店次数
                //Integer tandianNum = customerMapper.getTandianNum(customerListVo.getId());
                //customerListVo.setTdNum(customerListVo.getTdNum());
                //年龄
                if(customerListVo.getBirthday()!=null){
                    Integer diffMonth = customerMapper.getAgeForMonth(customerListVo.getBirthday());
                    String age = diffMonth>0?diffMonth/12+"岁"+diffMonth%12+"个月":"未满月";
                    customerListVo.setAge(age);
                }
                //最后沟通时间内容
                Map<String, Object> lastGoutongInfo = customerMapper.getLastGoutongInfo(customerListVo.getId());
                if(null != lastGoutongInfo){
                    if(StringUtils.isNotEmpty((String) lastGoutongInfo.get("gtTime")))
                        customerListVo.setLastGoutongTime(DateUtils.dateTime("yyyy-MM-dd",(String) lastGoutongInfo.get("gtTime")));
                    customerListVo.setLastGoutongDesc((String) lastGoutongInfo.get("gtDesc"));
                    if(StringUtils.isNotEmpty((String) lastGoutongInfo.get("interactTime")))
                        customerListVo.setInteractTime(DateUtils.dateTime("yyyy-MM-dd",(String) lastGoutongInfo.get("interactTime")));
                    if(null!=lastGoutongInfo.get("replyFlag")){
                        customerListVo.setReplyFlag((Integer) lastGoutongInfo.get("replyFlag"));
                        customerListVo.setReplyFlagName(GoutongConstant.GOUTONG_REPLY_LIST.get((Integer) lastGoutongInfo.get("replyFlag")));
                    }
                    if(null!=lastGoutongInfo.get("interactDesc")){
                        customerListVo.setInteractDesc((String) lastGoutongInfo.get("interactDesc"));
                    }
                }
                customerListVoList.add(customerListVo);
            });
        }

        ExcelBaseParam excelBaseParam = new ExcelBaseParam();
        excelBaseParam.setResponse(response)
                .setFileName("导出客资信息记录")
                .setSheetName("客资信息");
        try{
            Workbook workbook = ExcelSimpleUtil.exportExcel(excelBaseParam.getTitleName(), excelBaseParam.getSheetName(), customerListVoList, CustomerDownListVo.class,true);
            ExcelDownloadUtil.downloadExcel(workbook,excelBaseParam.getFileName()+".xlsx",excelBaseParam.getResponse());
        }catch (IOException e){
            logger.error("excel导出出错!",e);
        }
    }
}
