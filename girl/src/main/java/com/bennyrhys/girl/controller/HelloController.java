package com.bennyrhys.girl.controller;

import com.bennyrhys.girl.properties.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/say")
public class HelloController {
    //通过注解获取配置:类中的属性
    @Autowired
    private GirlConfig girlConfig;

//    @RequestMapping(value = {"/hi"}, method = RequestMethod.GET)
    @GetMapping(value = "/hi")

    public String say(@RequestParam(value = "id", required = false, defaultValue = "0") Integer myid){
//        return girlConfig.getCupSize()+girlConfig.getAge();
        return "id："+myid;
    }
}
