package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.admin.entity.Customer;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.system.common.BaseServiceImpl;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.CommonUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.admin.constant.GoutongConstant;
import com.javaweb.admin.entity.Goutong;
import com.javaweb.admin.mapper.GoutongMapper;
import com.javaweb.admin.query.GoutongQuery;
import com.javaweb.admin.service.IGoutongService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.GoutongListVo;
import com.javaweb.system.utils.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 沟通记录 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Service
public class GoutongServiceImpl extends BaseServiceImpl<GoutongMapper, Goutong> implements IGoutongService {

    @Autowired
    private GoutongMapper goutongMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        GoutongQuery goutongQuery = (GoutongQuery) query;
        // 查询条件
        QueryWrapper<Goutong> queryWrapper = new QueryWrapper<>();
        if(goutongQuery.getCustId()!=null&&goutongQuery.getCustId()>0){
            queryWrapper.eq("g.cust_id",goutongQuery.getCustId());
        }
        if (!StringUtils.isEmpty(goutongQuery.getKeywords())) {
            queryWrapper.and(wrapper->wrapper.like("c.cust_name", goutongQuery.getKeywords()).or().like("c.nick_name", goutongQuery.getKeywords()));
        }
        if(StringUtils.isNotEmpty(goutongQuery.getGtTimeStrStart())&&StringUtils.isEmpty(goutongQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" DATE_FORMAT(g.create_time,'%Y-%m-%d')>='"+goutongQuery.getGtTimeStrStart()+"')");
        }else if(StringUtils.isEmpty(goutongQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(goutongQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" DATE_FORMAT(g.create_time,'%Y-%m-%d')<='"+goutongQuery.getGtTimeStrEnd()+"')");
        }else if(StringUtils.isNotEmpty(goutongQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(goutongQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" DATE_FORMAT(g.create_time,'%Y-%m-%d')>='"+goutongQuery.getGtTimeStrStart()+"' and DATE_FORMAT(g.create_time,'%Y-%m-%d')<='"+goutongQuery.getGtTimeStrEnd()+"'");
        }
        if(StringUtils.isNotEmpty(goutongQuery.getReplyFlag())){
            queryWrapper.eq("g.reply_flag",goutongQuery.getReplyFlag());
        }
        if(AdminUtils.hasRoleAnyMatch(ShiroUtils.getAdminId(),"管理员","超级管理员")){
            //管理员看同部门下所有人数据
            queryWrapper.in("g.create_user",AdminUtils.getAdminsByDep(ShiroUtils.getAdminInfo().getDeptId()));
        }else{
            //其他人只查自己创建的
            queryWrapper.eq("g.create_user", ShiroUtils.getAdminId());
        }
        queryWrapper.eq("g.mark", 1);
        queryWrapper.orderByDesc("g.id");

        // 查询数据
        IPage<Goutong> page = new Page<>(goutongQuery.getPage(), goutongQuery.getLimit());
        IPage<Goutong> data = goutongMapper.selectPage(page, queryWrapper);
        List<Goutong> goutongList = data.getRecords();
        List<GoutongListVo> goutongListVoList = new ArrayList<>();
        if (!goutongList.isEmpty()) {
            goutongList.forEach(item -> {
                GoutongListVo goutongListVo = new GoutongListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, goutongListVo);
                // 回复标志
                if (goutongListVo.getReplyFlag()!=null&&goutongListVo.getReplyFlag()>0) {
                    goutongListVo.setReplyFlagStr(GoutongConstant.GOUTONG_REPLY_LIST.get(goutongListVo.getReplyFlag()));
                }
                // 创建人名称
                if (goutongListVo.getCreateUser() != null && goutongListVo.getCreateUser() > 0) {
                    goutongListVo.setCreateUserName(AdminUtils.getName((goutongListVo.getCreateUser())));
                }
                // 修改人名称
                if (goutongListVo.getUpdateUser() != null && goutongListVo.getUpdateUser() > 0) {
                    goutongListVo.setUpdateUserName(AdminUtils.getName((goutongListVo.getUpdateUser())));
                }
                goutongListVoList.add(goutongListVo);
            });
        }
        return JsonResult.success("操作成功", goutongListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        Goutong entity = (Goutong) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(Goutong entity) {
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
        Goutong entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

}