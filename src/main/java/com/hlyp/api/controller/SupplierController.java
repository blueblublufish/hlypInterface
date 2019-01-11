package com.hlyp.api.controller;

import com.hlyp.api.bean.HesoSupplier;
import com.hlyp.api.dto.supplier.SupplierDto;
import com.hlyp.api.service.HesoSupplierService;
import com.hlyp.api.vo.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("supplier")
@Api(value = "供应商模块")
public class SupplierController {

    @Autowired
    private HesoSupplierService supplierService ;
    @PostMapping("/findAll")
    @ApiOperation(value = "查询所有供应商")
    public Json findAllSupplier(){
        List<HesoSupplier> list = supplierService.findAllSupplier();
        List<SupplierDto> dtoList = new ArrayList<SupplierDto>();
        for(HesoSupplier supplier : list){
            SupplierDto dto = new SupplierDto();
            BeanUtils.copyProperties(supplier,dto);
            dtoList.add(dto);
        }
        Json json = new Json();
        json.setMsg("查找成功");
        json.setSuccess(true);
        json.setData(dtoList);
        return  json;
    }
}
