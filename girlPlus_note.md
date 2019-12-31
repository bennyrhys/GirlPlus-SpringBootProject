[TOC]



> 作者：bennyrhys@163.com

# @Valid表单验证

## 背景假设

拦截所有未满18周岁的少女，禁止添加。

## 整理代码

建包规整代码

- domain
  - Girl

- controller
  - GirlController
  - HelloController
- service
  - GirlService
- repository
  - GirlRepository
- properties
  - GirlConfig
- GirlApplication

## git提交

> git commit -m "代码包整理"

## 新增少女：参数换成对象

GirlController 

//原本参数获取太过繁琐

```java
@RequestParam("cupSize") String cupSize,
@RequestParam("age") Integer age
```

//修改参数，传入对象

```java
/**
 * 新增一个女生
 * http://localhost:8081/girl/girls
 * 注意：post请求
 *
 * 参数
 * cupSize  f
 * age  16
 *
 * 插入因为自增注意id冲突
 */
@PostMapping(value = "/girls")
public Girl girlAdd(Girl girl){
    girl.setCupSize(girl.getCupSize());
    girl.setAge(girl.getAge());
    //save返回添加对对象
    return repository.save(girl);
}
```

//测试

http://localhost:8081/girl/girls

{

​    "id": 8,

​    "cupSize": "C",

​    "age": 22

}

## 对象：字段进行限制

Girl

```java
@Min(value = 18, message = "未成年少女禁止入内")
private Integer age;
```

GirlController

```java
/**
 * 新增一个女生
 * http://localhost:8081/girl/girls
 * 注意：post请求
 *
 * 参数
 * cupSize  f
 * age  16
 *
 * 插入因为自增注意id冲突
 */
@PostMapping(value = "/girls")
//@Valid表示要验证的是这个对象 BindingResult返回验证信息
public Girl girlAdd(@Valid Girl girl, BindingResult bindingResult){
    //判断是否发生错误
    if (bindingResult.hasErrors()){
        //打印错误信息
        System.out.println(bindingResult.getFieldError().getDefaultMessage());
        return null;
    }
    girl.setCupSize(girl.getCupSize());
    girl.setAge(girl.getAge());
    //save返回添加对对象
    return repository.save(girl);
}
```



请求postman

http://localhost:8081/girl/girls

参数age小于18时提示禁止信息



控制台输出

