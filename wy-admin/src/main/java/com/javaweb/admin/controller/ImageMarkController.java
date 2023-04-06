package com.javaweb.admin.controller;


import com.javaweb.admin.entity.Images;
import com.javaweb.admin.mapper.ImagesMapper;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.admin.entity.ImageMark;
import com.javaweb.admin.query.ImageMarkQuery;
import com.javaweb.admin.service.IImageMarkService;
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
 * 图片标注信息 控制器
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
@Controller
@RequestMapping("/imagemark")
public class ImageMarkController extends BaseController {

    @Autowired
    private IImageMarkService imageMarkService;
    @Autowired
    private ImagesMapper imagesMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(ImageMarkQuery query) {
        return imageMarkService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
    @Log(title = "图片标注信息", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody ImageMark entity) {
        return imageMarkService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
    @Log(title = "图片标注信息", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody ImageMark entity) {
        return imageMarkService.edit(entity);
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
            info = imageMarkService.info(id);
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
    @Log(title = "图片标注信息", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return imageMarkService.delImageInfo(id);
    }
	
	/**
     * 批量删除
     *
     * @param ids 记录ID
     * @return
     */
    @Log(title = "图片标注信息", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return imageMarkService.delRealByIds(ids);
    }

    /**
     * 跳转图片列表页面
     * @return
     */
    @GetMapping("/imageListPage")
    public String imageListPage() {
        return "imagemark/imageList";
    }

    /**
     * 获取图片详情
     *
     * @param id    记录ID
     * @param model 模型
     * @return
     */
    @GetMapping("/editImage")
    public String editImage(Integer id, Model model) {
        Map<String, Object> info = new HashMap<>();
        if (id != null && id > 0) {
            Images images = imagesMapper.selectById(id);
            info.put("id",id);
            info.put("imgUrl",images.getImgUrl());
            info.put("imgGroup",images.getImgGroup());
            info.putAll(imageMarkService.queryMark(id));
        }
        model.addAttribute("info", info);
        return "imagemark/imageMarkInfo";
    }

    /**
     * 删除图片信息
     *
     * @param id
     * @return
     */
    @Log(title = "删除图片信息", businessType = BusinessType.DELETE)
    @ResponseBody
    @PostMapping("/delImage")
    public JsonResult delImage(Integer id) {
        return JsonResult.success();
    }

    /**
     * 保存图片标注信息
     *
     * @param param
     * @return
     */
    @Log(title = "保存图片标注信息", businessType = BusinessType.OTHER)
    @ResponseBody
    @PostMapping("/saveMark")
    public JsonResult saveMark(@RequestBody Map<String, Object> param) {
        return imageMarkService.saveMark(param);
    }
}
