package com.javaweb.system.controller;


import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.system.entity.ConfigGroup;
import com.javaweb.system.query.ConfigGroupQuery;
import com.javaweb.system.service.IConfigGroupService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.javaweb.common.common.BaseController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 配置分组 控制器
 * </p>
 *
 * @author leavin
 * @since 2020-04-20
 */
@Controller
@RequestMapping("/configgroup")
public class ConfigGroupController extends BaseController {

    @Autowired
    private IConfigGroupService configGroupService;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
//    @RequiresPermissions("sys:configgroup:list")
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(ConfigGroupQuery query) {
        return configGroupService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
//    @RequiresPermissions("sys:configgroup:add")
    @Log(title = "配置分组", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody ConfigGroup entity) {
        return configGroupService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
//    @RequiresPermissions("sys:configgroup:edit")
    @Log(title = "配置分组", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody ConfigGroup entity) {
        return configGroupService.edit(entity);
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
            info = configGroupService.info(id);
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
//    @RequiresPermissions("sys:configgroup:delete")
    @Log(title = "配置分组", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return configGroupService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 记录ID(多个使用逗号","分隔)
     * @return
     */
//    @RequiresPermissions("sys:configgroup:batchDelete")
    @Log(title = "配置分组", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return configGroupService.deleteByIds(ids);
    }

}
