package com.javaweb.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaweb.common.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 * 沟通记录
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_goutong")
public class Goutong extends BaseEntity {

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

    /**
     * 备注
     */
    private String remark;

}