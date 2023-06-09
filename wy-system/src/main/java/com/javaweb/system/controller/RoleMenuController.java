package com.javaweb.system.controller;


import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.annotation.Log;
import com.javaweb.common.enums.BusinessType;
import com.javaweb.system.dto.RolePermissionDto;
import com.javaweb.system.entity.RoleMenu;
import com.javaweb.system.query.RoleMenuQuery;
import com.javaweb.system.service.IRoleMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.javaweb.common.common.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单关系 控制器
 * </p>
 *
 * @author leavin
 * @since 2020-05-08
 */
@Controller
@RequestMapping("/rolemenu")
public class RoleMenuController extends BaseController {

    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
//    @RequiresPermissions("sys:rolemenu:list")
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(RoleMenuQuery query) {
        return roleMenuService.getList(query);
    }

    /**
     * 添加记录
     *
     * @param entity 实体对象
     * @return
     */
//    @RequiresPermissions("sys:rolemenu:add")
    @Log(title = "角色菜单关系", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/add")
    public JsonResult add(@RequestBody RoleMenu entity) {
        return roleMenuService.edit(entity);
    }

    /**
     * 修改记录
     *
     * @param entity 实体对象
     * @return
     */
//    @RequiresPermissions("sys:rolemenu:update")
    @Log(title = "角色菜单关系", businessType = BusinessType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public JsonResult update(@RequestBody RoleMenu entity) {
        return roleMenuService.edit(entity);
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
            info = roleMenuService.info(id);
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
//    @RequiresPermissions("sys:rolemenu:delete")
    @Log(title = "角色菜单关系", businessType = BusinessType.DELETE)
    @ResponseBody
    @GetMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        return roleMenuService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 记录ID(多个使用逗号","分隔)
     * @return
     */
//    @RequiresPermissions("sys:rolemenu:batchDelete")
    @Log(title = "角色菜单关系", businessType = BusinessType.BATCH_DELETE)
    @ResponseBody
    @GetMapping("/batchDelete/{ids}")
    public JsonResult batchDelete(@PathVariable("ids") String ids) {
        return roleMenuService.deleteByIds(ids);
    }

    /**
     * 根据角色ID获取菜单列表
     *
     * @param roleId 角色ID
     * @return
     */
    @ResponseBody
    @GetMapping("/getRolePermissionByRoleId")
    public JsonResult getRolePermissionByRoleId(Integer roleId) {
        return roleMenuService.getRolePermissionByRoleId(roleId);
    }

    /**
     * 设置角色权限
     *
     * @param rolePermissionDto 角色菜单权限
     * @return
     */
    @ResponseBody
    @PostMapping("/setRolePermission")
    public JsonResult setRolePermission(@RequestBody RolePermissionDto rolePermissionDto) {

        return roleMenuService.setRolePermission(rolePermissionDto);
    }

}
