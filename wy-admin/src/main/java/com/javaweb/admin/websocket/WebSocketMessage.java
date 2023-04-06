package com.javaweb.admin.websocket;

import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * @author myccb
 * @since 2021/5/21 9:35
 */
@Data
public class WebSocketMessage<T> {

    private List<T> list;

    private Function<String,List<T>> userFilter;//用户登录名称过滤

    private Function<List<String>,List<T>> orgFilter;//机构过滤

    private Function<Integer,Boolean> ifSendMessage;//是否进行消息推送

}
