package com.hlyp.api.service;

import com.hlyp.api.bean.User;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/25.
 */
public interface TestInterFace {
    public int testInterFace();
    public User testUser(String username);
    public int insertUser(String name,String sex);
    List <User> findAll();
}
