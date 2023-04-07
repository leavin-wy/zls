package com.javaweb.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 客户信息
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_customer")
public class Customer extends BaseEntity {

    @TableField(exist = false)
    private String interactTimeStr;

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 客户昵称
     */
    private String nickName;

    /**
     * 性别：1=男,2=女,3=未知
     */
    private Integer sex;

    /**
     * 出生时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date birthday;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交
     */
    private Integer custType;

    /**
     * 成交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date completeTime;

    /**
     * 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他
     */
    private Integer source;

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
     * 邀约时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date inviteTime;

    /**
     * 备注
     */
    private String remark;

}