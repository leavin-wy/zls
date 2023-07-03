package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.system.common.BaseServiceImpl;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.CommonUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.admin.constant.TandianConstant;
import com.javaweb.admin.entity.Tandian;
import com.javaweb.admin.mapper.TandianMapper;
import com.javaweb.admin.query.TandianQuery;
import com.javaweb.admin.service.ITandianService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.TandianListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 探店记录 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Service
public class TandianServiceImpl extends BaseServiceImpl<TandianMapper, Tandian> implements ITandianService {

    @Autowired
    private TandianMapper tandianMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        TandianQuery tandianQuery = (TandianQuery) query;
        // 查询条件
        QueryWrapper<Tandian> queryWrapper = new QueryWrapper<>();
        if(tandianQuery.getCustId()!=null&&tandianQuery.getCustId()>0){
            queryWrapper.eq("cust_id",tandianQuery.getCustId());
        }
        queryWrapper.eq("mark", 1);
        queryWrapper.orderByDesc("create_time");

        // 查询数据
        IPage<Tandian> page = new Page<>(tandianQuery.getPage(), tandianQuery.getLimit());
        IPage<Tandian> data = tandianMapper.selectPage(page, queryWrapper);
        List<Tandian> tandianList = data.getRecords();
        List<TandianListVo> tandianListVoList = new ArrayList<>();
        if (!tandianList.isEmpty()) {
            tandianList.forEach(item -> {
                TandianListVo tandianListVo = new TandianListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, tandianListVo);
                // 创建人名称
                if (tandianListVo.getCreateUser() != null && tandianListVo.getCreateUser() > 0) {
                    tandianListVo.setCreateUserName(AdminUtils.getName((tandianListVo.getCreateUser())));
                }
                // 修改人名称
                if (tandianListVo.getUpdateUser() != null && tandianListVo.getUpdateUser() > 0) {
                    tandianListVo.setUpdateUserName(AdminUtils.getName((tandianListVo.getUpdateUser())));
                }
                tandianListVoList.add(tandianListVo);
            });
        }
        return JsonResult.success("操作成功", tandianListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        Tandian entity = (Tandian) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult edit(Tandian entity) {
        if(entity.getId()==null||entity.getId()<1){
            //新增探店，探店次数加1
            tandianMapper.updateTdNumAdd(entity.getCustId());
        }
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
        Tandian entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

}