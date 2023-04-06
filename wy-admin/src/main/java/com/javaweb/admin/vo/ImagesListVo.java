package com.javaweb.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 图片信息列表Vo
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Data
public class ImagesListVo {

    /**
     * 图片信息ID
     */
    private Integer id;

    /**
     * 图片
     */
    private String imgUrl;
    private String avatarUrl;

    private Integer imgGroup;
    private String imgGroupName;

    /**
     * 图片是否标注： 1=已标注 2=未标注
     */
    private String imgMark;

    /**
    * 图片是否标注描述
    */
    private String imgMarkName;

    /**
     * 图片缺陷(多个规则逗号“,”隔开)
     */
    private String imgDefect;

    /**
     * 备注
     */
    private String note;

    /**
     * 显示顺序
     */
    private Integer sort;

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