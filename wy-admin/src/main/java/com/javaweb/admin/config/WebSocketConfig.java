package com.javaweb.admin.config;

import com.javaweb.system.utils.ShiroUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @program: demo2
 * @description:
 * @author: leavin
 * @create: 2023-03-19 15:07
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    @Value("${server.port}")
    private String port;

    private int time = 200; //每条推送间隔 ms
    private int maxNotice = 3; //最多同间隔推送条数
    private int maxTime = 5000; //最多同间隔推送后等待时间 ms
    private int noticeLimit = 80;//最多只发送80条

    /**
     * 修改握手,就是在握手协议建立之前修改其中携带的内容
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        sec.getUserProperties().put("user", ShiroUtils.getAdminInfo());
        super.modifyHandshake(sec, request, response);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}
