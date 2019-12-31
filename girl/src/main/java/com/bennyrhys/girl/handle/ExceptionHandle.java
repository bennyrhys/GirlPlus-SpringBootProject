package com.bennyrhys.girl.handle;

import com.bennyrhys.girl.domain.Result;
import com.bennyrhys.girl.exception.GirlException;
import com.bennyrhys.girl.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常捕获
 */
@ControllerAdvice
public class ExceptionHandle {
    //日志
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    //声明捕获哪个类
    @ExceptionHandler(value = Exception.class)
    @ResponseBody //因为要返回给浏览器，上面又没写@restcontroller
    public Result handle(Exception e){
        //捕获时，判断一下异常是否是自己定义的GirlException
        if (e instanceof GirlException){
            GirlException girlException = (GirlException) e;
            return ResultUtil.error(girlException.getCode(),girlException.getMessage());
        }else {
            //将未知异常通过日志排除
            logger.error("[系统异常]={}",e);
            return ResultUtil.error(-1,"未知错误");
        }
    }
}