>2019-12-31 09:15:01.151  INFO 1327 --- [nio-8081-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 9 ms
>Hibernate: select next_val as id_val from hibernate_sequence for update
>Hibernate: update hibernate_sequence set next_val= ? where next_val=?
>Hibernate: insert into girl (age, cup_size, id) values (?, ?, ?)
>Hibernate: select next_val as id_val from hibernate_sequence for update
>Hibernate: update hibernate_sequence set next_val= ? where next_val=?
>Hibernate: insert into girl (age, cup_size, id) values (?, ?, ?)
>未成年少女禁止入内

## git提交

>git commit -m "@Valid表单验证"

# AOP统一处理请求日志

## AOP，IOC防误区简述

- AOP编程范式
  - 编程范式，与语言无关，是一种设计思想
  - 面向切面（AOP）、面向对象（OOP）【java、c++、c#】、面向过程（POP）【c】
  - 如果说面向对象：垂直切割成各自独立对象，面向切面：就是**抽取通用业务逻辑**

## 背景假设

- 登陆后获得权限调用方法
  - 传统直接想到每个方法if判断（不可行，不能每个都加太麻烦）
  - controller加构造方法if（不可行，spring启动时调用构造方法，后期http请求不会调用构造方法）
  - AOP（推荐，统一进行验证）

## pom依赖

```xml
<!--        添加aop依赖，无需在启动类加注解-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
```

## 新建aspect拦截

- aspect包
  - HttpAspect.java

```java
package com.bennyrhys.girl.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect //aop标识
@Component //添加到容器
public class HttpAspect {
    //拦截方法：加两个..表示任何参数都会被拦截
    @Before("execution(public * com.bennyrhys.girl.controller.GirlController.girlList(..))")
    //先定方法不然没上方提示
    public void log(){
        System.out.println("llllllll");
    }
}
```

检测类下所有方法/*

```java
@Before("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
```



## 验证拦截

查询列表

http://localhost:8081/girl/girls

```
[{"id":3,"cupSize":"G","age":20},{"id":4,"cupSize":"G","age":20},{"id":5,"cupSize":"F","age":22},{"id":6,"cupSize":"F","age":16},{"id":7,"cupSize":"F","age":16},{"id":8,"cupSize":"C","age":22},{"id":9,"cupSize":"C","age":20},{"id":10,"cupSize":"C","age":18}]
```

控制台

>llllllll //被过滤
>Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from girl girl0_

增加少女

http://localhost:8081/girl/girls

>//未被过滤检测
>
>Hibernate: select next_val as id_val from hibernate_sequence for update
>Hibernate: update hibernate_sequence set next_val= ? where next_val=?
>Hibernate: insert into girl (age, cup_size, id) values (?, ?, ?)

## 拦截顺序

HttpAspect

```java
package com.bennyrhys.girl.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect //aop标识
@Component //添加到容器
public class HttpAspect {
    //@Before方法执行之前就执行了
    //拦截方法：加两个..表示任何参数都会被拦截
    @Before("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
    //先定方法不然没上方提示
    public void log(){
        System.out.println("llllllll");
    }


    @After("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
    //先定方法不然没上方提示
    public void doAfter(){
        System.out.println("22222222");
    }
}
```

GirlController

```java
/**
 * 查询所有女生列表
 * @return
 * http://localhost:8081/girl/girls
 * [
 *     {
 *         "id": 1,
 *         "cupSize": "B",
 *         "age": 18
 *     }
 * ]
 */
@GetMapping(value = "/girls")
public List<Girl> girlList(){
    System.out.println("girlList测试检测顺序");
    return repository.findAll();
}
```

控制台打印

> llllllll
> girlList测试检测顺序
> Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from girl girl0_
> 22222222

## 简化拦截代码

HttpAspect

```java
package com.bennyrhys.girl.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect //aop标识
@Component //添加到容器
public class HttpAspect {
    //提取公共方法
    //拦截方法：加两个..表示任何参数都会被拦截
    @Pointcut("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
    //先定方法不然没上方提示
    public void log(){
        System.out.println("1111");
    }

    //@Before方法执行之前就执行了
    @Before("log()")
    public void doBefore(){
        System.out.println("11111111");
    }


    @After("log()")
    //先定方法不然没上方提示
    public void doAfter(){
        System.out.println("22222222");
    }
}
```

控制台

>http://localhost:8081/girl/girls
>
>11111111
>girlList测试检测顺序
>Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from girl girl0_
>22222222

## 日志替换print输出

**HttpAspect**

//日志输出slf4j **注意类名切换**
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

//打印

 logger.info("222222222");

```java
package com.bennyrhys.girl.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect //aop标识
@Component //添加到容器
public class HttpAspect {

    //日志输出slf4j
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);


    //提取公共方法
    //拦截方法：加两个..表示任何参数都会被拦截
    @Pointcut("execution(public * com.bennyrhys.girl.controller.GirlController.*(..))")
    //先定方法不然没上方提示
    public void log(){
    }

    //@Before方法执行之前就执行了
    @Before("log()")
    public void doBefore(){
        logger.info("111111111");
    }


    @After("log()")
    //先定方法不然没上方提示
    public void doAfter(){
        logger.info("222222222");
    }
}
```

GirlController

```java
//日志输出slf4j
private final static Logger logger = LoggerFactory.getLogger(GirlController.class);
        logger.info("girlList测试检测顺序");
```



>2019-12-31 10:07:19.744  INFO 1684 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : 
>
>111111111
>2019-12-31 10:07:19.749  INFO 1684 --- [nio-8081-exec-1] c.b.girl.controller.GirlController       : girlList测试检测顺序
>Hibernate: select girl0_.id as id1_0_, girl0_.age as age2_0_, girl0_.cup_size as cup_size3_0_ from girl girl0_
>2019-12-31 10:07:19.851  INFO 1684 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : 222222222

## 日志获取请求信息

HttpAspect

```java
package com.bennyrhys.girl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
}
```

控制台

>2019-12-31 10:27:11.554  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : url=http://localhost:8081/girl/girls/6
>2019-12-31 10:27:11.554  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : method=GET
>2019-12-31 10:27:11.554  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : ip=0:0:0:0:0:0:0:1
>2019-12-31 10:27:11.555  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : class_method=com.bennyrhys.girl.controller.GirlController.girlFindById
>2019-12-31 10:27:11.555  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : args=6
>Hibernate: select girl0_.id as id1_0_0_, girl0_.age as age2_0_0_, girl0_.cup_size as cup_size3_0_0_ from girl girl0_ where girl0_.id=?
>2019-12-31 10:27:11.603  INFO 1816 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : 222222222

## 日志获取响应信息

HttpAspect

```java
//日志获取响应信息
//入参：returning 注意：toString方法，否则输出是对象
@AfterReturning(returning = "object", pointcut = "log()")
public void doAfterReturning(Object object){
    logger.info("response={}", object.toString());
}
```

控制台

>2019-12-31 10:36:42.905  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : url=http://localhost:8081/girl/girls/6
>2019-12-31 10:36:42.905  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : method=GET
>2019-12-31 10:36:42.905  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : ip=0:0:0:0:0:0:0:1
>2019-12-31 10:36:42.907  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : class_method=com.bennyrhys.girl.controller.GirlController.girlFindById
>2019-12-31 10:36:42.907  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : args=6
>Hibernate: select girl0_.id as id1_0_0_, girl0_.age as age2_0_0_, girl0_.cup_size as cup_size3_0_0_ from girl girl0_ where girl0_.id=?
>2019-12-31 10:36:42.952  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : 222222222
>2019-12-31 10:36:42.952  INFO 1885 --- [nio-8081-exec-1] com.bennyrhys.girl.aspect.HttpAspect     : response=Girl{id=6, cupSize='F', age=16}

## git提交

> "AOP日志"

# 统一异常处理

## 封装返回值json

新建Result

- domain包
  - Result.java

```java
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
}
```

GirlController

```java
/**
 * 新增一个女生
 * http://localhost:8081/girl/girls
 * 注意：post请求
 *
 * 参数
 * cupSize  f
 * age  16
 *
 * 插入因为自增注意id冲突
 */
