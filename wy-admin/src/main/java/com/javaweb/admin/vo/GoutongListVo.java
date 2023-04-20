package com.javaweb.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 沟通记录列表Vo
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Data
public class GoutongListVo {

    /**
     * 沟通记录ID
     */
    private Integer id;

    /**
     * 客户id
     */
    private Integer custId;

    /**
     * 沟通时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date gtTime;

    /**
     * 下次沟通时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date interactTime;

    /**
     * 下次沟通内容
     */
    private String interactDesc;

    /**
     * 沟通内容
     */
    private String gtDesc;

    /**
     * 回复标志:1=未回复 2=已回复
     */
    private Integer replyFlag;
    private String replyFlagStr;


    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updateUser;

    /**
     * 修改人名称
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

}