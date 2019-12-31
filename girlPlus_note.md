[TOC]

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

>
>
>

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

