package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoSupplier;
import com.hlyp.api.bean.HesoSupplierExample;
import com.hlyp.api.dao.HesoSupplierMapper;
import com.hlyp.api.service.HesoSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HesoSupplierServiceImpl implements HesoSupplierService {
    @Autowired
    private HesoSupplierMapper supplierMapper ;
    @Override
    public List<HesoSupplier> findAllSupplier() {

        HesoSupplierExample example = new HesoSupplierExample();
        HesoSupplierExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryEqualTo(2);
        List<HesoSupplier> list = supplierMapper.selectByExample(example);

        return list;
    }
}
