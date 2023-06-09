package com.javaweb.generator.mapper;

import com.javaweb.generator.entity.GenTableColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 代码生成列 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2020-05-10
 */
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

    /**
     * 根据表名查询列信息
     *
     * @param tableName 数据表名
     * @return
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);

    /**
     * 插入数据表列
     *
     * @param genTableColumn 数据表列
     * @return
     */
    int insertGenTableColumn(GenTableColumn genTableColumn);

}
