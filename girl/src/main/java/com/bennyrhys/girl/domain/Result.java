package com.bennyrhys.girl.domain;

/**
 * http请求返回对最外层对象
 */
//get/set
public class Result<T> {//具体内容用范型表示
    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //具体内容
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
