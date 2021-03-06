package com.bennyrhys.girl.controller;

import com.bennyrhys.girl.aspect.HttpAspect;
import com.bennyrhys.girl.domain.Girl;
import com.bennyrhys.girl.domain.Result;
import com.bennyrhys.girl.repository.GirlRepository;
import com.bennyrhys.girl.service.GirlService;
import com.bennyrhys.girl.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class GirlController {

    @Autowired
    private GirlRepository repository;
    @Autowired
    private GirlService service;

    //日志输出slf4j
    private final static Logger logger = LoggerFactory.getLogger(GirlController.class);


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
        logger.info("girlList测试检测顺序");
        return repository.findAll();
    }
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
    /**
     * 通过id查询一个女生
     * @return
     * http://localhost:8081/girl/girl/2
     * {
     *     "id": 2,
     *     "cupSize": "F",
     *     "age": 16
     * }
     */
    @GetMapping(value = "/girls/{id}")
    public Girl girlFindById(@PathVariable("id") Integer id){
        return repository.findById(id).orElse(null);

    }

    /**
     * 更新某个id女生基因信息
     * http://localhost:8081/girl/girls/2
     * 参数
     * cupSize  G
     * age  22
     *
     * {
     *     "id": 2,
     *     "cupSize": "G",
     *     "age": 22
     * }
     */
    @PutMapping(value = "/girls/{id}")
    public Girl updateGirl(@PathVariable("id") Integer id,
                           @RequestParam("cupSize") String cupSize,
                           @RequestParam("age") Integer age){
        Optional<Girl> optional = repository.findById(id);
        if (optional.isPresent()){
            Girl girl = optional.get();
            girl.setAge(age);
            girl.setCupSize(cupSize);
            return girl;
        }
        return null;
    }
    /**
     *删除某个id女生的信息
     * http://localhost:8081/girl/girls/2
     * 2
     */
    @DeleteMapping(value = "/girls/{id}")
    public Integer deleteGirlById(@PathVariable("id") Integer id){
        Optional<Girl> optional = repository.findById(id);
        if (optional.isPresent()){
            Girl girl = optional.get();
            repository.delete(girl);
            return id;
        }
        return null;
    }
    /**
     * 通过年龄查询女生列表
     * 先自定义dao方法
     * http://localhost:8081/girl/girls/age/16
     * [
     *     {
     *         "id": 3,
     *         "cupSize": "F",
     *         "age": 16
     *     },
     *     {
     *         "id": 4,
     *         "cupSize": "g",
     *         "age": 16
     *     },
     */
    @GetMapping("/girls/age/{age}")
    public List<Girl> girlListByAge(@PathVariable("age") Integer age){
        return repository.findByAge(age);
    }

    /**
     * 触发事务
     * 同时改变基因cupSize
     * http://localhost:8081/girl/girls/two
     * 数据发生改变
     */
    @PostMapping(value = "/girls/two")
    public void girlsTwo(){
        service.changgeTwo();
    }

    /**
     * 获取女生年龄做判断
     * 【	，10），返回“应该在上小学”
     *
     * 【10，16），返回“可能在上初中”
     */
    @GetMapping(value = "/girls/getAge/{id}")
    public void getAge(@PathVariable("id") Integer id)throws Exception{
        //逻辑在service中判断
        service.getAge(id);
    }
}
