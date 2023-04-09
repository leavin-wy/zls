package com.javaweb.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.system.entity.Target;
import com.javaweb.system.mapper.TargetMapper;
import com.javaweb.system.query.TargetQuery;
import com.javaweb.system.service.ITargetService;
import com.javaweb.system.vo.TargetListVo;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.system.common.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客资指标结果 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
@Service
public class TargetServiceImpl extends BaseServiceImpl<TargetMapper, Target> implements ITargetService {

    @Autowired
    private TargetMapper targetMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        TargetQuery targetQuery = (TargetQuery) query;
        // 查询条件
        QueryWrapper<Target> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mark", 1);
        queryWrapper.orderByDesc("id");

        // 查询数据
        IPage<Target> page = new Page<>(targetQuery.getPage(), targetQuery.getLimit());
        IPage<Target> data = targetMapper.selectPage(page, queryWrapper);
        List<Target> targetList = data.getRecords();
        List<TargetListVo> targetListVoList = new ArrayList<>();
        if (!targetList.isEmpty()) {
            targetList.forEach(item -> {
                TargetListVo targetListVo = new TargetListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, targetListVo);
                targetListVoList.add(targetListVo);
            });
        }
        return JsonResult.success("操作成功", targetListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        Target entity = (Target) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(Target entity) {
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
        Target entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

}