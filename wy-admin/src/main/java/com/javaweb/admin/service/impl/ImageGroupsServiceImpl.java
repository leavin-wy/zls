package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.admin.entity.Images;
import com.javaweb.admin.mapper.ImagesMapper;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.system.common.BaseServiceImpl;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.CommonUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.admin.constant.ImageGroupsConstant;
import com.javaweb.admin.entity.ImageGroups;
import com.javaweb.admin.mapper.ImageGroupsMapper;
import com.javaweb.admin.query.ImageGroupsQuery;
import com.javaweb.admin.service.IImageGroupsService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.ImageGroupsListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图片分组信息 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Service
public class ImageGroupsServiceImpl extends BaseServiceImpl<ImageGroupsMapper, ImageGroups> implements IImageGroupsService {

    @Autowired
    private ImageGroupsMapper imageGroupsMapper;
    @Autowired
    private ImagesMapper imagesMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        ImageGroupsQuery imageGroupsQuery = (ImageGroupsQuery) query;
        // 查询条件
        QueryWrapper<ImageGroups> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(imageGroupsQuery.getGroupName())){
            queryWrapper.like("group_name",imageGroupsQuery.getGroupName());
        }
        queryWrapper.eq("mark", 1);
        queryWrapper.orderByDesc("id");

        // 查询数据
        IPage<ImageGroups> page = new Page<>(imageGroupsQuery.getPage(), imageGroupsQuery.getLimit());
        IPage<ImageGroups> data = imageGroupsMapper.selectPage(page, queryWrapper);
        List<ImageGroups> imageGroupsList = data.getRecords();
        List<ImageGroupsListVo> imageGroupsListVoList = new ArrayList<>();
        if (!imageGroupsList.isEmpty()) {
            imageGroupsList.forEach(item -> {
                ImageGroupsListVo imageGroupsListVo = new ImageGroupsListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, imageGroupsListVo);
                //统计图片数量
                QueryWrapper<Images> queryImages = new QueryWrapper<>();
                queryImages.eq("img_group",imageGroupsListVo.getId());
                int imagesNum = imagesMapper.selectCount(queryImages);
                imageGroupsListVo.setImageNum(imagesNum);
                //统计图片标注数量
                queryImages.eq("img_mark","1");
                int markNum = imagesMapper.selectCount(queryImages);
                imageGroupsListVo.setIsmarkNum(markNum);
                //统计图片未标注数量
                int nomarkNum = imagesNum - markNum;
                imageGroupsListVo.setNomarkNum(nomarkNum);
                // 添加人名称
                if (imageGroupsListVo.getCreateUser() != null && imageGroupsListVo.getCreateUser() > 0) {
                    imageGroupsListVo.setCreateUserName(AdminUtils.getName((imageGroupsListVo.getCreateUser())));
                }
                /*// 更新人名称
                if (imageGroupsListVo.getUpdateUser() != null && imageGroupsListVo.getUpdateUser() > 0) {
                    imageGroupsListVo.setUpdateUserName(AdminUtils.getName((imageGroupsListVo.getUpdateUser())));
                }*/
                imageGroupsListVoList.add(imageGroupsListVo);
            });
        }
        return JsonResult.success("操作成功", imageGroupsListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        ImageGroups entity = (ImageGroups) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(ImageGroups entity) {
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
        ImageGroups entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

}