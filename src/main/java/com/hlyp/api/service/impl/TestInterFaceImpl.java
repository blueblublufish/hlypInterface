package com.hlyp.api.service.impl;

import com.hlyp.api.bean.User;
import com.hlyp.api.bean.UserExample;
import com.hlyp.api.dao.UserDao;
import com.hlyp.api.dao.UserMapper;
import com.hlyp.api.service.TestInterFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/25.
 */
@Service
public class TestInterFaceImpl implements TestInterFace {
    @Autowired
    UserDao userDao;
    @Autowired
    UserMapper userMapper;

    @Override
    public int testInterFace() {
        return 0;
    }

    @Override
    public User testUser(String username) {
        User user = new User();
        user.setUsername(username);
       // User userById = userMapper.selectOne(user);

        user.setUsername(username);
        User ser = userMapper.selectByPrimaryKey(1);
        User user2 = new User();
        user2.setId(ser.getId());
        user2.setNewtest("a test update");
        int d = userMapper.updateByPrimaryKeySelective(user2);
        UserExample example = new UserExample();
        UserExample.Criteria criteria =example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<User> userresult = userMapper.selectByExample(example);
        user.setSex("33");


        return user ;
    }

    @Override
    public int insertUser(String name, String sex) {
        User user = new User();
        user.setSex(sex);
        user.setUsername(name);
        return userDao.insert(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.selectAll();
    }

    /*@Override
    public User testUser(String username) {

        return userDao.findByName(username);
    }

    @Override
    public int insertUser(String name, String sex) {
        return userDao.insert(name,sex);
    }*/
}
