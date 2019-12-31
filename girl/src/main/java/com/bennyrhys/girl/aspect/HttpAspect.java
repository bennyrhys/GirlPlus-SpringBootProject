package com.bennyrhys.girl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截-运行前后
 */
@Aspect //aop标识
@Component //添加到容器
public class HttpAspect {

    //日志输出slf4j 注意类名切换
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);


    //提取公共方法
    //拦截方法：加两个..表示任何参数都会被拦截
    @Pointcut("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
    //先定方法不然没上方提示
    public void log(){
    }

    //@Before方法执行之前就执行了
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        //此处强转
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //import javax.servlet.http.HttpServletRequest;
        HttpServletRequest request = attributes.getRequest();
        //url
        logger.info("url={}", request.getRequestURL());
        //method
        logger.info("method={}",request.getMethod());
        //ip
        logger.info("ip={}",request.getRemoteAddr());
        //类方法 参数传入doBefore(JoinPoint joinPoint)
        //类名：getDeclaringTypeName类方法：getName
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        //参数
        logger.info("args={}", joinPoint.getArgs());
    }


    @After("log()")
    //先定方法不然没上方提示
    public void doAfter(){
        logger.info("222222222");
    }
    //日志获取响应信息
    //入参：returning 注意：toString方法，否则输出是对象
    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object){
        logger.info("response={}", object.toString());
    }
}
