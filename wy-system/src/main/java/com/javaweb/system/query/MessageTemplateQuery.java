package com.javaweb.system.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 消息模板查询条件
 * </p>
 *
 * @author leavin
 * @since 2020-05-04
 */
@Data
public class MessageTemplateQuery extends BaseQuery {

    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板类型：1系统模板 2短信模板 3邮件模板 4微信模板 5推送模板
     */
    private Integer type;

    /**
     * 状态：1在用 2停用
     */
    private Integer status;

}
