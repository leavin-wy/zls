package com.javaweb.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 图片标注信息列表Vo
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
@Data
public class ImageMarkListVo {

    /**
     * 图片标注信息ID
     */
    private Integer id;

    /**
     * 图片id
     */
    private Integer imgId;

    /**
     * 图片缺陷类型
     */
    private String imgDefectType;

    /**
     * 图片缺陷等级: 1=B(高) 2=C(中) 3=D(低)
     */
    private String imgDefectLevel;

    /**
    * 图片缺陷等级描述
    */
    private String imgDefectLevelName;

    /**
     * 图片缺陷(多个规则逗号隔开)
     */
    private String imgDefect;

    /**
     * 备注
     */
    private String note;

    /**
     * 添加人
     */
    private Integer createUser;

    /**
     * 添加人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新人名称
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

}