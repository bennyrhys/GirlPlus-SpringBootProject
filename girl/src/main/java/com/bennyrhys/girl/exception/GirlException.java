package com.bennyrhys.girl.exception;

import com.bennyrhys.girl.enums.ResultEnum;

/**
 * 重写Exception-多状态码
 *
 * 注意：
 * 继承RuntimeException
 * 因为spring事务回滚针对RuntimeException，而RuntimeException继承Exception。
 * Exception不回滚
 */
public class GirlException extends RuntimeException{
    private Integer code;
    //get/set/构造

    public GirlException(ResultEnum resultEnum) {
        //添加字段message，父类本身就会传一个message进去
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
