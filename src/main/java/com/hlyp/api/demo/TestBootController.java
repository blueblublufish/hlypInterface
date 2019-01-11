package com.hlyp.api.demo;

import com.hlyp.api.service.TestInterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lenovo on 2018/12/25.
 */
@Controller
@RequestMapping("test")
@ComponentScan(basePackages = {"com.hlyp.api.service"})
public class TestBootController {
    @Autowired
    private TestInterFace testInterFace;
    @RequestMapping("/num")
    @ResponseBody
    int home(){
        int i = testInterFace.testInterFace();
        return  i ;
    }



    public static void  main(String[] args) throws Exception{
        SpringApplication.run(TestBootController.class);

    }


}
