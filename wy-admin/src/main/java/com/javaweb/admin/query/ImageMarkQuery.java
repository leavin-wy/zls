package com.javaweb.admin.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 图片标注信息查询条件
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
@Data
public class ImageMarkQuery extends BaseQuery {

    /**
     * 图片缺陷等级: 1=B(高) 2=C(中) 3=D(低)
     */
    private String imgDefectLevel;

}
