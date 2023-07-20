package com.javaweb.admin.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 沟通记录查询条件
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Data
public class GoutongQuery extends BaseQuery {
    Integer custId ;

    private String keywords;
    private String gtTimeStrStart;
    private String gtTimeStrEnd;

    /**
     * 1=未回复,2=已回复
     */
    private String replyFlag;
}
