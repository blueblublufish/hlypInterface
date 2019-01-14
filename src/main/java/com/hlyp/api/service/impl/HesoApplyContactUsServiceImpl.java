package com.hlyp.api.service.impl;

import com.hlyp.api.bean.HesoApplyContactUs;
import com.hlyp.api.bean.HesoSupplier;
import com.hlyp.api.dao.HesoAccountMapper;
import com.hlyp.api.dao.HesoApplyContactUsMapper;
import com.hlyp.api.service.HesoApplyContactUsService;
import com.hlyp.api.service.HesoSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HesoApplyContactUsServiceImpl implements HesoApplyContactUsService {

    @Autowired
    private HesoApplyContactUsMapper applyContactUsMapper;
    @Override
    public int addHesoApplyContactYs(HesoApplyContactUs us) {
        int result = applyContactUsMapper.insert(us);
        return result;
    }
}
