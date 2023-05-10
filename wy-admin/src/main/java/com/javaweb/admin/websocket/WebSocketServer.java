package com.javaweb.admin.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.javaweb.admin.config.WebSocketConfig;
import com.javaweb.common.utils.SpringUtils;
import com.javaweb.system.constant.AdminConstant;
import com.javaweb.system.entity.Admin;
import com.javaweb.system.mapper.DepMapper;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.system.utils.ShiroUtils;
import com.javaweb.system.vo.AdminInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: leavin
 * @create: 2023-03-19 15:11
 **/
@Slf4j
@ServerEndpoint(value = "/notice/",configurator = WebSocketConfig.class)
@Component
public class WebSocketServer {

    private final static Log logger = LogFactory.getLog(WebSocketServer.class);

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";

    private static WebSocketConfig webSocketConfig = SpringUtils.getBean(WebSocketConfig.class);
    private static ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringUtils.getBean(ThreadPoolTaskExecutor.class);

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Admin user = (Admin)session.getUserProperties().get("user");
        this.userId = user.getId().toString();
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        logger.info("用户连接: "+ AdminUtils.getAdminInfo(user.getId()).getUsername()+",当前在线人数为: "+getOnlineCount());
//        try {
//            sendMessage("连接成功");
//        } catch (IOException e) {
//            log.error("用户:{},网络异常!!!!!!",userId);
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    logger.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 接受待推送消息，并进行分发
     * */
    public static <T> void sendHandler(WebSocketMessage<T> webSocketMessage){
        List<CompletableFuture<Void>> taskList = new ArrayList<>();
        for (Map.Entry<String, WebSocketServer> entry : webSocketMap.entrySet()) {
            String userId = entry.getKey();
            Boolean ifSendMessage = webSocketMessage.getIfSendMessage().apply(Integer.valueOf(userId));//判断是否可以推送
            AdminInfoVo adminInfoVo = AdminUtils.getAdminInfo(Integer.valueOf(userId));
            logger.info("【沟通计划消息推送】用户："+adminInfoVo.getUsername()+" ，推送标志："+ifSendMessage);
            if(ifSendMessage){
                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                    List<T> filterList = new ArrayList<>();
                    //除了管理员和超级管理员看全部，其他的看自己的。
                    int i = AdminUtils.hasRoleAnyMatch(Integer.valueOf(userId),"管理员","超级管理员")?1:2;
                    switch (i) {
                        case 1:
                            if(webSocketMessage.getList().size()<=20) filterList = webSocketMessage.getList();
                            else filterList = webSocketMessage.getList().subList(0, 20);//管理员只选20条进行推送
                            break;
                        case 2:
                            filterList = webSocketMessage.getUserFilter().apply(adminInfoVo.getUsername());
                            break;

                    }
                    if (filterList.size() > webSocketConfig.getNoticeLimit()) {
                        filterList = filterList.subList(0, webSocketConfig.getNoticeLimit());
                        log.info("【沟通计划消息推送】用户：{} 共有{}条沟通计划信息，超过{}上限，只选取{}条进行推送", adminInfoVo.getUsername(), filterList.size(), webSocketConfig.getNoticeLimit(), webSocketConfig.getNoticeLimit());
                    } else {
                        log.info("【沟通计划消息推送】用户：{} 共有{}条沟通计划信息", adminInfoVo.getUsername(), filterList.size());
                    }
                    List<List<T>> lists = sliceList(filterList, webSocketConfig.getMaxNotice());
                    sendSliceList(lists, userId);
                }, threadPoolTaskExecutor);
                taskList.add(completableFuture);
            }
        }
        taskList.forEach(CompletableFuture::join);
    }

    /**
     * 发送拆分的list
     * @param lists
     * @param userId
     * @param <T>
     * @throws Exception
     */
    private static <T> void sendSliceList(List<List<T>> lists,String userId){
        for (List<T> list : lists) {
            for (T t : list) {
                String message = JSONObject.toJSONString(t);
                try {
                    webSocketMap.get(userId).sendMessage(message);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                try {
                    Thread.sleep(webSocketConfig.getTime());
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            try {
                Thread.sleep(webSocketConfig.getMaxTime());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 拆分list
     * @param list
     * @param maxCount
     * @return
     */
    private static  <T> List<List<T>> sliceList(List<T> list,int maxCount){
        List<List<T>> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            double ceil = Math.ceil((double) list.size() / maxCount);
            for(int i=0;i<ceil;i++){
                List<T> custWarnCapitals = new ArrayList<>(list.subList(i*maxCount,(i+1)*maxCount > list.size() ? list.size():(i+1)*maxCount));
                result.add(custWarnCapitals);
            }
        }
        return result;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

