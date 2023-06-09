package com.javaweb.system.controller;


import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.system.dto.LevelDto;
import com.javaweb.system.entity.Level;
import com.javaweb.system.query.LevelQuery;
import com.javaweb.system.service.ILevelService;
import org.apache.ibatis.annotations.Param;
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
 * 职级 控制器
 * </p>
 *
 * @author leavin
 * @since 2020-04-20
 */
@Controller
@RequestMapping("/level")
public class LevelController extends BaseController {

    @Autowired
    private ILevelService levelService;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @RequiresPermissions("sys:level:list")
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(LevelQuery query) {
        return levelService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:level:add")
    @Log(title = "职级", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody Level entity) {
        return levelService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:level:update")
    @Log(title = "职级", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody Level entity) {
        return levelService.edit(entity);
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
            info = levelService.info(id);
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
    @RequiresPermissions("sys:level:delete")
    @Log(title = "职级", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return levelService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 记录ID(多个使用逗号","分隔)
     * @return
     */
//    @RequiresPermissions("sys:level:batchDelete")
    @Log(title = "职级", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return levelService.deleteByIds(ids);
    }

    /**
     * 设置状态
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:level:status")
    @Log(title = "职级", businessType = BusinessType.STATUS)
    @ResponseBody
    @PostMapping("/setStatus")
    public JsonResult setStatus(@RequestBody Level entity) {
        return levelService.setStatus(entity);
    }

    /**
     * 批量设置状态
     *
     * @param levelDto 状态Dto
     * @return
     */
    //    @RequiresPermissions("sys:level:batchStatus")
    @Log(title = "职级", businessType = BusinessType.STATUS)
    @ResponseBody
    @PostMapping("/batchStatus")
    public JsonResult batchStatus(@RequestBody LevelDto levelDto) {
        return levelService.batchStatus(levelDto);
    }
}