@PostMapping(value = "/girls")
//@Valid表示要验证的是这个对象 BindingResult返回验证信息
public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
    //判断是否发生错误
    if (bindingResult.hasErrors()){
        //打印错误信息
        Result result = new Result();
        result.setCode(1);
        result.setMsg(bindingResult.getFieldError().getDefaultMessage());
        result.setData(null);
        return result;
    }
    girl.setCupSize(girl.getCupSize());
    girl.setAge(girl.getAge());
    //save返回添加对对象

    Result result =  new Result();
    result.setCode(0);
    result.setMsg("成功");
    result.setData(repository.save(girl));
    return result;
}
```

## 验证返回json

访问：失败

http://localhost:8081/girl/girls

参数

cupSize	D

age	17

响应信息

>{
>
>​    "code": 1,
>
>​    "msg": "未成年少女禁止入内",
>
>​    "data": null
>
>}

参数



访问：成功

http://localhost:8081/girl/girls

参数

cupSize	D

age	18

响应信息

> {
>
> ​    "code": 0,
>
> ​    "msg": "成功",
>
> ​    "data": {
>
> ​        "id": 14,
>
> ​        "cupSize": "D",
>
> ​        "age": 18
>
> ​    }
>
> }

## 代码优化-减少重复

新建

- utils包
  - ResultUtil.java

```java
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
```

GirlController

```java
/**
 * 新增一个女生
 * http://localhost:8081/girl/girls
 * 注意：post请求
 *
 * 参数
 * cupSize  f
 * age  16
 *
 * 插入因为自增注意id冲突
 */
@PostMapping(value = "/girls")
//@Valid表示要验证的是这个对象 BindingResult返回验证信息
public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
    //判断是否发生错误
    if (bindingResult.hasErrors()){
        //打印错误信息
        return ResultUtil.error(1,bindingResult.getFieldError().getDefaultMessage());
    }
    girl.setCupSize(girl.getCupSize());
    girl.setAge(girl.getAge());
    //save返回添加对对象

    return ResultUtil.success(repository.save(girl));
}
```

## 背景假设

获取女生年龄做判断

【	，10），返回“应该在上小学”

【10，16），返回“可能在上初中”

## 业务分析

要如何把信息返回界面？

一般不是简单的一个方法就直接返回页面，业务逻辑复杂，因此一次判断类似一次表单验证，后续还有其他逻辑跟随

- controller判断打标记返回，service继续根据不同标记进行（可以，繁琐，标记乱）
- 统一处理,从service-》if抛异常，controller继续抛异常（推荐）

## 判断信息-外抛异常获取

GirlService

```java
/**
 * 获取女生年龄做判断
 * 【   ，10），返回“应该在上小学”
 *
 * 【10，16），返回“可能在上初中”
 */
public void getAge(Integer id)throws Exception{
    Optional<Girl> optional = repository.findById(id);
    if (optional.isPresent()){
        Girl girl = optional.get();
        Integer age = girl.getAge();
        if(age < 10){
            //【    ，10），返回“应该在上小学”
            throw new Exception("应该在上小学");

        }else if (age >= 10 && age <16){
            //【10，16），返回“可能在上初中”
            throw new Exception("可能在上初中");

        }
    }
}
```

GirlController

```java
/**
 * 获取女生年龄做判断
 * 【   ，10），返回“应该在上小学”
 *
 * 【10，16），返回“可能在上初中”
 */
@GetMapping(value = "/girls/getAge/{id}")
public void getAge(@PathVariable("id") Integer id)throws Exception{
    //逻辑在service中判断
    service.getAge(id);
}
```

访问

http://localhost:8081/girl/girls/getAge/3

响应异常信息

> {
>
> ​    "timestamp": "2019-12-31T08:41:32.890+0000",
>
> ​    "status": 500,
>
> ​    "error": "Internal Server Error",
>
> ​    "message": "应该在上小学",
>
> ​    "path": "/girl/girls/getAge/3"
>
> }

控制台

> java.lang.Exception: 应该在上小学

## git提交-判断信息-外抛异常获取

## 捕获异常封装返回json



