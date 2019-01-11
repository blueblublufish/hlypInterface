package com.hlyp.api.controller;

import com.hlyp.api.bean.User;
import com.hlyp.api.param.userParam.UserAddParam;
import com.hlyp.api.service.HesoPayOrderService;
import com.hlyp.api.service.TestInterFace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Lenovo on 2018/12/25.
 */
@RestController
@RequestMapping("/user")
@Api(value = "测试用户模块")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    /*@Autowired
    private TestInterFace testInterFace;

    @Autowired
    private HesoPayOrderService hesoPayOrderService;
    @PostMapping("/num")
    @ResponseBody
    int home(){
        int i = testInterFace.testInterFace();
        return  i ;
    }

    @GetMapping("/get")
    @ApiOperation(value = "根据姓名获取用户")
    User getUser(@RequestParam String userName){
        log.info("this is test log print");

        User user = testInterFace.testUser(userName);
        return user;
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增用户")
    String add(@RequestBody UserAddParam param){
        testInterFace.insertUser(param.getUserName(),param.getSex());
        return "success";
    }

    @PostMapping("/getAll")
    @ResponseBody
    List<User> getAll(){
        return testInterFace.findAll();
    }
*/

}
