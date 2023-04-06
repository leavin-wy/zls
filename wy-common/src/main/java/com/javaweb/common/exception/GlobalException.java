package com.javaweb.common.exception;

import com.javaweb.common.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author leavin
 * @date 2023-03-29 20:51
 * @desc
 */
@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult exception(Exception e) {
        e.printStackTrace();
        log.error("系统全局发生错误:[出错原因:{},出错信息:{}]",e.getCause(),e.getMessage());
        if (e instanceof AuthorizationException){
            return JsonResult.error("无权限操作");
        }
        return JsonResult.error("系统异常");
    }
}
