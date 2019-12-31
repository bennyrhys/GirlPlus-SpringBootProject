package com.bennyrhys.girl.enums;

/**
 * 管理异常code和msg-枚举
 * 注意：枚举类型文件
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    PRIMARY_SCHOOL(100,"可能小学"),
    MIDDLE_SCHOOL(101,"可能中学"),
    ;
    private Integer code;
    private String msg;
//    构造方法（两个参数），只get方法(因为枚举都是直接用构造方法创建，不用再set了)

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
