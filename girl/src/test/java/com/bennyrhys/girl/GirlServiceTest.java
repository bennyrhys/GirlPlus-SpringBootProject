package com.bennyrhys.girl;

import com.bennyrhys.girl.domain.Girl;
import com.bennyrhys.girl.service.GirlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest //启动整个spring工程
public class GirlServiceTest {
    @Autowired
    private GirlService girlService;

    @Test
    public void findOneTest(){
        Girl girl = girlService.findOne(3);
        //断言比较，测试通过
        Assertions.assertEquals(9,girl.getAge());
    }
}
