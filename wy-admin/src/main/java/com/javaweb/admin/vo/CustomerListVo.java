package com.javaweb.admin.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 客户信息列表Vo
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
@Data
public class CustomerListVo {

    private String errorMsg;

    private String age;


    /**
     * 客户信息ID
     */
    private Integer id;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称",width = 15)
    private String custName;

    /**
     * 客户昵称
     */
    @Excel(name = "客户昵称",width = 15)
    private String nickName;

    /**
     * 性别：1=男,2=女,3=未知
     */
    private Integer sex;

    /**
     * 性别描述
     */
    @Excel(name = "性别",width = 20)
    private String sexName;

    /**
     * 出生时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @Excel(name = "出生时间",width = 15,format = "yyyy/MM/dd")
    private Date birthday;
    private String birthdayStr;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话",width = 15)
    private String phone;

    /**
     * 家庭住址
     */
    @Excel(name = "家庭住址",width = 15)
    private String address;

    /**
     * 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交
     */
    private Integer custType;

    /**
     * 客户类型描述
     */
    @Excel(name = "客户类型",width = 30)
    private String custTypeName;

    /**
     * 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他,6=新生堂,7=影耀
     */
    private Integer source;

    /**
     * 渠道描述
     */
    @Excel(name = "渠道",width = 30)
    private String sourceName;

    /**
     * 成交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @Excel(name = "成交时间",width = 15,format = "yyyy/MM/dd")
    private Date completeTime;
    private String completeTimeStr;

    /**
     * 历史探店次数
     */
    @Excel(name = "探店次数",width = 15)
    private int tdNum;


    @Excel(name = "最后探店时间",width = 15,format = "yyyy/MM/dd")
    private Date lastTandianTime;//最后探店时间
    private String lastTandianTimeStr;//最后探店时间
    private Integer tandianNum;//探店次数

    @Excel(name = "最后沟通内容",width = 15)
    private String lastGoutongDesc;//最后沟通内容
    @Excel(name = "最后沟通时间",width = 15,format = "yyyy/MM/dd")
    private Date lastGoutongTime;//最后沟通时间
    private String lastGoutongTimeStr;//最后沟通时间

    /**
     * 回复标志   1未回复   2已回复
     */
    private Integer replyFlag;
    @Excel(name = "回复标志")
    private String replyFlagName;

    /**
     * 下次沟通时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @Excel(name = "下次沟通时间",width = 15,format = "yyyy/MM/dd")
    private Date interactTime;
    private String interactTimeStr;

    /**
     * 下次沟通内容
     */
    @Excel(name = "下次沟通内容",width = 15)
    private String interactDesc;

    /**
     * 邀约时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @Excel(name = "邀约时间",width = 15,format = "yyyy/MM/dd")
    private Date inviteTime;
    private String inviteTimeStr;

    /**
     * 备注
     */
    @Excel(name = "备注",width = 15)
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