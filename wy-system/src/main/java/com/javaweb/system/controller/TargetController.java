package com.javaweb.system.controller;

import com.javaweb.common.annotation.Log;
import com.javaweb.common.common.BaseController;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.system.entity.Target;
import com.javaweb.system.query.TargetQuery;
import com.javaweb.system.service.ITargetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客资指标结果 控制器
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
@Controller
@RequestMapping("/target")
public class TargetController extends BaseController {

    @Autowired
    private ITargetService targetService;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(TargetQuery query) {
        return targetService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:target:add")
    @Log(title = "客资指标结果", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody Target entity) {
        return targetService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:target:update")
    @Log(title = "客资指标结果", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody Target entity) {
        return targetService.edit(entity);
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
            info = targetService.info(id);
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
    @RequiresPermissions("sys:target:delete")
    @Log(title = "客资指标结果", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return targetService.delRealById(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @RequiresPermissions("sys:target:batchDelete")
    @Log(title = "客资指标结果", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return targetService.delRealByIds(ids);
    }

}
