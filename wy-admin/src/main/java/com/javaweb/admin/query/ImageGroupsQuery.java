package com.javaweb.admin.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 图片分组信息查询条件
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Data
public class ImageGroupsQuery extends BaseQuery {
    private String groupName;
}
