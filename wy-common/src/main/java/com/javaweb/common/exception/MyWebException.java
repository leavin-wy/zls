package com.javaweb.common.exception;

/**
 * @author leavin
 * @createTime 2023/03/17 10:59
 * @description
 */
public class MyWebException extends RuntimeException {

    private static final long serialVersionUID = 3844100562400725986L;
    private Integer errorCode;
    private String msg;

    public MyWebException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyWebException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}