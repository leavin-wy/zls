package com.javaweb.system.service.impl;

import com.javaweb.common.enums.Constants;
import com.javaweb.common.exception.user.CaptchaException;
import com.javaweb.common.exception.user.UserNotExistsException;
import com.javaweb.common.utils.*;
import com.javaweb.system.dto.LoginDto;
import com.javaweb.system.entity.Admin;
import com.javaweb.system.manager.AsyncFactory;
import com.javaweb.system.manager.AsyncManager;
import com.javaweb.system.service.IAdminService;
import com.javaweb.system.service.ILoginService;
import com.javaweb.system.utils.ShiroUtils;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 后台用户管理表 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2020-03-31
 */
@Service
public class LoginServiceImpl implements ILoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private IAdminService adminService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取验证码
     *
     * @param response 请求响应
     * @return
     */
    @Override
    public JsonResult captcha(HttpServletResponse response) {
        VerifyUtil verifyUtil = new VerifyUtil();
        Map<String, String> result = new HashMap();
        try {
            String key = UUID.randomUUID().toString();
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");
            // 返回base64
            //写入redis缓存
            Map<String, String> mapInfo = verifyUtil.getRandomCodeBase64();
            String randomStr = mapInfo.get("randomStr");
            logger.info("redis key is : " + key + " , value : " + randomStr);
            redisUtils.set(key, randomStr, 60 * 5);
            result.put("url", "data:image/png;base64," + mapInfo.get("img"));
            result.put("key", key);
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("操作成功", result);
    }

    /**
     * 系统登录
     *
     * @param loginDto 登录参数
     * @return
     */
    @Override
    public JsonResult login(HttpServletRequest request, LoginDto loginDto) {
        // 验证验证码是否为空
        if (StringUtils.isEmpty(loginDto.getCaptcha())) {
            return JsonResult.error("验证码不能为空");
        }
        // 验证码校验
        if (!CaptchaUtil.ver(loginDto.getCaptcha(), request)) {
            CaptchaUtil.clear(request);  // 清除session中的验证码
            return JsonResult.error("验证码不正确");
        }
        String host = request.getRemoteHost();
        String redisKey = loginDto.getUsername()+"-"+host;
        try {
            //验证身份和登陆
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(loginDto.getUsername(), loginDto.getPassword(), loginDto.isRememberMe());
            // 设置记住我
            token.setRememberMe(loginDto.isRememberMe());
            //进行登录操作
            subject.login(token);
            //用于触发realm中的授权doGetAuthorizationInfo()方法
            subject.hasRole("登录");
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("token", SecurityUtils.getSubject().getSession().getId().toString());


            HttpSession session = request.getSession();
            session.setAttribute("loginUserId", loginDto.getUsername());
            session.setAttribute("kickout", false);
            redisUtils.set("loginUser:" + loginDto.getUsername(), session.getId());
            redisUtils.set(redisKey, 0);
            return JsonResult.success("操作成功", result);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage());
            return JsonResult.error("用户不存在/密码错误");
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public JsonResult logout() {
        // 获取当前登录人信息
        Admin user = ShiroUtils.getAdminInfo();
        // 记录用户退出日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUsername(), Constants.LOGOUT, "退出成功"));
        // 退出登录
        ShiroUtils.logout();
        return JsonResult.success("注销成功");
    }

    /**
     * 系统登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public Admin login(String username, String password) {
        // 用户名和验证码校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 获取验证码
        String captcha = ServletUtils.getRequest().getSession().getAttribute("captcha").toString();
        if (StringUtils.isEmpty(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 验证码校验
        if (!CaptchaUtil.ver(captcha, ServletUtils.getRequest())) {
            // 验证码校验
            CaptchaUtil.clear(ServletUtils.getRequest());  // 清除session中的验证码
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(captcha, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }

        // 查询用户信息
        Admin user = adminService.getAdminByUsername(username);
        if (user == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        // 判断用户状态
        if (user.getStatus() != 1) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new LockedAccountException();
        }

        // 创建登录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        return user;
    }

}
