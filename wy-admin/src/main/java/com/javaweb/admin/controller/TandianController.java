package com.javaweb.admin.controller;


import com.javaweb.admin.mapper.TandianMapper;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.Tandian;
import com.javaweb.admin.query.TandianQuery;
import com.javaweb.admin.service.ITandianService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.javaweb.common.common.BaseController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 探店记录 控制器
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Controller
@RequestMapping("/tandian")
public class TandianController extends BaseController {

    @Autowired
    private ITandianService tandianService;
    @Autowired
    private TandianMapper tandianMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(TandianQuery query) {
        return tandianService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:tandian:add")
    @Log(title = "探店记录", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody Tandian entity) {
        return tandianService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:tandian:update")
    @Log(title = "探店记录", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody Tandian entity) {
        return tandianService.edit(entity);
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
            info = tandianService.info(id);
        }
        model.addAttribute("info", info);
        return super.edit(id, model);
    }

    @GetMapping("/indexTd")
    public String indexTd(Integer custId, Model model) {
        model.addAttribute("custId",custId);
        return "tandian/index";
    }

    @GetMapping("/addTandian")
    public String addTandian(Integer custId, Model model) {
        Map<String, Object> info = new HashMap<>();
        info.put("custId",custId);
        model.addAttribute("info",info);
        return "tandian/edit";
    }

    /**
     * 删除记录
     *
     * @param id 记录ID
     * @return
     */
    @RequiresPermissions("sys:tandian:delete")
    @Log(title = "探店记录", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult delete(@PathVariable("id") Integer id) {
        Tandian entity = tandianService.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        //删除探店记录，探店次数减1
        tandianMapper.updateTdNumReduce(entity.getCustId());
        return tandianService.delRealById(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @RequiresPermissions("sys:tandian:batchDelete")
    @Log(title = "探店记录", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return tandianService.delRealByIds(ids);
    }

}
