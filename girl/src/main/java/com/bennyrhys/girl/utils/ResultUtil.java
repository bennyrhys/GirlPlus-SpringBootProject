package com.bennyrhys.girl.utils;

import com.bennyrhys.girl.domain.Result;

public class ResultUtil {
    //成功-返回结果
    public static Result success(Object object){
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }
    //成功-没有返回结果
    public static Result success(){
        return success(null);
    }
    //失败
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return  result;
    }
}
