package com.javaweb.admin.controller;


import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.Goutong;
import com.javaweb.admin.query.GoutongQuery;
import com.javaweb.admin.service.IGoutongService;
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
 * 沟通记录 控制器
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Controller
@RequestMapping("/goutong")
public class GoutongController extends BaseController {

    @Autowired
    private IGoutongService goutongService;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(GoutongQuery query) {
        return goutongService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:goutong:add")
    @Log(title = "沟通记录", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody Goutong entity) {
        return goutongService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @RequiresPermissions("sys:goutong:update")
    @Log(title = "沟通记录", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody Goutong entity) {
        return goutongService.edit(entity);
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
            info = goutongService.info(id);
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
    @RequiresPermissions("sys:goutong:delete")
    @Log(title = "沟通记录", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return goutongService.delRealById(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @RequiresPermissions("sys:goutong:batchDelete")
    @Log(title = "沟通记录", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return goutongService.delRealByIds(ids);
    }

    @GetMapping("/indexGt")
    public String indexGt(Integer custId, Model model) {
        model.addAttribute("custId",custId);
        return "goutong/index";
    }

    @GetMapping("/index")
    @Override
    public String index(Model model) {
        return "goutong/indexGt";
    }

    @GetMapping("/addGoutong")
    public String addGoutong(Integer custId, Model model) {
        Map<String, Object> info = new HashMap<>();
        info.put("custId",custId);
        model.addAttribute("info",info);
        return "goutong/edit";
    }

}
