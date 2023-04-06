package com.javaweb.admin.controller;


import com.javaweb.admin.mapper.ImageGroupsMapper;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.ImageGroups;
import com.javaweb.admin.query.ImageGroupsQuery;
import com.javaweb.admin.service.IImageGroupsService;
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
 * 图片分组信息 控制器
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Controller
@RequestMapping("/imagegroups")
public class ImageGroupsController extends BaseController {

    @Autowired
    private IImageGroupsService imageGroupsService;
    @Autowired
    private ImageGroupsMapper imageGroupsMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(ImageGroupsQuery query) {
        return imageGroupsService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @Log(title = "图片分组信息", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody ImageGroups entity) {
        return imageGroupsService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @Log(title = "图片分组信息", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody ImageGroups entity) {
        return imageGroupsService.edit(entity);
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
            info = imageGroupsService.info(id);
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
    @Log(title = "图片分组信息", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        Integer sum = imageGroupsMapper.checkGroupImg(id);
        if(sum > 0){
            return JsonResult.error("该图片分组下面已有图片，不能删除操作！");
        }
        return imageGroupsService.delRealById(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @Log(title = "图片分组信息", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return imageGroupsService.delRealByIds(ids);
    }

}
