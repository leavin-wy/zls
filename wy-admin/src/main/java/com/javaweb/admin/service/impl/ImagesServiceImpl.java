package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.admin.entity.ImageGroups;
import com.javaweb.admin.mapper.ImageGroupsMapper;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.system.common.BaseServiceImpl;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.CommonUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.admin.constant.ImagesConstant;
import com.javaweb.admin.entity.Images;
import com.javaweb.admin.mapper.ImagesMapper;
import com.javaweb.admin.query.ImagesQuery;
import com.javaweb.admin.service.IImagesService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.ImagesListVo;
import com.javaweb.system.utils.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片信息 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Service
public class ImagesServiceImpl extends BaseServiceImpl<ImagesMapper, Images> implements IImagesService {

    @Autowired
    private ImagesMapper imagesMapper;
    @Autowired
    private ImageGroupsMapper imageGroupsMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        ImagesQuery imagesQuery = (ImagesQuery) query;
        // 查询条件
        QueryWrapper<Images> queryWrapper = new QueryWrapper<>();
        // 图片分组
        if (StringUtils.isNotEmpty(imagesQuery.getGroupId())) {
            queryWrapper.eq("img_group", imagesQuery.getGroupId());
        }
        // 图片标注
        if (StringUtils.isNotEmpty(imagesQuery.getImgMark())) {
            queryWrapper.eq("img_mark", imagesQuery.getImgMark());
        }
        queryWrapper.eq("mark", 1);
        queryWrapper.orderByDesc("create_time");

        // 查询数据
        IPage<Images> page = new Page<>(imagesQuery.getPage(), imagesQuery.getLimit());
        IPage<Images> data = imagesMapper.selectPage(page, queryWrapper);
        List<Images> imagesList = data.getRecords();
        List<ImagesListVo> imagesListVoList = new ArrayList<>();
        if (!imagesList.isEmpty()) {
            imagesList.forEach(item -> {
                ImagesListVo imagesListVo = new ImagesListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, imagesListVo);

                // 图片地址
                if (!StringUtils.isEmpty(imagesListVo.getImgUrl())) {
                    imagesListVo.setAvatarUrl(CommonUtils.getImageURL(imagesListVo.getImgUrl()));
                }

                //图片分组
                Integer groupId = imagesListVo.getImgGroup();
                ImageGroups imageGroups = imageGroupsMapper.selectById(groupId);
                imagesListVo.setImgGroupName(imageGroups.getGroupName());

                // 图片是否标注
                if (!StringUtils.isEmpty(imagesListVo.getImgMark())) {
                    imagesListVo.setImgMarkName(ImagesConstant.IMAGES_IMGMARK_LIST.get(imagesListVo.getImgMark()));
                }
                // 添加人名称
                if (imagesListVo.getCreateUser() != null && imagesListVo.getCreateUser() > 0) {
                    imagesListVo.setCreateUserName(AdminUtils.getName((imagesListVo.getCreateUser())));
                }
                // 更新人名称
                if (imagesListVo.getUpdateUser() != null && imagesListVo.getUpdateUser() > 0) {
                    imagesListVo.setUpdateUserName(AdminUtils.getName((imagesListVo.getUpdateUser())));
                }
                imagesListVoList.add(imagesListVo);
            });
        }
        return JsonResult.success("操作成功", imagesListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        Images entity = (Images) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(Images entity) {
        return super.edit(entity);
    }

    /**
     * 删除记录
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public JsonResult deleteById(Integer id) {
        if (id == null || id == 0) {
            return JsonResult.error("记录ID不能为空");
        }
        Images entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

    @Override
    public JsonResult upload(Map<String, Object> map) {
        String groupId = "";//图片分组
        List<String> imgUrlList = new ArrayList<>();//图片地址
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            //System.out.println("KEY:" + key + ",值：" + value);
            if(key.equals("groupId")){
                //图片分组id
                groupId = value;
            }else if (key.contains("image")) {
                String[] stringsVal = value.split(",");
                for (String s : stringsVal) {
                    if (s.contains(CommonConfig.imageURL)) {
                        imgUrlList.add(s.replaceAll(CommonConfig.imageURL, ""));
                    } else {
                        // 已上传图片
                        imgUrlList.add(s.replaceAll(CommonConfig.imageURL, ""));
                    }
                }
            }
        }
        if(StringUtils.isEmpty(groupId)){
            return JsonResult.error("图片分组不能为空");
        }
        // 插入图片信息
        for(String str : imgUrlList){
            if(StringUtils.isEmpty(str)){
                return JsonResult.error("请先上传图片");
            }
            Images images = new Images();
            images.setImgUrl(str);
            images.setImgMark("2");//默认未标注
            images.setImgGroup(Integer.parseInt(groupId));
            images.setCreateUser(ShiroUtils.getAdminId());
            images.setCreateTime(new Date());
            this.save(images);
        }
        return JsonResult.success();
    }
}