package com.javaweb.admin.controller;

import com.javaweb.admin.config.WebSocketConfig;
import com.javaweb.common.utils.JsonResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program:
 * @description:
 * @author: leavin
 * @create: 2023-03-19 14:58
 **/
@RequestMapping("/websocket")
@RestController
public class WebSocketController {

    private final static Log logger = LogFactory.getLog(WebSocketController.class);

    @Value("${serverIp}")
    private String serverIp;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @GetMapping("/config")
    public JsonResult config(){
        Map<String,String> map = new HashMap<>();
        map.put("port",webSocketConfig.getPort());
        map.put("ip", serverIp);
        logger.info("websocket获取地址信息：" + map.toString());
        return JsonResult.success("",map);
    }

}

