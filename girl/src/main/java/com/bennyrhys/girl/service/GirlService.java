package com.bennyrhys.girl.service;

import com.bennyrhys.girl.domain.Girl;
import com.bennyrhys.girl.repository.GirlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GirlService {
    @Autowired
    private GirlRepository repository;

    @Transactional //保证同时发生的注解，spring版
    public void changgeTwo(){
        Girl girl = new Girl();
        girl.setId(3);
        girl.setAge(20);
        girl.setCupSize("G");
        repository.save(girl);

        Girl girl2 = new Girl();
        girl2.setId(4);
        girl2.setAge(20);
        girl2.setCupSize("G");
        repository.save(girl2);
    }

    /**
     * 获取女生年龄做判断
     * 【	，10），返回“应该在上小学”
     *
     * 【10，16），返回“可能在上初中”
     */
    public void getAge(Integer id)throws Exception{
        Optional<Girl> optional = repository.findById(id);
        if (optional.isPresent()){
            Girl girl = optional.get();
            Integer age = girl.getAge();
            if(age < 10){
                //【	，10），返回“应该在上小学”
                throw new Exception("应该在上小学");

            }else if (age >= 10 && age <16){
                //【10，16），返回“可能在上初中”
                throw new Exception("可能在上初中");

            }
        }
    }
}
