package com.javaweb.system.utils;

import com.javaweb.common.utils.SpringUtils;
import com.javaweb.system.entity.Admin;
import com.javaweb.system.entity.Dep;
import com.javaweb.system.entity.Role;
import com.javaweb.system.mapper.AdminMapper;
import com.javaweb.system.mapper.RoleMapper;
import com.javaweb.system.service.impl.DepServiceImpl;
import com.javaweb.system.vo.AdminInfoVo;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AdminUtils {

    /**
     * 根据ID获取人员名称
     *
     * @param id 人员ID
     * @return
     */
    public static String getName(Integer id) {
        AdminMapper adminMapper = SpringUtils.getBean(AdminMapper.class);
        Admin admin = adminMapper.selectById(id);
        return admin.getRealname();
    }

    /**
     * 根据ID获取人员信息
     *
     * @param id 人员ID
     * @return
     */
    public static AdminInfoVo getAdminInfo(Integer id) {
        AdminMapper adminMapper = SpringUtils.getBean(AdminMapper.class);
        Admin admin = adminMapper.selectById(id);
        AdminInfoVo adminInfoVo = new AdminInfoVo();
        if(admin == null) return adminInfoVo;
        DepServiceImpl depService = SpringUtils.getBean(DepServiceImpl.class);
        Dep dep = (Dep) depService.getInfo(admin.getDeptId());
        if(dep == null) return adminInfoVo;
        BeanUtils.copyProperties(admin, adminInfoVo);
        adminInfoVo.setDeptName(dep.getName());
        return adminInfoVo;
    }

    /**
     * 判断人员是否拥至少一个角色
     */
    public static boolean hasRoleAnyMatch(Integer userId,String... roleNames){
        RoleMapper roleMapper = SpringUtils.getBean(RoleMapper.class);
        AdminInfoVo adminInfoVo = getAdminInfo(userId);
        List<String> roleList = Arrays.asList(roleNames);
        List<String> roleIdList = Arrays.asList(adminInfoVo.getRoleIds().split(","));
        List<Role> rolesByRoleIds = roleMapper.getRolesByRoleIds(roleIdList);
        Optional<Role> result = rolesByRoleIds.stream().filter(row->roleList.contains(row.getName())).findFirst();
        return result.isPresent();
    }
}
