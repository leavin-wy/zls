package com.javaweb.admin.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 图片信息查询条件
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Data
public class ImagesQuery extends BaseQuery {

    private Integer imgGroup;
    private String groupId;
    private String imgMark;

}
