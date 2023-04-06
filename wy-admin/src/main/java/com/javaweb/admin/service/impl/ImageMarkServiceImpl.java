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
import com.javaweb.admin.constant.ImageMarkConstant;
import com.javaweb.admin.entity.ImageMark;
import com.javaweb.admin.mapper.ImageMarkMapper;
import com.javaweb.admin.query.ImageMarkQuery;
import com.javaweb.admin.service.IImageMarkService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.ImageMarkListVo;
import com.javaweb.system.utils.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 图片标注信息 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
@Service
public class ImageMarkServiceImpl extends BaseServiceImpl<ImageMarkMapper, ImageMark> implements IImageMarkService {

    @Autowired
    private ImageMarkMapper imageMarkMapper;
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
        ImageMarkQuery imageMarkQuery = (ImageMarkQuery) query;
        // 查询条件
        QueryWrapper<ImageMark> queryWrapper = new QueryWrapper<>();
        // 图片缺陷等级: 1=B(高) 2=C(中) 3=D(低)
        if (!StringUtils.isEmpty(imageMarkQuery.getImgDefectLevel())) {
            queryWrapper.eq("img_defect_level", imageMarkQuery.getImgDefectLevel());
        }
        queryWrapper.eq("mark", 1);
        queryWrapper.orderByDesc("id");

        // 查询数据
        IPage<ImageMark> page = new Page<>(imageMarkQuery.getPage(), imageMarkQuery.getLimit());
        IPage<ImageMark> data = imageMarkMapper.selectPage(page, queryWrapper);
        List<ImageMark> imageMarkList = data.getRecords();
        List<ImageMarkListVo> imageMarkListVoList = new ArrayList<>();
        if (!imageMarkList.isEmpty()) {
            imageMarkList.forEach(item -> {
                ImageMarkListVo imageMarkListVo = new ImageMarkListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, imageMarkListVo);
                // 图片缺陷等级描述
                if (!StringUtils.isEmpty(imageMarkListVo.getImgDefectLevel())) {
                    imageMarkListVo.setImgDefectLevelName(ImageMarkConstant.IMAGEMARK_IMGDEFECTLEVEL_LIST.get(imageMarkListVo.getImgDefectLevel()));
                }
                // 添加人名称
                if (imageMarkListVo.getCreateUser() != null && imageMarkListVo.getCreateUser() > 0) {
                    imageMarkListVo.setCreateUserName(AdminUtils.getName((imageMarkListVo.getCreateUser())));
                }
                // 更新人名称
                if (imageMarkListVo.getUpdateUser() != null && imageMarkListVo.getUpdateUser() > 0) {
                    imageMarkListVo.setUpdateUserName(AdminUtils.getName((imageMarkListVo.getUpdateUser())));
                }
                imageMarkListVoList.add(imageMarkListVo);
            });
        }
        return JsonResult.success("操作成功", imageMarkListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        ImageMark entity = (ImageMark) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(ImageMark entity) {
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
        ImageMark entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

    /**
     * 保存标注
     * @param param
     * @return
     */
    @Override
    public JsonResult saveMark(Map<String, Object> param) {
        Integer imgId = Integer.parseInt((String) param.get("id"));
        QueryWrapper<ImageMark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("img_id",imgId);
        queryWrapper.in("img_defect_type",ImageMarkConstant.DEFECTTYPES);
        imageMarkMapper.delete(queryWrapper);

        //表面缺欠
        String bmqqs = (String)param.get(ImageMarkConstant.DEFECT_TYPE_BMQQ);
        List<Map<String,String>> bmqqLevList = (List<Map<String,String>>)param.get("bmqqLev");
        for(String bmqq : bmqqs.split(",")){
            for(Map<String,String> map : bmqqLevList){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    String bmqqLevKey = entry.getKey();
                    String bmqqLevValue = entry.getValue();
                    System.out.println(bmqqLevKey+":"+bmqqLevValue);
                    if((bmqq).equals(bmqqLevKey)){
                        //System.out.println(bmqqLevKey);
                        this.saveInfo(imgId,ImageMarkConstant.DEFECT_TYPE_BMQQ,bmqqLevValue,bmqqLevKey);
                    }
                }
            }
        }

        //几何形状缼欠
        String jhxzqqs = (String)param.get(ImageMarkConstant.DEFECT_TYPE_JHXZQQ);
        List<Map<String,String>> jhxzqqLevList = (List<Map<String,String>>)param.get("jhxzqqLev");
        for(String jhxzqq : jhxzqqs.split(",")){
            for(Map<String,String> map : jhxzqqLevList){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    String jhxzqqLevKey = entry.getKey();
                    String jhxzqqLevValue = entry.getValue();
                    System.out.println(jhxzqqLevKey+":"+jhxzqqLevValue);
                    if((jhxzqq).equals(jhxzqqLevKey)){
                        //System.out.println(jhxzqqLevKey);
                        this.saveInfo(imgId,ImageMarkConstant.DEFECT_TYPE_JHXZQQ,jhxzqqLevValue,jhxzqqLevKey);
                    }
                }
            }
        }
        //更新图片状态为已标注
        Images images = new Images();
        images.setId(imgId);
        images.setImgMark("1");
        images.setUpdateUser(ShiroUtils.getAdminId());
        images.setUpdateTime(new Date());
        imagesMapper.updateById(images);

        return JsonResult.success();
    }

    @Override
    public Map<String, String> queryMark(Integer imgId) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper<ImageMark> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("img_id",imgId);
        queryWrapper.in("img_defect_type",ImageMarkConstant.DEFECTTYPES);
        List<ImageMark> list = imageMarkMapper.selectList(queryWrapper);
        List<String> bmqqDefectList = new ArrayList<>();
        List<String> jhqqDefectList = new ArrayList<>();
        for(ImageMark imageMark : list){
            //缼欠项
            String imgDefect = imageMark.getImgDefect();
            String imgDefectType = imageMark.getImgDefectType();
            if(ImageMarkConstant.DEFECT_TYPE_BMQQ.equals(imgDefectType)){
                bmqqDefectList.add(imgDefect);
            }else if(ImageMarkConstant.DEFECT_TYPE_JHXZQQ.equals(imgDefectType)){
                jhqqDefectList.add(imgDefect);
            }
            map.put(imageMark.getImgDefectType()+"_lev_"+imgDefect,imageMark.getImgDefectLevel());
        }
        map.put(ImageMarkConstant.DEFECT_TYPE_BMQQ,String.join(",",bmqqDefectList));
        map.put(ImageMarkConstant.DEFECT_TYPE_JHXZQQ,String.join(",",jhqqDefectList));
        return map;
    }

    @Override
    public JsonResult delImageInfo(Integer imgId) {
        //删除标注信息
        QueryWrapper<ImageMark> markWrapper = new QueryWrapper<>();
        markWrapper.eq("img_id",imgId);
        imageMarkMapper.delete(markWrapper);
        //删除图片信息
        imagesMapper.deleteById(imgId);
        return JsonResult.success("操作成功");
    }

    public void saveInfo(Integer imgId, String imgDefectType, String imgDefectLevel, String imgDefect){
        ImageMark imageMark = new ImageMark();
        imageMark.setImgId(imgId);
        imageMark.setImgDefectType(imgDefectType);
        imageMark.setImgDefectLevel(imgDefectLevel);
        imageMark.setImgDefect(imgDefect);
        imageMark.setCreateUser(ShiroUtils.getAdminId());
        imageMark.setCreateTime(new Date());
        imageMark.setMark(1);
        imageMarkMapper.insert(imageMark);
    }
}