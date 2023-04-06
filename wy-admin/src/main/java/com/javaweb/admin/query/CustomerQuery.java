package com.javaweb.admin.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 客户信息查询条件
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
@Data
public class CustomerQuery extends BaseQuery {

    private String keywords;

    /**
     * 性别：1=男,2=女,3=未知
     */
    private String sex;

    /**
     * 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交
     */
    private String custType;

    /**
     * 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他
     */
    private String source;

    private String interactTimeStr;

}
