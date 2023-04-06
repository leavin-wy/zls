package com.javaweb.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaweb.admin.mapper.CustomerMapper;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.Customer;
import com.javaweb.admin.query.CustomerQuery;
import com.javaweb.admin.service.ICustomerService;
import com.javaweb.common.utils.easypoi.ExcelBaseParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.javaweb.common.common.BaseController;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
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
        Integer count = customerMapper.selectCount(custQueryWrapper);
        if(count>0){
            return JsonResult.error("相同名字和昵称的宝宝已存在");
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
    @Log(title = "客户信息模版下载", businessType = BusinessType.EXPORT)
    @GetMapping("/btnExport")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {

        ExcelBaseParam excelBaseParam = new ExcelBaseParam();
        excelBaseParam.setResponse(response)
                .setFileName("客户信息模板")
                .setSheetName("客户信息");
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
}
