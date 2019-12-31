package com.bennyrhys.girl.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc //检测api
class GirlControllerTest {

    @Autowired
    private GirlController girlController;
    @Autowired
    private MockMvc mvc;

    @Test
    void girlList() throws Exception {
//        不是这样测试，这样测试没有报错，但没检测到api及请求类型
//        girlController.girlList();
        mvc.perform(MockMvcRequestBuilders.get("/girls"))
                .andExpect(MockMvcResultMatchers.status().isOk()); //期望返回状态码200，测试通过
                //.andExpect(MockMvcResultMatchers.content().string("abc")); //期望返回string=abc，测试肯定不通过
/**
 * 检测abc返回，控制台输出
 * Expected :abc
 * Actual   :[{"id":3,"cupSize":"G","age":9},{"id":4,"cupSize":"G","age":15},{"id":5,"cupSize":"F","age":22},{"id":6,"cupSize":"F","age":16},{"id":7,"cupSize":"F","age":16},{"id":8,"cupSize":"C","age":22},{"id":9,"cupSize":"C","age":20},{"id":10,"cupSize":"C","age":18},{"id":11,"cupSize":"D","age":19},{"id":12,"cupSize":"D","age":18},{"id":13,"cupSize":"D","age":18},{"id":14,"cupSize":"D","age":18},{"id":15,"cupSize":"D","age":18}]
 */
    }
}